/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.metadata.id3;

import androidx.annotation.Nullable;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.util.UnstableApi;
import java.util.Arrays;
import java.util.Objects;

/** APIC (Attached Picture) ID3 frame. */
@UnstableApi
public final class ApicFrame extends Id3Frame {

  public static final String ID = "APIC";

  public final String mimeType;
  @Nullable public final String description;
  public final int pictureType;
  public final byte[] pictureData;

  public ApicFrame(
      String mimeType, @Nullable String description, int pictureType, byte[] pictureData) {
    super(ID);
    this.mimeType = mimeType;
    this.description = description;
    this.pictureType = pictureType;
    this.pictureData = pictureData;
  }

  @Override
  public void populateMediaMetadata(MediaMetadata.Builder builder) {
    builder.maybeSetArtworkData(pictureData, pictureType);
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    ApicFrame other = (ApicFrame) obj;
    return pictureType == other.pictureType
        && Objects.equals(mimeType, other.mimeType)
        && Objects.equals(description, other.description)
        && Arrays.equals(pictureData, other.pictureData);
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + pictureType;
    result = 31 * result + (mimeType != null ? mimeType.hashCode() : 0);
    result = 31 * result + (description != null ? description.hashCode() : 0);
    result = 31 * result + Arrays.hashCode(pictureData);
    return result;
  }

  @Override
  public String toString() {
    return id + ": mimeType=" + mimeType + ", description=" + description;
  }
}
