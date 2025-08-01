/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common;

import static androidx.media3.common.util.Assertions.checkArgument;

import android.view.Surface;
import androidx.annotation.Nullable;
import androidx.media3.common.util.UnstableApi;

/** Immutable value class for a {@link Surface} and supporting information. */
@UnstableApi
public final class SurfaceInfo {

  /** The {@link Surface}. */
  public final Surface surface;

  /** The width of frames rendered to the {@link #surface}, in pixels. */
  public final int width;

  /** The height of frames rendered to the {@link #surface}, in pixels. */
  public final int height;

  /**
   * A counter-clockwise rotation to apply to frames before rendering them to the {@link #surface}.
   *
   * <p>Must be 0, 90, 180, or 270 degrees. Default is 0.
   */
  public final int orientationDegrees;

  /** Whether the {@link #surface} is an encoder input surface. */
  public final boolean isEncoderInputSurface;

  /** Creates a new instance. */
  public SurfaceInfo(Surface surface, int width, int height) {
    this(surface, width, height, /* orientationDegrees= */ 0);
  }

  /** Creates a new instance. */
  public SurfaceInfo(Surface surface, int width, int height, int orientationDegrees) {
    this(surface, width, height, orientationDegrees, /* isEncoderInputSurface= */ false);
  }

  /** Creates a new instance. */
  public SurfaceInfo(
      Surface surface,
      int width,
      int height,
      int orientationDegrees,
      boolean isEncoderInputSurface) {
    checkArgument(
        orientationDegrees == 0
            || orientationDegrees == 90
            || orientationDegrees == 180
            || orientationDegrees == 270,
        "orientationDegrees must be 0, 90, 180, or 270");
    this.surface = surface;
    this.width = width;
    this.height = height;
    this.orientationDegrees = orientationDegrees;
    this.isEncoderInputSurface = isEncoderInputSurface;
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof SurfaceInfo)) {
      return false;
    }
    SurfaceInfo that = (SurfaceInfo) o;
    return width == that.width
        && height == that.height
        && orientationDegrees == that.orientationDegrees
        && isEncoderInputSurface == that.isEncoderInputSurface
        && surface.equals(that.surface);
  }

  @Override
  public int hashCode() {
    int result = surface.hashCode();
    result = 31 * result + width;
    result = 31 * result + height;
    result = 31 * result + orientationDegrees;
    result = 31 * result + (isEncoderInputSurface ? 1 : 0);
    return result;
  }
}
