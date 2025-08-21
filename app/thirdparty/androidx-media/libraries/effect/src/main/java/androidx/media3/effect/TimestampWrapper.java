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
 * Applies a {@link GlEffect} from {@code startTimeUs} to {@code endTimeUs}, and no change on all
 * other timestamps.
 */
@UnstableApi
public final class TimestampWrapper implements GlEffect {

  public final GlEffect glEffect;
  public final long startTimeUs;
  public final long endTimeUs;

  /**
   * Creates a new instance.
   *
   * @param glEffect The {@link GlEffect} to apply, from {@code startTimeUs} to {@code endTimeUs}.
   *     This instance must not change the output dimensions.
   * @param startTimeUs The time to begin applying {@code glEffect} on, in microseconds. Must be
   *     non-negative.
   * @param endTimeUs The time to stop applying {code glEffect} on, in microseconds. Must be
   *     non-negative.
   */
  public TimestampWrapper(
      GlEffect glEffect, @IntRange(from = 0) long startTimeUs, @IntRange(from = 0) long endTimeUs) {
    // TODO(b/272063508): Allow TimestampWrapper to take in a glEffect that changes the output
    //  dimensions, likely by moving the configure() method from BaseGlShaderProgram to
    //  GlShaderProgram, so that we can detect the output dimensions of the
    //  glEffect.toGlShaderProgram.
    checkArgument(
        startTimeUs >= 0 && endTimeUs >= 0, "startTimeUs and endTimeUs must be non-negative.");
    checkArgument(endTimeUs > startTimeUs, "endTimeUs should be after startTimeUs.");
    this.glEffect = glEffect;
    this.startTimeUs = startTimeUs;
    this.endTimeUs = endTimeUs;
  }

  @Override
  public GlShaderProgram toGlShaderProgram(Context context, boolean useHdr)
      throws VideoFrameProcessingException {
    return new TimestampWrapperShaderProgram(context, useHdr, /* timestampWrapper= */ this);
  }

  @Override
  public boolean isNoOp(int inputWidth, int inputHeight) {
    return glEffect.isNoOp(inputWidth, inputHeight);
  }
}
