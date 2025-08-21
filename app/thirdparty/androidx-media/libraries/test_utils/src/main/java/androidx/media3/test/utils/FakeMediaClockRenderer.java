/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.test.utils;

import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.MediaClock;
import androidx.media3.exoplayer.Renderer;

/** Fake abstract {@link Renderer} which is also a {@link MediaClock}. */
@UnstableApi
public abstract class FakeMediaClockRenderer extends FakeRenderer implements MediaClock {

  public FakeMediaClockRenderer(int trackType) {
    super(trackType);
  }

  @Override
  public MediaClock getMediaClock() {
    return this;
  }
}
