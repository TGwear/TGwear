/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.test.utils;

import androidx.annotation.Nullable;
import androidx.media3.common.Metadata;
import androidx.media3.common.util.UnstableApi;

/** A fake {@link Metadata.Entry}. */
@UnstableApi
public final class FakeMetadataEntry implements Metadata.Entry {

  public final String data;

  public FakeMetadataEntry(String data) {
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
    FakeMetadataEntry other = (FakeMetadataEntry) obj;
    return data.equals(other.data);
  }

  @Override
  public int hashCode() {
    return data.hashCode();
  }
}
