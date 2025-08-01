/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.audio;

import android.media.AudioFormat;
import android.media.AudioTrack;
import androidx.annotation.RequiresApi;
import androidx.media3.common.AudioAttributes;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.common.util.Util;
import com.google.errorprone.annotations.CanIgnoreReturnValue;

/**
 * The default provider for {@link AudioTrack} instances.
 *
 * <p>Subclasses of this provider can customize the {@link AudioTrack.Builder} in {@link
 * #customizeAudioTrackBuilder} if required.
 */
@UnstableApi
public class DefaultAudioTrackProvider implements DefaultAudioSink.AudioTrackProvider {

  @Override
  public final AudioTrack getAudioTrack(
      AudioSink.AudioTrackConfig audioTrackConfig,
      AudioAttributes audioAttributes,
      int audioSessionId) {
    if (Util.SDK_INT >= 23) {
      return createAudioTrackV23(audioTrackConfig, audioAttributes, audioSessionId);
    } else {
      return createAudioTrackV21(audioTrackConfig, audioAttributes, audioSessionId);
    }
  }

  @RequiresApi(23)
  private AudioTrack createAudioTrackV23(
      AudioSink.AudioTrackConfig audioTrackConfig,
      AudioAttributes audioAttributes,
      int audioSessionId) {
    AudioFormat audioFormat =
        Util.getAudioFormat(
            audioTrackConfig.sampleRate, audioTrackConfig.channelConfig, audioTrackConfig.encoding);
    android.media.AudioAttributes audioTrackAttributes =
        getAudioTrackAttributesV21(audioAttributes, audioTrackConfig.tunneling);
    AudioTrack.Builder audioTrackBuilder =
        new AudioTrack.Builder()
            .setAudioAttributes(audioTrackAttributes)
            .setAudioFormat(audioFormat)
            .setTransferMode(AudioTrack.MODE_STREAM)
            .setBufferSizeInBytes(audioTrackConfig.bufferSize)
            .setSessionId(audioSessionId);
    if (Util.SDK_INT >= 29) {
      setOffloadedPlaybackV29(audioTrackBuilder, audioTrackConfig.offload);
    }
    return customizeAudioTrackBuilder(audioTrackBuilder).build();
  }

  @RequiresApi(29)
  private void setOffloadedPlaybackV29(AudioTrack.Builder audioTrackBuilder, boolean isOffloaded) {
    audioTrackBuilder.setOffloadedPlayback(isOffloaded);
  }

  /**
   * Optionally customize {@link AudioTrack.Builder} with other parameters.
   *
   * <p>Note that this method is only called on API 23 and above.
   *
   * @param audioTrackBuilder The {@link AudioTrack.Builder} on which to set the attributes.
   * @return The same {@link AudioTrack.Builder} instance provided.
   */
  @RequiresApi(23) // AudioTrack.Builder is available starting from API 23.
  @CanIgnoreReturnValue
  protected AudioTrack.Builder customizeAudioTrackBuilder(AudioTrack.Builder audioTrackBuilder) {
    return audioTrackBuilder;
  }

  private AudioTrack createAudioTrackV21(
      AudioSink.AudioTrackConfig audioTrackConfig,
      AudioAttributes audioAttributes,
      int audioSessionId) {
    return new AudioTrack(
        getAudioTrackAttributesV21(audioAttributes, audioTrackConfig.tunneling),
        Util.getAudioFormat(
            audioTrackConfig.sampleRate, audioTrackConfig.channelConfig, audioTrackConfig.encoding),
        audioTrackConfig.bufferSize,
        AudioTrack.MODE_STREAM,
        audioSessionId);
  }

  private android.media.AudioAttributes getAudioTrackAttributesV21(
      AudioAttributes audioAttributes, boolean tunneling) {
    if (tunneling) {
      return getAudioTrackTunnelingAttributesV21();
    } else {
      return audioAttributes.getAudioAttributesV21().audioAttributes;
    }
  }

  private android.media.AudioAttributes getAudioTrackTunnelingAttributesV21() {
    return new android.media.AudioAttributes.Builder()
        .setContentType(android.media.AudioAttributes.CONTENT_TYPE_MOVIE)
        .setFlags(android.media.AudioAttributes.FLAG_HW_AV_SYNC)
        .setUsage(android.media.AudioAttributes.USAGE_MEDIA)
        .build();
  }
}
