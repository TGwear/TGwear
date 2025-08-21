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

/** PRIV (Private) ID3 frame. */
@UnstableApi
public final class PrivFrame extends Id3Frame {

  public static final String ID = "PRIV";

  public final String owner;
  public final byte[] privateData;

  public PrivFrame(String owner, byte[] privateData) {
    super(ID);
    this.owner = owner;
    this.privateData = privateData;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    PrivFrame other = (PrivFrame) obj;
    return Objects.equals(owner, other.owner) && Arrays.equals(privateData, other.privateData);
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (owner != null ? owner.hashCode() : 0);
    result = 31 * result + Arrays.hashCode(privateData);
    return result;
  }

  @Override
  public String toString() {
    return id + ": owner=" + owner;
  }
}
