/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.effect;

import androidx.media3.common.util.Size;
import androidx.media3.common.util.UnstableApi;

/**
 * An interface for 1 dimensional convolution functions.
 *
 * <p>The domain defines the region over which the function operates, in pixels.
 */
@UnstableApi
public interface ConvolutionFunction1D {

  /** A configurable provider for {@link ConvolutionFunction1D} instances. */
  interface Provider {

    /**
     * Configures the provider to return {@linkplain ConvolutionFunction1D 1D convolution functions}
     * based on the input frame dimensions.
     *
     * <p>This method must be called before {@link #getConvolution(long)}.
     *
     * @param inputSize The input frame size on which to apply a convolution.
     * @return The output frame size after applying the convolution.
     */
    Size configure(Size inputSize);

    /**
     * Returns a {@linkplain ConvolutionFunction1D 1D convolution function}.
     *
     * @param presentationTimeUs The presentation timestamp of the input frame, in microseconds.
     */
    ConvolutionFunction1D getConvolution(long presentationTimeUs);
  }

  /** Returns the start of the domain. */
  float domainStart();

  /** Returns the end of the domain. */
  float domainEnd();

  /** Returns the width of the domain. */
  default float width() {
    return domainEnd() - domainStart();
  }

  /** Returns the value of the function at the {@code samplePosition}. */
  float value(float samplePosition);
}
