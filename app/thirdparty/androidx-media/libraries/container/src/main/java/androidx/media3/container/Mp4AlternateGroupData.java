/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.container;

import androidx.annotation.Nullable;
import androidx.media3.common.Metadata;
import androidx.media3.common.util.UnstableApi;

/** Stores MP4 {@code alternate_group} info parsed from a {@code tkhd} box. */
@UnstableApi
public final class Mp4AlternateGroupData implements Metadata.Entry {

  public final int alternateGroup;

  public Mp4AlternateGroupData(int alternateGroup) {
    this.alternateGroup = alternateGroup;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Mp4AlternateGroupData)) {
      return false;
    }

    Mp4AlternateGroupData other = (Mp4AlternateGroupData) obj;
    return alternateGroup == other.alternateGroup;
  }

  @Override
  public int hashCode() {
    return alternateGroup;
  }

  @Override
  public String toString() {
    return "Mp4AlternateGroup: " + alternateGroup;
  }
}
