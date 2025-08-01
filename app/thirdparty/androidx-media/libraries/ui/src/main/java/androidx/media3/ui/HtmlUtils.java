/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.ui;

import android.graphics.Color;
import androidx.annotation.ColorInt;
import androidx.media3.common.util.Util;

/**
 * Utility methods for generating HTML and CSS for use with {@link WebViewSubtitleOutput} and {@link
 * SpannedToHtmlConverter}.
 */
/* package */ final class HtmlUtils {

  private HtmlUtils() {}

  public static String toCssRgba(@ColorInt int color) {
    return Util.formatInvariant(
        "rgba(%d,%d,%d,%.3f)",
        Color.red(color), Color.green(color), Color.blue(color), Color.alpha(color) / 255.0);
  }

  /**
   * Returns a CSS selector that selects all elements with {@code class=className} and all their
   * descendants.
   */
  public static String cssAllClassDescendantsSelector(String className) {
    return "." + className + ",." + className + " *";
  }
}
