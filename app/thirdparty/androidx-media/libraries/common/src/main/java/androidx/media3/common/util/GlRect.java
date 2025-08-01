/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common.util;

import static androidx.media3.common.util.Assertions.checkArgument;

/**
 * Represents a rectangle by the coordinates of its 4 edges (left, bottom, right, top).
 *
 * <p>Note that the right and top coordinates are exclusive.
 *
 * <p>This class represents coordinates in the OpenGL coordinate convention: {@code left <= right}
 * and {@code bottom <= top}.
 */
@UnstableApi
public final class GlRect {
  public int left;
  public int bottom;
  public int right;
  public int top;

  /** Creates an instance from (0, 0) to the specified width and height. */
  public GlRect(int width, int height) {
    this(/* left= */ 0, /* bottom= */ 0, width, height);
  }

  /** Creates an instance. */
  public GlRect(int left, int bottom, int right, int top) {
    checkArgument(left <= right && bottom <= top);
    this.left = left;
    this.bottom = bottom;
    this.right = right;
    this.top = top;
  }
}
