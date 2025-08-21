/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common;

import static androidx.media3.common.util.Assertions.checkNotNull;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.common.util.Util;
import java.util.Objects;

/** A label for a {@link Format}. */
@UnstableApi
public class Label {
  /**
   * The language of this label, as an IETF BCP 47 conformant tag, or null if unknown or not
   * applicable.
   */
  @Nullable public final String language;

  /** The value for this label. */
  public final String value;

  /**
   * Creates a label.
   *
   * @param language The language of this label, as an IETF BCP 47 conformant tag, or null if
   *     unknown or not applicable.
   * @param value The label value.
   */
  public Label(@Nullable String language, String value) {
    this.language = Util.normalizeLanguageCode(language);
    this.value = value;
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Label label = (Label) o;
    return Objects.equals(language, label.language) && Objects.equals(value, label.value);
  }

  @Override
  public int hashCode() {
    int result = value.hashCode();
    result = 31 * result + (language != null ? language.hashCode() : 0);
    return result;
  }

  private static final String FIELD_LANGUAGE_INDEX = Util.intToStringMaxRadix(0);
  private static final String FIELD_VALUE_INDEX = Util.intToStringMaxRadix(1);

  /** Serializes this instance to a {@link Bundle}. */
  public Bundle toBundle() {
    Bundle bundle = new Bundle();
    if (language != null) {
      bundle.putString(FIELD_LANGUAGE_INDEX, language);
    }
    bundle.putString(FIELD_VALUE_INDEX, value);
    return bundle;
  }

  /** Deserializes an instance from a {@link Bundle} produced by {@link #toBundle()}. */
  public static Label fromBundle(Bundle bundle) {
    return new Label(
        bundle.getString(FIELD_LANGUAGE_INDEX), checkNotNull(bundle.getString(FIELD_VALUE_INDEX)));
  }
}
