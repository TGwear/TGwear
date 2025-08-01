/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package androidx.media3.common.util;

import static androidx.media3.common.util.Assertions.checkArgument;

import androidx.annotation.Nullable;
import androidx.media3.common.C;

/** Immutable class for describing width and height dimensions in pixels. */
@UnstableApi
public final class Size {

  /** A static instance to represent an unknown size value. */
  public static final Size UNKNOWN =
      new Size(/* width= */ C.LENGTH_UNSET, /* height= */ C.LENGTH_UNSET);

  /* A static instance to represent a size of zero height and width. */
  public static final Size ZERO = new Size(/* width= */ 0, /* height= */ 0);

  private final int width;
  private final int height;

  /**
   * Creates a new immutable Size instance.
   *
   * @param width The width of the size, in pixels, or {@link C#LENGTH_UNSET} if unknown.
   * @param height The height of the size, in pixels, or {@link C#LENGTH_UNSET} if unknown.
   * @throws IllegalArgumentException if an invalid {@code width} or {@code height} is specified.
   */
  public Size(int width, int height) {
    checkArgument(
        (width == C.LENGTH_UNSET || width >= 0) && (height == C.LENGTH_UNSET || height >= 0));

    this.width = width;
    this.height = height;
  }

  /** Returns the width of the size (in pixels), or {@link C#LENGTH_UNSET} if unknown. */
  public int getWidth() {
    return width;
  }

  /** Returns the height of the size (in pixels), or {@link C#LENGTH_UNSET} if unknown. */
  public int getHeight() {
    return height;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (obj == null) {
      return false;
    }
    if (this == obj) {
      return true;
    }
    if (obj instanceof Size) {
      Size other = (Size) obj;
      return width == other.width && height == other.height;
    }
    return false;
  }

  @Override
  public String toString() {
    return width + "x" + height;
  }

  @Override
  public int hashCode() {
    // assuming most sizes are <2^16, doing a rotate will give us perfect hashing
    return height ^ ((width << (Integer.SIZE / 2)) | (width >>> (Integer.SIZE / 2)));
  }
}
