/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.dash.manifest;

import androidx.annotation.Nullable;
import androidx.media3.common.util.UnstableApi;
import java.util.Objects;

/** A descriptor, as defined by ISO 23009-1, 2nd edition, 5.8.2. */
@UnstableApi
public final class Descriptor {

  /** The scheme URI. */
  public final String schemeIdUri;

  /** The value, or null. */
  @Nullable public final String value;

  /** The identifier, or null. */
  @Nullable public final String id;

  /**
   * @param schemeIdUri The scheme URI.
   * @param value The value, or null.
   * @param id The identifier, or null.
   */
  public Descriptor(String schemeIdUri, @Nullable String value, @Nullable String id) {
    this.schemeIdUri = schemeIdUri;
    this.value = value;
    this.id = id;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Descriptor other = (Descriptor) obj;
    return Objects.equals(schemeIdUri, other.schemeIdUri)
        && Objects.equals(value, other.value)
        && Objects.equals(id, other.id);
  }

  @Override
  public int hashCode() {
    int result = schemeIdUri.hashCode();
    result = 31 * result + (value != null ? value.hashCode() : 0);
    result = 31 * result + (id != null ? id.hashCode() : 0);
    return result;
  }
}
