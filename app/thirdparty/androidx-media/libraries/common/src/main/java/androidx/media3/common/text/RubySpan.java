/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common.text;

import static androidx.media3.common.util.Assertions.checkNotNull;

import android.os.Bundle;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.common.util.Util;

/**
 * A styling span for ruby text.
 *
 * <p>The text covered by this span is known as the "base text", and the ruby text is stored in
 * {@link #rubyText}.
 *
 * <p>More information on <a href="https://en.wikipedia.org/wiki/Ruby_character">ruby characters</a>
 * and <a href="https://developer.android.com/guide/topics/text/spans">span styling</a>.
 */
// NOTE: There's no Android layout support for rubies, so this span currently doesn't extend any
// styling superclasses (e.g. MetricAffectingSpan). The only way to render these rubies is to
// extract the spans and do the layout manually.
// TODO: Consider adding support for parenthetical text to be used when rendering doesn't support
// rubies (e.g. HTML <rp> tag).
@UnstableApi
public final class RubySpan implements LanguageFeatureSpan {

  /** The ruby text, i.e. the smaller explanatory characters. */
  public final String rubyText;

  /** The position of the ruby text relative to the base text. */
  public final @TextAnnotation.Position int position;

  private static final String FIELD_TEXT = Util.intToStringMaxRadix(0);
  private static final String FIELD_POSITION = Util.intToStringMaxRadix(1);

  public RubySpan(String rubyText, @TextAnnotation.Position int position) {
    this.rubyText = rubyText;
    this.position = position;
  }

  public Bundle toBundle() {
    Bundle bundle = new Bundle();
    bundle.putString(FIELD_TEXT, rubyText);
    bundle.putInt(FIELD_POSITION, position);
    return bundle;
  }

  public static RubySpan fromBundle(Bundle bundle) {
    return new RubySpan(
        /* rubyText= */ checkNotNull(bundle.getString(FIELD_TEXT)),
        /* position= */ bundle.getInt(FIELD_POSITION));
  }
}
