/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common;

import android.os.Bundle;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.Nullable;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.common.util.Util;

/** Represents the video size. */
public final class VideoSize {

  private static final int DEFAULT_WIDTH = 0;
  private static final int DEFAULT_HEIGHT = 0;
  private static final float DEFAULT_PIXEL_WIDTH_HEIGHT_RATIO = 1F;

  public static final VideoSize UNKNOWN = new VideoSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

  /** The video width in pixels, 0 when unknown. */
  @IntRange(from = 0)
  public final int width;

  /** The video height in pixels, 0 when unknown. */
  @IntRange(from = 0)
  public final int height;

  /**
   * @deprecated Rotation is handled internally by the player, so this is always zero.
   */
  @IntRange(from = 0, to = 359)
  @Deprecated
  public final int unappliedRotationDegrees;

  /**
   * The width to height ratio of each pixel, 1 if unknown.
   *
   * <p>For the normal case of square pixels this will be equal to 1.0. Different values are
   * indicative of anamorphic content.
   */
  @FloatRange(from = 0, fromInclusive = false)
  public final float pixelWidthHeightRatio;

  /**
   * Creates a VideoSize without unapplied rotation or anamorphic content.
   *
   * @param width The video width in pixels.
   * @param height The video height in pixels.
   */
  @UnstableApi
  public VideoSize(@IntRange(from = 0) int width, @IntRange(from = 0) int height) {
    this(width, height, DEFAULT_PIXEL_WIDTH_HEIGHT_RATIO);
  }

  /**
   * Creates a new instance.
   *
   * @param width The video width in pixels.
   * @param height The video height in pixels.
   * @param pixelWidthHeightRatio The width to height ratio of each pixel. For the normal case of
   *     square pixels this will be equal to 1.0. Different values are indicative of anamorphic
   *     content.
   */
  @SuppressWarnings("deprecation") // Setting deprecated field
  @UnstableApi
  public VideoSize(
      @IntRange(from = 0) int width,
      @IntRange(from = 0) int height,
      @FloatRange(from = 0, fromInclusive = false) float pixelWidthHeightRatio) {
    this.width = width;
    this.height = height;
    this.unappliedRotationDegrees = 0;
    this.pixelWidthHeightRatio = pixelWidthHeightRatio;
  }

  /**
   * @deprecated Use {@link VideoSize#VideoSize(int, int, float)} instead. {@code
   *     unappliedRotationDegrees} is not needed on API 21+ and is always zero.
   */
  @Deprecated
  @UnstableApi
  public VideoSize(
      @IntRange(from = 0) int width,
      @IntRange(from = 0) int height,
      @IntRange(from = 0, to = 359) int unappliedRotationDegrees,
      @FloatRange(from = 0, fromInclusive = false) float pixelWidthHeightRatio) {
    this(width, height, pixelWidthHeightRatio);
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj instanceof VideoSize) {
      VideoSize other = (VideoSize) obj;
      return width == other.width
          && height == other.height
          && pixelWidthHeightRatio == other.pixelWidthHeightRatio;
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = 7;
    result = 31 * result + width;
    result = 31 * result + height;
    result = 31 * result + Float.floatToRawIntBits(pixelWidthHeightRatio);
    return result;
  }

  private static final String FIELD_WIDTH = Util.intToStringMaxRadix(0);
  private static final String FIELD_HEIGHT = Util.intToStringMaxRadix(1);
  // 2 reserved for deprecated 'unappliedRotationDegrees'.
  private static final String FIELD_PIXEL_WIDTH_HEIGHT_RATIO = Util.intToStringMaxRadix(3);

  @UnstableApi
  public Bundle toBundle() {
    Bundle bundle = new Bundle();
    if (width != 0) {
      bundle.putInt(FIELD_WIDTH, width);
    }
    if (height != 0) {
      bundle.putInt(FIELD_HEIGHT, height);
    }
    if (pixelWidthHeightRatio != 1f) {
      bundle.putFloat(FIELD_PIXEL_WIDTH_HEIGHT_RATIO, pixelWidthHeightRatio);
    }
    return bundle;
  }

  /** Restores a {@code VideoSize} from a {@link Bundle}. */
  @UnstableApi
  public static VideoSize fromBundle(Bundle bundle) {
    int width = bundle.getInt(FIELD_WIDTH, DEFAULT_WIDTH);
    int height = bundle.getInt(FIELD_HEIGHT, DEFAULT_HEIGHT);
    float pixelWidthHeightRatio =
        bundle.getFloat(FIELD_PIXEL_WIDTH_HEIGHT_RATIO, DEFAULT_PIXEL_WIDTH_HEIGHT_RATIO);
    return new VideoSize(width, height, pixelWidthHeightRatio);
  }
}
