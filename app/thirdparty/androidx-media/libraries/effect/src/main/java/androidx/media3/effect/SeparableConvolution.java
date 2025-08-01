/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.effect;

import android.content.Context;
import androidx.media3.common.VideoFrameProcessingException;
import androidx.media3.common.util.UnstableApi;

/**
 * A {@link GlEffect} for performing separable convolutions.
 *
 * <p>A single 1D convolution function is applied horizontally on a first pass and vertically on a
 * second pass.
 */
@UnstableApi
public abstract class SeparableConvolution implements GlEffect {
  private final float scaleWidth;
  private final float scaleHeight;

  /** Creates an instance with {@code scaleWidth} and {@code scaleHeight} set to {@code 1.0f}. */
  public SeparableConvolution() {
    this(/* scaleWidth= */ 1.0f, /* scaleHeight= */ 1.0f);
  }

  /**
   * Creates an instance.
   *
   * @param scaleWidth The scaling factor used to determine the width of the output relative to the
   *     input.
   * @param scaleHeight The scaling factor used to determine the height of the output relative to
   *     the input.
   */
  public SeparableConvolution(float scaleWidth, float scaleHeight) {
    this.scaleWidth = scaleWidth;
    this.scaleHeight = scaleHeight;
  }

  /**
   * Returns a {@linkplain ConvolutionFunction1D 1D convolution function}.
   *
   * @param presentationTimeUs The presentation timestamp of the input frame, in microseconds.
   */
  public abstract ConvolutionFunction1D getConvolution(long presentationTimeUs);

  @Override
  public GlShaderProgram toGlShaderProgram(Context context, boolean useHdr)
      throws VideoFrameProcessingException {
    return new SeparableConvolutionShaderProgram(
        context, useHdr, /* convolution= */ this, scaleWidth, scaleHeight);
  }
}
