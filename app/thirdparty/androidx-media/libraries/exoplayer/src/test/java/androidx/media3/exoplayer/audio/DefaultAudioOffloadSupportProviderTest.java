/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.audio;

import static com.google.common.truth.Truth.assertThat;

import androidx.media3.common.AudioAttributes;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

/** Unit tests for {@link DefaultAudioOffloadSupportProvider}. */
@RunWith(AndroidJUnit4.class)
public final class DefaultAudioOffloadSupportProviderTest {

  @Test
  public void
      getAudioOffloadSupport_withoutSampleRate_returnsAudioOffloadSupportDefaultUnsupported() {
    Format formatWithoutSampleRate =
        new Format.Builder().setSampleMimeType(MimeTypes.AUDIO_MPEG).build();
    DefaultAudioOffloadSupportProvider audioOffloadSupportProvider =
        new DefaultAudioOffloadSupportProvider();

    AudioOffloadSupport audioOffloadSupport =
        audioOffloadSupportProvider.getAudioOffloadSupport(
            formatWithoutSampleRate, AudioAttributes.DEFAULT);

    assertThat(audioOffloadSupport.isFormatSupported).isFalse();
  }

  @Test
  @Config(maxSdk = 29)
  public void
      getAudioOffloadSupport_withOpusAndSdkUnder30_returnsAudioOffloadSupportDefaultUnsupported() {
    Format formatOpus =
        new Format.Builder().setSampleMimeType(MimeTypes.AUDIO_OPUS).setSampleRate(48_000).build();
    DefaultAudioOffloadSupportProvider audioOffloadSupportProvider =
        new DefaultAudioOffloadSupportProvider();

    AudioOffloadSupport audioOffloadSupport =
        audioOffloadSupportProvider.getAudioOffloadSupport(formatOpus, AudioAttributes.DEFAULT);

    assertThat(audioOffloadSupport.isFormatSupported).isFalse();
  }

  @Test
  @Config(maxSdk = 33)
  public void
      getAudioOffloadSupport_withDtsXAndSdkUnder34_returnsAudioOffloadSupportDefaultUnsupported() {
    Format formatDtsX =
        new Format.Builder().setSampleMimeType(MimeTypes.AUDIO_DTS_X).setSampleRate(48_000).build();
    DefaultAudioOffloadSupportProvider audioOffloadSupportProvider =
        new DefaultAudioOffloadSupportProvider();

    AudioOffloadSupport audioOffloadSupport =
        audioOffloadSupportProvider.getAudioOffloadSupport(formatDtsX, AudioAttributes.DEFAULT);

    assertThat(audioOffloadSupport.isFormatSupported).isFalse();
  }
}
