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
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Tests for {@link DefaultAudioTrackBufferSizeProvider} DTS-HD (DTS Express) audio. */
@RunWith(AndroidJUnit4.class)
public class DefaultAudioTrackBufferSizeProviderDTSHDTest {

  private static final DefaultAudioTrackBufferSizeProvider DEFAULT =
      new DefaultAudioTrackBufferSizeProvider.Builder().build();

  @Test
  public void
      getBufferSizeInBytes_passthroughDtshdAndNoBitrate_assumesMaxByteRateTimesMultiplicationFactor() {
    int bufferSize =
        DEFAULT.getBufferSizeInBytes(
            /* minBufferSizeInBytes= */ 0,
            /* encoding= */ C.ENCODING_DTS_HD,
            /* outputMode= */ OUTPUT_MODE_PASSTHROUGH,
            /* pcmFrameSize= */ 1,
            /* sampleRate= */ 0,
            /* bitrate= */ Format.NO_VALUE,
            /* maxAudioTrackPlaybackSpeed= */ 1);

    assertThat(bufferSize)
        .isEqualTo(
            durationUsToDtshdMaxBytes(DEFAULT.passthroughBufferDurationUs)
                * DEFAULT.dtshdBufferMultiplicationFactor);
  }

  @Test
  public void
      getBufferSizeInBytes_passthroughDtshdAt384Kbits_isPassthroughBufferSizeTimesMultiplicationFactor() {
    int bufferSize =
        DEFAULT.getBufferSizeInBytes(
            /* minBufferSizeInBytes= */ 0,
            /* encoding= */ C.ENCODING_DTS_HD,
            /* outputMode= */ OUTPUT_MODE_PASSTHROUGH,
            /* pcmFrameSize= */ 1,
            /* sampleRate= */ 0,
            /* bitrate= */ 384_000,
            /* maxAudioTrackPlaybackSpeed= */ 1);

    // Default buffer duration 0.25s => 0.25 * 384000 / 8 = 12000
    assertThat(bufferSize).isEqualTo(12000 * DEFAULT.dtshdBufferMultiplicationFactor);
  }

  private static int durationUsToDtshdMaxBytes(long durationUs) {
    return (int)
        (durationUs * getMaximumEncodedRateBytesPerSecond(C.ENCODING_DTS_HD) / MICROS_PER_SECOND);
  }
}
