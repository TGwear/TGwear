/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.metadata.id3;

import androidx.annotation.Nullable;
import androidx.media3.common.util.UnstableApi;
import java.util.Arrays;
import java.util.Objects;

/** GEOB (General Encapsulated Object) ID3 frame. */
@UnstableApi
public final class GeobFrame extends Id3Frame {

  public static final String ID = "GEOB";

  public final String mimeType;
  public final String filename;
  public final String description;
  public final byte[] data;

  public GeobFrame(String mimeType, String filename, String description, byte[] data) {
    super(ID);
    this.mimeType = mimeType;
    this.filename = filename;
    this.description = description;
    this.data = data;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    GeobFrame other = (GeobFrame) obj;
    return Objects.equals(mimeType, other.mimeType)
        && Objects.equals(filename, other.filename)
        && Objects.equals(description, other.description)
        && Arrays.equals(data, other.data);
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (mimeType != null ? mimeType.hashCode() : 0);
    result = 31 * result + (filename != null ? filename.hashCode() : 0);
    result = 31 * result + (description != null ? description.hashCode() : 0);
    result = 31 * result + Arrays.hashCode(data);
    return result;
  }

  @Override
  public String toString() {
    return id
        + ": mimeType="
        + mimeType
        + ", filename="
        + filename
        + ", description="
        + description;
  }
}
