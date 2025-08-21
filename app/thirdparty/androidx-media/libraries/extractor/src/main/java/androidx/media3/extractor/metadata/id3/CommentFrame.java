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

/** Comment ID3 frame. */
@UnstableApi
public final class CommentFrame extends Id3Frame {

  public static final String ID = "COMM";

  public final String language;
  public final String description;
  public final String text;

  public CommentFrame(String language, String description, String text) {
    super(ID);
    this.language = language;
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
    CommentFrame other = (CommentFrame) obj;
    return Objects.equals(description, other.description)
        && Objects.equals(language, other.language)
        && Objects.equals(text, other.text);
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (language != null ? language.hashCode() : 0);
    result = 31 * result + (description != null ? description.hashCode() : 0);
    result = 31 * result + (text != null ? text.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return id + ": language=" + language + ", description=" + description + ", text=" + text;
  }
}
