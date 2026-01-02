/*
 * Copyright (c) 2026 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.gohj99.tgwear.utils

import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jtransforms.fft.DoubleFFT_1D
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.sqrt

suspend fun waveformTo5bit(file: File): ByteArray {

    val pcm = decodeToPCM(file)
    val (waveform, _) = extractWaveformUsingJTransforms(pcm)

    val bits = waveform.map { (it * 31).toInt().coerceIn(0, 31) }

    val output = mutableListOf<Byte>()
    var buffer = 0
    var bitPos = 0

    for (value in bits) {
        buffer = buffer or (value shl bitPos)
        bitPos += 5

        while (bitPos >= 8) {
            output.add((buffer and 0xFF).toByte())
            buffer = buffer shr 8
            bitPos -= 8
        }
    }

    if (bitPos > 0) {
        output.add((buffer and 0xFF).toByte())
    }

    return output.toByteArray()
}

suspend fun decodeToPCM(file: File): ShortArray = withContext(Dispatchers.IO) {
    val extractor = MediaExtractor()
    // 1. 将 codec 定义在 try 外面，设为可空
    var codec: MediaCodec? = null

    try {
        extractor.setDataSource(file.absolutePath)

        val trackIndex = (0 until extractor.trackCount).firstOrNull {
            extractor.getTrackFormat(it).getString(MediaFormat.KEY_MIME)?.startsWith("audio/") == true
        } ?: error("No audio track found")

        extractor.selectTrack(trackIndex)
        val format = extractor.getTrackFormat(trackIndex)
        val mime = format.getString(MediaFormat.KEY_MIME)!!

        // 2. 创建解码器
        codec = MediaCodec.createDecoderByType(mime)
        codec.configure(format, null, null, 0)
        codec.start()

        val outputStream = ByteArrayOutputStream()
        val bufferInfo = MediaCodec.BufferInfo()

        // 注意：API 21+ 建议使用 getOutputBuffer(index)，但为了兼容你原有的逻辑保持不变
        // 仅当你 targetSdk 很高时，inputBuffers/outputBuffers 这种写法才会被标记过时，但依然可用
        val inputBuffers = codec.inputBuffers
        val outputBuffers = codec.outputBuffers

        var inputDone = false
        var outputDone = false

        // 增加一个简单的超时保护，避免死锁
        val timeoutUs = 5000L

        while (!outputDone) {
            // 检查协程是否被取消，如果取消了抛出异常，触发 finally 释放资源
            // ensureActive() // 如果你在协程中，建议加上这句

            if (!inputDone) {
                val inIndex = codec.dequeueInputBuffer(timeoutUs)
                if (inIndex >= 0) {
                    val buffer = inputBuffers[inIndex]
                    val sampleSize = extractor.readSampleData(buffer, 0)
                    if (sampleSize < 0) {
                        codec.queueInputBuffer(inIndex, 0, 0, 0L, MediaCodec.BUFFER_FLAG_END_OF_STREAM)
                        inputDone = true
                    } else {
                        codec.queueInputBuffer(inIndex, 0, sampleSize, extractor.sampleTime, 0)
                        extractor.advance()
                    }
                }
            }

            val outIndex = codec.dequeueOutputBuffer(bufferInfo, timeoutUs)
            if (outIndex >= 0) {
                val buffer = outputBuffers[outIndex]
                val chunk = ByteArray(bufferInfo.size)
                buffer.get(chunk)
                outputStream.write(chunk)
                buffer.clear()
                codec.releaseOutputBuffer(outIndex, false)

                if (bufferInfo.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM != 0) {
                    outputDone = true
                }
            } else if (outIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                // 格式改变，通常可以忽略，或者在这里更新采样率
            }
        }

        val pcmBytes = outputStream.toByteArray()
        val shorts = ByteBuffer.wrap(pcmBytes)
            .order(ByteOrder.LITTLE_ENDIAN)
            .asShortBuffer()
        val result = ShortArray(shorts.limit())
        shorts.get(result)
        return@withContext result

    } catch (e: Exception) {
        e.printStackTrace()
        // 出错时返回空数组，或者根据需要抛出
        return@withContext ShortArray(0)
    } finally {
        // 3. 【绝对安全防线】无论上面发生什么（报错、取消、成功），这里都会执行
        try {
            codec?.stop()
        } catch (e: Exception) { /* 忽略 stop 失败 */ }

        try {
            codec?.release()
        } catch (e: Exception) { /* 忽略 release 失败 */ }

        try {
            extractor.release()
        } catch (e: Exception) { /* 忽略 extractor 失败 */ }
    }
}

fun extractWaveformUsingJTransforms(
    pcm: ShortArray,
    sampleRate: Int = 44100,
    frameSize: Int = 1024,
    hopSize: Int = 512,
    lowBand: IntRange = 0..1000,
    highBand: IntRange = 2000..4000
): Pair<List<Float>, List<Float>> {
    val fft = DoubleFFT_1D(frameSize.toLong())
    val lowFreq = mutableListOf<Float>()
    val highFreq = mutableListOf<Float>()
    val freqPerBin = (sampleRate / 2.0) / (frameSize / 2)

    for (i in 0 until pcm.size - frameSize step hopSize) {
        val frame = DoubleArray(frameSize)
        for (j in frame.indices) {
            frame[j] = pcm[i + j] / Short.MAX_VALUE.toDouble()
        }

        // Prepare real FFT input: double[2N]
        val fftData = DoubleArray(frameSize * 2)
        frame.copyInto(fftData, 0)

        fft.realForwardFull(fftData)

        val magnitudes = DoubleArray(frameSize / 2)
        for (k in 0 until frameSize / 2) {
            val re = fftData[2 * k]
            val im = fftData[2 * k + 1]
            magnitudes[k] = sqrt(re * re + im * im)
        }

        val low = lowBand.mapNotNull { freq ->
            val bin = (freq / freqPerBin).toInt()
            magnitudes.getOrNull(bin)
        }.average().toFloat()

        val high = highBand.mapNotNull { freq ->
            val bin = (freq / freqPerBin).toInt()
            magnitudes.getOrNull(bin)
        }.average().toFloat()

        lowFreq.add(low)
        highFreq.add(high)
    }

    // Normalize
    val max = (lowFreq + highFreq).maxOrNull() ?: 1f
    return Pair(
        lowFreq.map { (it / max).coerceIn(0f, 1f) },
        highFreq.map { (it / max).coerceIn(0f, 1f) }
    )
}
