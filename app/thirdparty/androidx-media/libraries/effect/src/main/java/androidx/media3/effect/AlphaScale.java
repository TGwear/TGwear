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
import androidx.annotation.FloatRange;
import androidx.media3.common.VideoFrameProcessingException;
import androidx.media3.common.util.UnstableApi;

/** Scales the alpha value (i.e. the translucency) of a frame. */
@UnstableApi
public final class AlphaScale implements GlEffect {
  private final float alphaScale;

  /**
   * Creates a new instance to scale the entire frame's alpha values by {@code alphaScale}, to
   * modify translucency.
   *
   * <p>An {@code alphaScale} value of {@code 1} means no change is applied. A value below {@code 1}
   * increases translucency, and a value above {@code 1} reduces translucency.
   */
  public AlphaScale(@FloatRange(from = 0) float alphaScale) {
    checkArgument(0 <= alphaScale);
    this.alphaScale = alphaScale;
  }

  @Override
  public AlphaScaleShaderProgram toGlShaderProgram(Context context, boolean useHdr)
      throws VideoFrameProcessingException {
    return new AlphaScaleShaderProgram(context, useHdr, alphaScale);
  }

  @Override
  public boolean isNoOp(int inputWidth, int inputHeight) {
    return alphaScale == 1f;
  }
}
