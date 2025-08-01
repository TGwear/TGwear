/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.effect;

import static androidx.media3.common.util.Assertions.checkArgument;

import android.content.Context;
import androidx.annotation.IntRange;
import androidx.media3.common.VideoFrameProcessingException;
import androidx.media3.common.util.UnstableApi;

/**
 * Caches the input frames.
 *
 * <p>Example usage: cache the processed frames when presenting them on screen, to accommodate for
 * the possible fluctuation in video frame processing time between frames.
 */
@UnstableApi
public final class FrameCache implements GlEffect {
  /** The capacity of the frame cache. */
  public final int capacity;

  /**
   * Creates a new instance.
   *
   * <p>The {@code capacity} should be chosen carefully. OpenGL could crash unexpectedly if the
   * device is not capable of allocating the requested buffer.
   *
   * <p>Currently up to 8 frames can be cached in one {@code FrameCache} instance.
   *
   * @param capacity The capacity of the frame cache, must be greater than zero.
   */
  public FrameCache(@IntRange(from = 1, to = 8) int capacity) {
    // TODO(b/243033952) Consider adding a global limit across many FrameCache instances.
    checkArgument(capacity > 0 && capacity < 9);
    this.capacity = capacity;
  }

  @Override
  public GlShaderProgram toGlShaderProgram(Context context, boolean useHdr)
      throws VideoFrameProcessingException {
    return new FrameCacheGlShaderProgram(context, capacity, useHdr);
  }
}
