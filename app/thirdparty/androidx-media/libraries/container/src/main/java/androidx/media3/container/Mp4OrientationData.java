/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.container;

import static androidx.media3.common.util.Assertions.checkArgument;

import androidx.annotation.Nullable;
import androidx.media3.common.Metadata;
import androidx.media3.common.util.UnstableApi;

/**
 * Stores the orientation hint for the video playback.
 *
 * <p>The orientation hint is typically read/written in the "tkhd" box (track header box, defined in
 * ISO/IEC 14496-12).
 */
@UnstableApi
public final class Mp4OrientationData implements Metadata.Entry {

  /** The orientation, in degrees. */
  public final int orientation;

  /**
   * Creates an instance.
   *
   * @param orientation The orientation, in degrees. The supported values are 0, 90, 180 and 270
   *     (degrees).
   */
  public Mp4OrientationData(int orientation) {
    checkArgument(
        orientation == 0 || orientation == 90 || orientation == 180 || orientation == 270,
        "Unsupported orientation");
    this.orientation = orientation;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Mp4OrientationData)) {
      return false;
    }
    Mp4OrientationData other = (Mp4OrientationData) obj;
    return orientation == other.orientation;
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + Integer.hashCode(orientation);
    return result;
  }

  @Override
  public String toString() {
    return "Orientation= " + orientation;
  }
}
