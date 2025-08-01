/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.runner.RunWith;

/** Unit test for {@link ExoPlayer} with playlist preloading enabled. */
// TODO(issuetracker.google.com/316040980): Replace this class with a parameterized ExoPlayerTest
//     when resolved.
@RunWith(AndroidJUnit4.class)
public class ExoPlayerWithPreloadingEnabledTest extends ExoPlayerTest {

  @Override
  protected long getTargetPreloadDurationUs() {
    return 5_000_000L;
  }
}
