/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.audio;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit tests for {@link AudioOffloadSupport}. */
@RunWith(AndroidJUnit4.class)
public final class AudioOffloadSupportTest {

  @Test
  public void checkDefaultUnsupported_allFieldsAreFalse() {
    AudioOffloadSupport audioOffloadSupport = AudioOffloadSupport.DEFAULT_UNSUPPORTED;

    assertThat(audioOffloadSupport.isFormatSupported).isFalse();
    assertThat(audioOffloadSupport.isGaplessSupported).isFalse();
    assertThat(audioOffloadSupport.isSpeedChangeSupported).isFalse();
  }

  @Test
  public void hashCode_withAllFlagsTrue_reportedExpectedValue() {
    AudioOffloadSupport audioOffloadSupport =
        new AudioOffloadSupport.Builder()
            .setIsFormatSupported(true)
            .setIsGaplessSupported(true)
            .setIsSpeedChangeSupported(true)
            .build();

    assertThat(audioOffloadSupport.hashCode()).isEqualTo(7);
  }

  @Test
  public void build_withoutFormatSupportedWithGaplessSupported_throwsIllegalStateException() {
    AudioOffloadSupport.Builder audioOffloadSupport =
        new AudioOffloadSupport.Builder()
            .setIsFormatSupported(false)
            .setIsGaplessSupported(true)
            .setIsSpeedChangeSupported(false);

    assertThrows(IllegalStateException.class, audioOffloadSupport::build);
  }

  @Test
  public void buildUpon_individualSetters_equalsToOriginal() {
    AudioOffloadSupport audioOffloadSupport =
        new AudioOffloadSupport.Builder()
            .setIsFormatSupported(true)
            .setIsGaplessSupported(true)
            .setIsSpeedChangeSupported(false)
            .build();

    AudioOffloadSupport copy = audioOffloadSupport.buildUpon().build();

    assertThat(copy).isEqualTo(audioOffloadSupport);
  }
}
