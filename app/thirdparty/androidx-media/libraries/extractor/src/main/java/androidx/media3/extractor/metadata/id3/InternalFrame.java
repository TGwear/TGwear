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
import java.util.Objects;

/** Internal ID3 frame that is intended for use by the player. */
@UnstableApi
public final class InternalFrame extends Id3Frame {

  public static final String ID = "----";

  public final String domain;
  public final String description;
  public final String text;

  public InternalFrame(String domain, String description, String text) {
    super(ID);
    this.domain = domain;
    this.description = description;
    this.text = text;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    InternalFrame other = (InternalFrame) obj;
    return Objects.equals(description, other.description)
        && Objects.equals(domain, other.domain)
        && Objects.equals(text, other.text);
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (domain != null ? domain.hashCode() : 0);
    result = 31 * result + (description != null ? description.hashCode() : 0);
    result = 31 * result + (text != null ? text.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return id + ": domain=" + domain + ", description=" + description;
  }
}
