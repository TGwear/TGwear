/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package androidx.media3.effect;

import static androidx.media3.common.util.Assertions.checkArgument;

import androidx.annotation.FloatRange;
import androidx.media3.common.util.UnstableApi;

/** A {@link RgbMatrix} to control the contrast of video frames. */
@UnstableApi
public final class Contrast implements RgbMatrix {

  /** Adjusts the contrast of video frames in the interval [-1, 1]. */
  private final float contrast;

  private final float[] contrastMatrix;

  /**
   * Creates a new instance for the given contrast value.
   *
   * <p>Contrast values range from -1 (all gray pixels) to 1 (maximum difference of colors). 0 means
   * to add no contrast and leaves the frames unchanged.
   */
  public Contrast(@FloatRange(from = -1, to = 1) float contrast) {
    checkArgument(-1 <= contrast && contrast <= 1, "Contrast needs to be in the interval [-1, 1].");
    this.contrast = contrast;
    float contrastFactor = (1 + contrast) / (1.0001f - contrast);
    contrastMatrix =
        new float[] {
          contrastFactor,
          0.0f,
          0.0f,
          0.0f,
          0.0f,
          contrastFactor,
          0.0f,
          0.0f,
          0.0f,
          0.0f,
          contrastFactor,
          0.0f,
          (1.0f - contrastFactor) * 0.5f,
          (1.0f - contrastFactor) * 0.5f,
          (1.0f - contrastFactor) * 0.5f,
          1.0f
        };
  }

  @Override
  public float[] getMatrix(long presentationTimeUs, boolean useHdr) {
    // Implementation is not currently time-varying, therefore matrix should not be changing between
    // frames.
    return contrastMatrix;
  }

  @Override
  public boolean isNoOp(int inputWidth, int inputHeight) {
    return contrast == 0f;
  }
}
