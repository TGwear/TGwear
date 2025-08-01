/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.audio;

import static androidx.media3.test.utils.TestUtil.createByteBuffer;
import static androidx.media3.test.utils.TestUtil.createFloatArray;
import static androidx.media3.test.utils.TestUtil.createShortArray;
import static com.google.common.truth.Truth.assertThat;

import androidx.media3.common.C;
import androidx.media3.common.audio.AudioProcessor.AudioFormat;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit tests for {@link TrimmingAudioProcessor}. */
@RunWith(AndroidJUnit4.class)
public final class TrimmingAudioProcessorTest {

  private static final AudioFormat STEREO_PCM16_FORMAT =
      new AudioFormat(/* sampleRate= */ 44100, /* channelCount= */ 2, C.ENCODING_PCM_16BIT);
  private static final AudioFormat STEREO_PCM_FLOAT_FORMAT =
      new AudioFormat(/* sampleRate= */ 44100, /* channelCount= */ 2, C.ENCODING_PCM_FLOAT);
  private static final int TRACK_ONE_UNTRIMMED_FRAME_COUNT = 1024;
  private static final int TRACK_ONE_TRIM_START_FRAME_COUNT = 64;
  private static final int TRACK_ONE_TRIM_END_FRAME_COUNT = 32;
  private static final int TRACK_TWO_TRIM_START_FRAME_COUNT = 128;
  private static final int TRACK_TWO_TRIM_END_FRAME_COUNT = 16;

  private static final int TRACK_ONE_BUFFER_SIZE_BYTES =
      STEREO_PCM16_FORMAT.bytesPerFrame * TRACK_ONE_UNTRIMMED_FRAME_COUNT;
  private static final int TRACK_ONE_TRIMMED_BUFFER_SIZE_BYTES =
      TRACK_ONE_BUFFER_SIZE_BYTES
          - STEREO_PCM16_FORMAT.bytesPerFrame
              * (TRACK_ONE_TRIM_START_FRAME_COUNT + TRACK_ONE_TRIM_END_FRAME_COUNT);

  @Test
  public void flushTwice_trimsStartAndEnd() throws Exception {
    TrimmingAudioProcessor trimmingAudioProcessor = new TrimmingAudioProcessor();
    trimmingAudioProcessor.setTrimFrameCount(
        TRACK_ONE_TRIM_START_FRAME_COUNT, TRACK_ONE_TRIM_END_FRAME_COUNT);
    trimmingAudioProcessor.configure(STEREO_PCM16_FORMAT);
    trimmingAudioProcessor.flush();
    trimmingAudioProcessor.flush();

    // Feed and drain the processor, simulating a gapless transition to another track.
    ByteBuffer inputBuffer = ByteBuffer.allocate(TRACK_ONE_BUFFER_SIZE_BYTES);
    int outputSize = 0;
    while (!trimmingAudioProcessor.isEnded()) {
      if (inputBuffer.hasRemaining()) {
        trimmingAudioProcessor.queueInput(inputBuffer);
        if (!inputBuffer.hasRemaining()) {
          // Reconfigure for a next track then begin draining.
          trimmingAudioProcessor.setTrimFrameCount(
              TRACK_TWO_TRIM_START_FRAME_COUNT, TRACK_TWO_TRIM_END_FRAME_COUNT);
          trimmingAudioProcessor.configure(STEREO_PCM16_FORMAT);
          trimmingAudioProcessor.queueEndOfStream();
        }
      }
      ByteBuffer outputBuffer = trimmingAudioProcessor.getOutput();
      outputSize += outputBuffer.remaining();
      outputBuffer.clear();
    }
    trimmingAudioProcessor.reset();

    assertThat(trimmingAudioProcessor.getTrimmedFrameCount())
        .isEqualTo(TRACK_ONE_TRIM_START_FRAME_COUNT + TRACK_ONE_TRIM_END_FRAME_COUNT);
    assertThat(outputSize).isEqualTo(TRACK_ONE_TRIMMED_BUFFER_SIZE_BYTES);
  }

  @Test
  public void trim_withPcm16Samples_removesExpectedSamples() throws Exception {
    TrimmingAudioProcessor trimmingAudioProcessor = new TrimmingAudioProcessor();
    ByteBuffer resultBuffer = ByteBuffer.allocateDirect(8).order(ByteOrder.nativeOrder());
    trimmingAudioProcessor.setTrimFrameCount(1, 2);
    trimmingAudioProcessor.configure(STEREO_PCM16_FORMAT);
    trimmingAudioProcessor.flush();

    ByteBuffer inputBuffer = createByteBuffer(new short[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
    while (!trimmingAudioProcessor.isEnded()) {
      if (inputBuffer.hasRemaining()) {
        trimmingAudioProcessor.queueInput(inputBuffer);
        if (!inputBuffer.hasRemaining()) {
          trimmingAudioProcessor.configure(STEREO_PCM16_FORMAT);
          trimmingAudioProcessor.queueEndOfStream();
        }
      }
      resultBuffer.put(trimmingAudioProcessor.getOutput());
    }
    resultBuffer.flip();

    assertThat(trimmingAudioProcessor.getTrimmedFrameCount()).isEqualTo(3);
    assertThat(createShortArray(resultBuffer)).isEqualTo(new short[] {3, 4, 5, 6});
  }

  @Test
  public void trim_withPcmFloatSamples_removesExpectedSamples() throws Exception {
    TrimmingAudioProcessor trimmingAudioProcessor = new TrimmingAudioProcessor();
    ByteBuffer resultBuffer = ByteBuffer.allocateDirect(8).order(ByteOrder.nativeOrder());
    trimmingAudioProcessor.setTrimFrameCount(2, 2);
    trimmingAudioProcessor.configure(STEREO_PCM_FLOAT_FORMAT);
    trimmingAudioProcessor.flush();

    ByteBuffer inputBuffer =
        createByteBuffer(new float[] {1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f});
    while (!trimmingAudioProcessor.isEnded()) {
      if (inputBuffer.hasRemaining()) {
        trimmingAudioProcessor.queueInput(inputBuffer);
        if (!inputBuffer.hasRemaining()) {
          trimmingAudioProcessor.configure(STEREO_PCM_FLOAT_FORMAT);
          trimmingAudioProcessor.queueEndOfStream();
        }
      }
      resultBuffer.put(trimmingAudioProcessor.getOutput());
    }
    resultBuffer.flip();

    assertThat(trimmingAudioProcessor.getTrimmedFrameCount()).isEqualTo(4);
    assertThat(createFloatArray(resultBuffer)).isEqualTo(new float[] {5f, 6f});
  }
}
