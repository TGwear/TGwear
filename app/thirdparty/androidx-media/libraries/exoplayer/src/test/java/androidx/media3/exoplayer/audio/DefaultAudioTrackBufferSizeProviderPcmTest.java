/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.audio;

import static androidx.media3.common.C.MICROS_PER_SECOND;
import static androidx.media3.exoplayer.audio.DefaultAudioSink.OUTPUT_MODE_PCM;
import static com.google.common.truth.Truth.assertThat;
import static java.lang.Math.ceil;

import androidx.media3.common.C;
import androidx.media3.common.Format;
import androidx.media3.common.util.Util;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.ParameterizedRobolectricTestRunner;

/** Tests for {@link DefaultAudioTrackBufferSizeProvider} for PCM audio. */
@RunWith(ParameterizedRobolectricTestRunner.class)
public class DefaultAudioTrackBufferSizeProviderPcmTest {

  private static final DefaultAudioTrackBufferSizeProvider DEFAULT =
      new DefaultAudioTrackBufferSizeProvider.Builder().build();

  @ParameterizedRobolectricTestRunner.Parameter(0)
  public @C.PcmEncoding int encoding;

  @ParameterizedRobolectricTestRunner.Parameter(1)
  public int channelCount;

  @ParameterizedRobolectricTestRunner.Parameter(2)
  public int sampleRate;

  @ParameterizedRobolectricTestRunner.Parameters(
      name = "{index}: encoding={0}, channelCount={1}, sampleRate={2}")
  public static List<Integer[]> data() {
    return Sets.cartesianProduct(
            ImmutableList.of(
                /* encoding */ ImmutableSet.of(
                    C.ENCODING_PCM_8BIT,
                    C.ENCODING_PCM_16BIT,
                    C.ENCODING_PCM_16BIT_BIG_ENDIAN,
                    C.ENCODING_PCM_24BIT,
                    C.ENCODING_PCM_32BIT,
                    C.ENCODING_PCM_FLOAT),
                /* channelCount */ ImmutableSet.of(1, 2, 3, 4, 6, 8),
                /* sampleRate*/ ImmutableSet.of(
                    8000, 11025, 16000, 22050, 44100, 48000, 88200, 96000)))
        .stream()
        .map(s -> s.toArray(new Integer[0]))
        .collect(Collectors.toList());
  }

  private int getPcmFrameSize() {
    return Util.getPcmFrameSize(encoding, channelCount);
  }

  private int roundUpToFrame(int buffer) {
    int pcmFrameSize = getPcmFrameSize();
    return (int) ceil((double) buffer / pcmFrameSize) * pcmFrameSize;
  }

  private int durationUsToBytes(int durationUs) {
    return (int) ((long) durationUs * getPcmFrameSize() * sampleRate / MICROS_PER_SECOND);
  }

  @Test
  public void getBufferSizeInBytes_veryBigMinBufferSize_isMinBufferSize() {
    int bufferSize =
        DEFAULT.getBufferSizeInBytes(
            /* minBufferSizeInBytes= */ 1234567890,
            /* encoding= */ encoding,
            /* outputMode= */ OUTPUT_MODE_PCM,
            /* pcmFrameSize= */ getPcmFrameSize(),
            /* sampleRate= */ sampleRate,
            /* bitrate= */ Format.NO_VALUE,
            /* maxAudioTrackPlaybackSpeed= */ 1);

    assertThat(bufferSize).isEqualTo(roundUpToFrame(1234567890));
  }

  @Test
  public void getBufferSizeInBytes_noMinBufferSize_isMinBufferDuration() {
    int bufferSize =
        DEFAULT.getBufferSizeInBytes(
            /* minBufferSizeInBytes= */ 0,
            /* encoding= */ encoding,
            /* outputMode= */ OUTPUT_MODE_PCM,
            /* pcmFrameSize= */ getPcmFrameSize(),
            /* sampleRate= */ sampleRate,
            /* bitrate= */ Format.NO_VALUE,
            /* maxAudioTrackPlaybackSpeed= */ 1);

    assertThat(bufferSize)
        .isEqualTo(roundUpToFrame(durationUsToBytes(DEFAULT.minPcmBufferDurationUs)));
  }

  @Test
  public void getBufferSizeInBytes_tooSmallMinBufferSize_isMinBufferDuration() {
    int minBufferSizeInBytes =
        durationUsToBytes(DEFAULT.minPcmBufferDurationUs / DEFAULT.pcmBufferMultiplicationFactor)
            - 1;
    int bufferSize =
        DEFAULT.getBufferSizeInBytes(
            /* minBufferSizeInBytes= */ minBufferSizeInBytes,
            /* encoding= */ encoding,
            /* outputMode= */ OUTPUT_MODE_PCM,
            /* pcmFrameSize= */ getPcmFrameSize(),
            /* sampleRate= */ sampleRate,
            /* bitrate= */ Format.NO_VALUE,
            /* maxAudioTrackPlaybackSpeed= */ 1);

    assertThat(bufferSize)
        .isEqualTo(roundUpToFrame(durationUsToBytes(DEFAULT.minPcmBufferDurationUs)));
  }

  @Test
  public void getBufferSizeInBytes_lowMinBufferSize_multipliesAudioTrackMinBuffer() {
    int minBufferSizeInBytes =
        durationUsToBytes(DEFAULT.minPcmBufferDurationUs / DEFAULT.pcmBufferMultiplicationFactor)
            + 1;
    int bufferSize =
        DEFAULT.getBufferSizeInBytes(
            /* minBufferSizeInBytes= */ minBufferSizeInBytes,
            /* encoding= */ encoding,
            /* outputMode= */ OUTPUT_MODE_PCM,
            /* pcmFrameSize= */ getPcmFrameSize(),
            /* sampleRate= */ sampleRate,
            /* bitrate= */ Format.NO_VALUE,
            /* maxAudioTrackPlaybackSpeed= */ 1);

    assertThat(bufferSize)
        .isEqualTo(roundUpToFrame(minBufferSizeInBytes * DEFAULT.pcmBufferMultiplicationFactor));
  }

  @Test
  public void getBufferSizeInBytes_highMinBufferSize_multipliesAudioTrackMinBuffer() {
    int minBufferSizeInBytes =
        durationUsToBytes(DEFAULT.maxPcmBufferDurationUs / DEFAULT.pcmBufferMultiplicationFactor)
            - 1;
    int bufferSize =
        DEFAULT.getBufferSizeInBytes(
            /* minBufferSizeInBytes= */ minBufferSizeInBytes,
            /* encoding= */ encoding,
            /* outputMode= */ OUTPUT_MODE_PCM,
            /* pcmFrameSize= */ getPcmFrameSize(),
            /* sampleRate= */ sampleRate,
            /* bitrate= */ Format.NO_VALUE,
            /* maxAudioTrackPlaybackSpeed= */ 1);

    assertThat(bufferSize)
        .isEqualTo(roundUpToFrame(minBufferSizeInBytes * DEFAULT.pcmBufferMultiplicationFactor));
  }

  @Test
  public void getBufferSizeInBytes_tooHighMinBufferSize_isMaxBufferDuration() {
    int minBufferSizeInBytes =
        durationUsToBytes(DEFAULT.maxPcmBufferDurationUs / DEFAULT.pcmBufferMultiplicationFactor)
            + 1;
    int bufferSize =
        DEFAULT.getBufferSizeInBytes(
            /* minBufferSizeInBytes= */ minBufferSizeInBytes,
            /* encoding= */ encoding,
            /* outputMode= */ OUTPUT_MODE_PCM,
            /* pcmFrameSize= */ getPcmFrameSize(),
            /* sampleRate= */ sampleRate,
            /* bitrate= */ Format.NO_VALUE,
            /* maxAudioTrackPlaybackSpeed= */ 1);

    assertThat(bufferSize)
        .isEqualTo(roundUpToFrame(durationUsToBytes(DEFAULT.maxPcmBufferDurationUs)));
  }

  @Test
  public void getBufferSizeInBytes_lowPlaybackSpeed_isScaledByPlaybackSpeed() {
    int bufferSize =
        DEFAULT.getBufferSizeInBytes(
            /* minBufferSizeInBytes= */ 0,
            /* encoding= */ encoding,
            /* outputMode= */ OUTPUT_MODE_PCM,
            /* pcmFrameSize= */ getPcmFrameSize(),
            /* sampleRate= */ sampleRate,
            /* bitrate= */ Format.NO_VALUE,
            /* maxAudioTrackPlaybackSpeed= */ 1 / 5F);

    assertThat(bufferSize)
        .isEqualTo(roundUpToFrame(durationUsToBytes(DEFAULT.minPcmBufferDurationUs) / 5));
  }

  @Test
  public void getBufferSizeInBytes_highPlaybackSpeed_isScaledByPlaybackSpeed() {
    int bufferSize =
        DEFAULT.getBufferSizeInBytes(
            /* minBufferSizeInBytes= */ 0,
            /* encoding= */ encoding,
            /* outputMode= */ OUTPUT_MODE_PCM,
            /* pcmFrameSize= */ getPcmFrameSize(),
            /* sampleRate= */ sampleRate,
            /* bitrate= */ Format.NO_VALUE,
            /* maxAudioTrackPlaybackSpeed= */ 8F);

    int expected = roundUpToFrame(durationUsToBytes(DEFAULT.minPcmBufferDurationUs) * 8);
    assertThat(bufferSize).isEqualTo(expected);
  }
}
