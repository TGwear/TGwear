/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.effect;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.sin;

import androidx.annotation.Nullable;
import java.util.Objects;

/**
 * Implementation of a scaled Lanczos window function.
 *
 * <p>The function input is multiplied by {@code scale} before applying the textbook Lanczos window
 * function.
 */
/* package */ final class ScaledLanczosFunction implements ConvolutionFunction1D {
  private final float radius;
  private final float scale;

  /**
   * Creates an instance.
   *
   * @param radius The radius parameter of the Lanczos window function.
   * @param scale The scaling factor applied to inputs.
   */
  public ScaledLanczosFunction(float radius, float scale) {
    this.radius = radius;
    this.scale = scale;
  }

  @Override
  public float domainStart() {
    return -radius / scale;
  }

  @Override
  public float domainEnd() {
    return radius / scale;
  }

  @Override
  public float value(float samplePosition) {
    float x = samplePosition * scale;
    if (abs(x) < 1e-5) {
      return 1.0f;
    }
    if (abs(x) > radius) {
      return 0.0f;
    }
    return (float) (radius * sin(PI * x) * sin(PI * x / radius) / (PI * PI * x * x));
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ScaledLanczosFunction)) {
      return false;
    }
    ScaledLanczosFunction that = (ScaledLanczosFunction) o;
    return Float.compare(that.radius, radius) == 0 && Float.compare(that.scale, scale) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(radius, scale);
  }
}
