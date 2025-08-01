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

/** A parsed program information element. */
@UnstableApi
public final class ProgramInformation {
  /** The title for the media presentation. */
  @Nullable public final String title;

  /** Information about the original source of the media presentation. */
  @Nullable public final String source;

  /** A copyright statement for the media presentation. */
  @Nullable public final String copyright;

  /** A URL that provides more information about the media presentation. */
  @Nullable public final String moreInformationURL;

  /** Declares the language code(s) for this ProgramInformation. */
  @Nullable public final String lang;

  public ProgramInformation(
      @Nullable String title,
      @Nullable String source,
      @Nullable String copyright,
      @Nullable String moreInformationURL,
      @Nullable String lang) {
    this.title = title;
    this.source = source;
    this.copyright = copyright;
    this.moreInformationURL = moreInformationURL;
    this.lang = lang;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof ProgramInformation)) {
      return false;
    }
    ProgramInformation other = (ProgramInformation) obj;
    return Objects.equals(this.title, other.title)
        && Objects.equals(this.source, other.source)
        && Objects.equals(this.copyright, other.copyright)
        && Objects.equals(this.moreInformationURL, other.moreInformationURL)
        && Objects.equals(this.lang, other.lang);
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (title != null ? title.hashCode() : 0);
    result = 31 * result + (source != null ? source.hashCode() : 0);
    result = 31 * result + (copyright != null ? copyright.hashCode() : 0);
    result = 31 * result + (moreInformationURL != null ? moreInformationURL.hashCode() : 0);
    result = 31 * result + (lang != null ? lang.hashCode() : 0);
    return result;
  }
}
