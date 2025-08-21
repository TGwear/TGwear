/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common;

import static com.google.common.truth.Truth.assertThat;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit tests for {@link AudioAttributes}. */
@RunWith(AndroidJUnit4.class)
public class AudioAttributesTest {

  @Test
  public void roundTripViaBundle_yieldsEqualInstance() {
    AudioAttributes audioAttributes =
        new AudioAttributes.Builder()
            .setContentType(C.AUDIO_CONTENT_TYPE_SONIFICATION)
            .setFlags(C.FLAG_AUDIBILITY_ENFORCED)
            .setUsage(C.USAGE_ALARM)
            .setAllowedCapturePolicy(C.ALLOW_CAPTURE_BY_SYSTEM)
            .setSpatializationBehavior(C.SPATIALIZATION_BEHAVIOR_NEVER)
            .build();

    assertThat(AudioAttributes.fromBundle(audioAttributes.toBundle())).isEqualTo(audioAttributes);
  }
}
