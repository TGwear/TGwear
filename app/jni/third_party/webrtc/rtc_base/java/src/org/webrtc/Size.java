/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

/**
 * Class for representing size of an object. Very similar to android.util.Size but available on all
 * devices.
 */
public class Size {
  public int width;
  public int height;

  public Size(int width, int height) {
    this.width = width;
    this.height = height;
  }

  @Override
  public String toString() {
    return width + "x" + height;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Size)) {
      return false;
    }
    final Size otherSize = (Size) other;
    return width == otherSize.width && height == otherSize.height;
  }

  @Override
  public int hashCode() {
    // Use prime close to 2^16 to avoid collisions for normal values less than 2^16.
    return 1 + 65537 * width + height;
  }
}
