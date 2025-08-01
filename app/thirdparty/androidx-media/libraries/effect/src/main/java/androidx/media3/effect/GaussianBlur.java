/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.effect;

import androidx.annotation.FloatRange;
import androidx.media3.common.util.UnstableApi;

/**
 * A {@link SeparableConvolution} to apply a Gaussian blur on image data.
 *
 * <p>The width of the blur is specified in pixels and applied symmetrically.
 */
@UnstableApi
public final class GaussianBlur extends SeparableConvolution {
  private final float sigma;
  private final float numStandardDeviations;

  /**
   * Creates an instance.
   *
   * @param sigma The half-width of 1 standard deviation, in pixels.
   * @param numStandardDeviations The size of function domain, measured in the number of standard
   *     deviations.
   */
  public GaussianBlur(
      @FloatRange(from = 0.0, fromInclusive = false) float sigma,
      @FloatRange(from = 0.0, fromInclusive = false) float numStandardDeviations) {
    this.sigma = sigma;
    this.numStandardDeviations = numStandardDeviations;
  }

  /**
   * Creates an instance with {@code numStandardDeviations} set to {@code 2.0f}.
   *
   * @param sigma The half-width of 1 standard deviation, in pixels.
   */
  public GaussianBlur(float sigma) {
    this(sigma, /* numStandardDeviations= */ 2.0f);
  }

  @Override
  public ConvolutionFunction1D getConvolution(long presentationTimeUs) {
    return new GaussianFunction(sigma, numStandardDeviations);
  }
}
