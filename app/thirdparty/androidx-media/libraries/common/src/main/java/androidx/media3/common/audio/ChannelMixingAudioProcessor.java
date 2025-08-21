/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common.audio;

import static androidx.media3.common.util.Assertions.checkStateNotNull;

import android.util.SparseArray;
import androidx.annotation.Nullable;
import androidx.media3.common.C;
import androidx.media3.common.util.UnstableApi;
import java.nio.ByteBuffer;

/**
 * An {@link AudioProcessor} that handles mixing and scaling audio channels. Call {@link
 * #putChannelMixingMatrix(ChannelMixingMatrix)} specifying mixing matrices to apply for each
 * possible input channel count before using the audio processor. Input and output are 16-bit PCM.
 */
@UnstableApi
public final class ChannelMixingAudioProcessor extends BaseAudioProcessor {

  private final SparseArray<ChannelMixingMatrix> matrixByInputChannelCount;

  /** Creates a new audio processor for mixing and scaling audio channels. */
  public ChannelMixingAudioProcessor() {
    matrixByInputChannelCount = new SparseArray<>();
  }

  /**
   * Stores a channel mixing matrix for processing audio with a given {@link
   * ChannelMixingMatrix#getInputChannelCount() channel count}. Overwrites any previously stored
   * matrix for the same input channel count.
   */
  public void putChannelMixingMatrix(ChannelMixingMatrix matrix) {
    int inputChannelCount = matrix.getInputChannelCount();
    matrixByInputChannelCount.put(inputChannelCount, matrix);
  }

  @Override
  protected AudioFormat onConfigure(AudioFormat inputAudioFormat)
      throws UnhandledAudioFormatException {
    // TODO(b/290002731): Expand to allow float due to AudioMixingUtil built-in support for float.
    if (inputAudioFormat.encoding != C.ENCODING_PCM_16BIT) {
      throw new UnhandledAudioFormatException(inputAudioFormat);
    }
    @Nullable
    ChannelMixingMatrix channelMixingMatrix =
        matrixByInputChannelCount.get(inputAudioFormat.channelCount);
    if (channelMixingMatrix == null) {
      throw new UnhandledAudioFormatException(
          "No mixing matrix for input channel count", inputAudioFormat);
    }
    if (channelMixingMatrix.isIdentity()) {
      return AudioFormat.NOT_SET;
    }
    return new AudioFormat(
        inputAudioFormat.sampleRate,
        channelMixingMatrix.getOutputChannelCount(),
        C.ENCODING_PCM_16BIT);
  }

  @Override
  public void queueInput(ByteBuffer inputBuffer) {
    ChannelMixingMatrix channelMixingMatrix =
        checkStateNotNull(matrixByInputChannelCount.get(inputAudioFormat.channelCount));

    int framesToMix = inputBuffer.remaining() / inputAudioFormat.bytesPerFrame;
    ByteBuffer outputBuffer = replaceOutputBuffer(framesToMix * outputAudioFormat.bytesPerFrame);
    AudioMixingUtil.mix(
        inputBuffer,
        inputAudioFormat,
        outputBuffer,
        outputAudioFormat,
        channelMixingMatrix,
        framesToMix,
        /* accumulate= */ false,
        /* clipFloatOutput= */ true);
    outputBuffer.flip();
  }
}
