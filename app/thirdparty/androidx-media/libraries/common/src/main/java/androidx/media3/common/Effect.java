/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package androidx.media3.common;

import androidx.media3.common.util.UnstableApi;

/** Marker interface for a video frame effect. */
@UnstableApi
public interface Effect {

  /**
   * Returns the expected duration of the output stream when the effect is applied given a input
   * {@code durationUs}.
   */
  default long getDurationAfterEffectApplied(long durationUs) {
    return durationUs;
  }
}
