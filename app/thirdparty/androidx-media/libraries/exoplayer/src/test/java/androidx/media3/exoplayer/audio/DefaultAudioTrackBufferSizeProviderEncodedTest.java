/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.audio;

import static androidx.media3.common.C.MICROS_PER_SECOND;
import static androidx.media3.exoplayer.audio.DefaultAudioSink.OUTPUT_MODE_PASSTHROUGH;
import static androidx.media3.exoplayer.audio.DefaultAudioTrackBufferSizeProvider.getMaximumEncodedRateBytesPerSecond;
import static com.google.common.truth.Truth.assertThat;

import androidx.media3.common.C;
import androidx.media3.common.Format;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.ParameterizedRobolectricTestRunner;

/**
 * Tests for {@link DefaultAudioTrackBufferSizeProvider} for encoded audio except {@link
 * C#ENCODING_AC3}.
 */
@RunWith(ParameterizedRobolectricTestRunner.class)
public class DefaultAudioTrackBufferSizeProviderEncodedTest {

  private static final DefaultAudioTrackBufferSizeProvider DEFAULT =
      new DefaultAudioTrackBufferSizeProvider.Builder().build();

  @ParameterizedRobolectricTestRunner.Parameter(0)
  public @C.Encoding int encoding;

  @ParameterizedRobolectricTestRunner.Parameters(name = "{index}: encoding={0}")
  public static ImmutableList<Integer> data() {
    return ImmutableList.of(
        C.ENCODING_MP3,
        C.ENCODING_AAC_LC,
        C.ENCODING_AAC_HE_V1,
        C.ENCODING_E_AC3,
        C.ENCODING_E_AC3_JOC,
        C.ENCODING_AC4,
        C.ENCODING_DTS,
        C.ENCODING_DOLBY_TRUEHD);
  }

  @Test
  public void getBufferSizeInBytes_veryBigMinBufferSize_isMinBufferSize() {
    int bufferSize =
        DEFAULT.getBufferSizeInBytes(
            /* minBufferSizeInBytes= */ 123456789,
            /* encoding= */ encoding,
            /* outputMode= */ OUTPUT_MODE_PASSTHROUGH,
            /* pcmFrameSize= */ 1,
            /* sampleRate= */ 0,
            /* bitrate= */ Format.NO_VALUE,
            /* maxAudioTrackPlaybackSpeed= */ 0);

    assertThat(bufferSize).isEqualTo(123456789);
  }

  @Test
  public void
      getBufferSizeInBytes_passThroughAndBitrateNotSet_returnsBufferSizeWithAssumedBitrate() {
    int bufferSize =
        DEFAULT.getBufferSizeInBytes(
            /* minBufferSizeInBytes= */ 0,
            /* encoding= */ encoding,
            /* outputMode= */ OUTPUT_MODE_PASSTHROUGH,
            /* pcmFrameSize= */ 1,
            /* sampleRate= */ 0,
            /* bitrate= */ Format.NO_VALUE,
            /* maxAudioTrackPlaybackSpeed= */ 1);

    assertThat(bufferSize)
        .isEqualTo(durationUsToMaxBytes(encoding, DEFAULT.passthroughBufferDurationUs));
  }

  @Test
  public void getBufferSizeInBytes_passthroughAndBitrateDefined() {
    int bufferSize =
        DEFAULT.getBufferSizeInBytes(
            /* minBufferSizeInBytes= */ 0,
            /* encoding= */ encoding,
            /* outputMode= */ OUTPUT_MODE_PASSTHROUGH,
            /* pcmFrameSize= */ 1,
            /* sampleRate= */ 0,
            /* bitrate= */ 256_000,
            /* maxAudioTrackPlaybackSpeed= */ 1);

    // Default buffer duration is 250ms => 0.25 * 256000 / 8 = 8000
    assertThat(bufferSize).isEqualTo(8000);
  }

  private static int durationUsToMaxBytes(@C.Encoding int encoding, long durationUs) {
    return (int) (durationUs * getMaximumEncodedRateBytesPerSecond(encoding) / MICROS_PER_SECOND);
  }
}
