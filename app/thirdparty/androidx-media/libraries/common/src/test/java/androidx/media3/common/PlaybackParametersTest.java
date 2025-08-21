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

/** Unit tests for {@link PlaybackParameters}. */
@RunWith(AndroidJUnit4.class)
public class PlaybackParametersTest {

  @Test
  public void roundTripViaBundle_ofPlaybackParameters_yieldsEqualInstance() {
    PlaybackParameters playbackParameters =
        new PlaybackParameters(/* speed= */ 2.9f, /* pitch= */ 1.2f);

    assertThat(PlaybackParameters.fromBundle(playbackParameters.toBundle()))
        .isEqualTo(playbackParameters);
  }
}
