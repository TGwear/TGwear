/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.audio;

import static com.google.common.truth.Truth.assertThat;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import androidx.media3.common.C;
import androidx.media3.common.util.Util;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.common.collect.ImmutableList;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit tests for {@link WaveformAudioBufferSinkTest}. */
@RunWith(AndroidJUnit4.class)
public final class WaveformAudioBufferSinkTest {
  private static final long TIMEOUT_MS = 1_000;
  private static final double ALLOWED_FLOAT_CONVERSION_ERROR = 0.0001;

  @Test
  public void handleBuffer_monoToMono16Bit_callbackHasExpectedValue() throws Exception {
    ByteBuffer byteBuffer = ByteBuffer.allocate(4);
    byteBuffer.putShort(0, (short) (Short.MIN_VALUE / 3));
    byteBuffer.putShort(2, (short) (Short.MAX_VALUE / 2));
    ImmutableList<WaveformAudioBufferSink.WaveformBar> channels =
        calculateChannelWaveformBars(
            byteBuffer,
            /* inputChannelCount= */ 1,
            /* outputChannelCount= */ 1,
            C.ENCODING_PCM_16BIT);

    assertThat(channels.get(0).getSampleCount()).isEqualTo(2);
    assertThat(channels.get(0).getMinSampleValue())
        .isWithin(ALLOWED_FLOAT_CONVERSION_ERROR)
        .of(-0.3333);
    assertThat(channels.get(0).getMaxSampleValue())
        .isWithin(ALLOWED_FLOAT_CONVERSION_ERROR)
        .of(0.5);
    assertThat(channels.get(0).getRootMeanSquare())
        .isWithin(ALLOWED_FLOAT_CONVERSION_ERROR)
        .of(0.4249);
  }

  @Test
  public void handleBuffer_stereoToMono16Bit_callbackHasExpectedValue() throws Exception {
    ByteBuffer byteBuffer = ByteBuffer.allocate(8);
    byteBuffer.putShort(0, (short) (Short.MIN_VALUE / 3));
    byteBuffer.putShort(2, (short) (Short.MIN_VALUE / 3));
    byteBuffer.putShort(4, (short) (Short.MAX_VALUE / 2));
    byteBuffer.putShort(6, (short) (Short.MAX_VALUE / 2));
    ImmutableList<WaveformAudioBufferSink.WaveformBar> channels =
        calculateChannelWaveformBars(
            byteBuffer,
            /* inputChannelCount= */ 2,
            /* outputChannelCount= */ 1,
            C.ENCODING_PCM_16BIT);

    assertThat(channels.get(0).getSampleCount()).isEqualTo(2);
    assertThat(channels.get(0).getMinSampleValue())
        .isWithin(ALLOWED_FLOAT_CONVERSION_ERROR)
        .of(-0.3333);
    assertThat(channels.get(0).getMaxSampleValue())
        .isWithin(ALLOWED_FLOAT_CONVERSION_ERROR)
        .of(0.5);
    assertThat(channels.get(0).getRootMeanSquare())
        .isWithin(ALLOWED_FLOAT_CONVERSION_ERROR)
        .of(0.4249);
  }

  @Test
  public void handleBuffer_stereoToStereo16Bit_callbackHasExpectedValue() throws Exception {
    ByteBuffer byteBuffer = ByteBuffer.allocate(8);
    byteBuffer.putShort(0, (short) (Short.MIN_VALUE / 3));
    byteBuffer.putShort(2, (short) (Short.MIN_VALUE / 2));
    byteBuffer.putShort(4, (short) (Short.MAX_VALUE / 2));
    byteBuffer.putShort(6, (short) (Short.MAX_VALUE / 3));
    ImmutableList<WaveformAudioBufferSink.WaveformBar> channels =
        calculateChannelWaveformBars(
            byteBuffer,
            /* inputChannelCount= */ 2,
            /* outputChannelCount= */ 2,
            C.ENCODING_PCM_16BIT);

    assertThat(channels.get(0).getSampleCount()).isEqualTo(2);
    assertThat(channels.get(0).getMinSampleValue())
        .isWithin(ALLOWED_FLOAT_CONVERSION_ERROR)
        .of(-0.3333);
    assertThat(channels.get(0).getMaxSampleValue())
        .isWithin(ALLOWED_FLOAT_CONVERSION_ERROR)
        .of(0.5);
    assertThat(channels.get(0).getRootMeanSquare())
        .isWithin(ALLOWED_FLOAT_CONVERSION_ERROR)
        .of(0.4249);

    assertThat(channels.get(1).getSampleCount()).isEqualTo(2);
    assertThat(channels.get(1).getMinSampleValue())
        .isWithin(ALLOWED_FLOAT_CONVERSION_ERROR)
        .of(-0.5);
    assertThat(channels.get(1).getMaxSampleValue())
        .isWithin(ALLOWED_FLOAT_CONVERSION_ERROR)
        .of(0.3333);
    assertThat(channels.get(1).getRootMeanSquare())
        .isWithin(ALLOWED_FLOAT_CONVERSION_ERROR)
        .of(0.4249);
  }

  @Test
  public void handleBuffer_monoToMonoFloat_callbackHasExpectedValue() throws Exception {
    ByteBuffer byteBuffer = ByteBuffer.allocate(8);
    byteBuffer.putFloat(0, -0.3333f);
    byteBuffer.putFloat(4, 0.5f);
    ImmutableList<WaveformAudioBufferSink.WaveformBar> channels =
        calculateChannelWaveformBars(
            byteBuffer,
            /* inputChannelCount= */ 1,
            /* outputChannelCount= */ 1,
            C.ENCODING_PCM_FLOAT);

    assertThat(channels.get(0).getSampleCount()).isEqualTo(2);
    assertThat(channels.get(0).getMinSampleValue())
        .isWithin(ALLOWED_FLOAT_CONVERSION_ERROR)
        .of(-0.3333);
    assertThat(channels.get(0).getMaxSampleValue())
        .isWithin(ALLOWED_FLOAT_CONVERSION_ERROR)
        .of(0.5);
    assertThat(channels.get(0).getRootMeanSquare())
        .isWithin(ALLOWED_FLOAT_CONVERSION_ERROR)
        .of(0.4249);
  }

  private ImmutableList<WaveformAudioBufferSink.WaveformBar> calculateChannelWaveformBars(
      ByteBuffer byteBuffer,
      int inputChannelCount,
      int outputChannelCount,
      @C.PcmEncoding int encoding)
      throws InterruptedException {
    List<WaveformAudioBufferSink.WaveformBar> channels = new ArrayList<>(outputChannelCount);
    for (int i = 0; i < outputChannelCount; i++) {
      channels.add(new WaveformAudioBufferSink.WaveformBar());
    }
    CountDownLatch countDownLatch = new CountDownLatch(outputChannelCount);
    WaveformAudioBufferSink waveformAudioBufferSink =
        new WaveformAudioBufferSink(
            /* barsPerSecond= */ 1,
            outputChannelCount,
            (channelIndex, bar) -> {
              countDownLatch.countDown();
              channels.set(channelIndex, bar);
            });
    int sampleRateHz = byteBuffer.remaining() / Util.getPcmFrameSize(encoding, inputChannelCount);
    waveformAudioBufferSink.flush(sampleRateHz, inputChannelCount, encoding);
    waveformAudioBufferSink.handleBuffer(byteBuffer);
    assertThat(countDownLatch.await(TIMEOUT_MS, MILLISECONDS)).isTrue();
    return ImmutableList.copyOf(channels);
  }
}
