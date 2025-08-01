/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.ui;

import static androidx.media3.common.util.Assertions.checkNotNull;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.RelativeSizeSpan;
import androidx.media3.common.text.Cue;
import androidx.media3.common.text.LanguageFeatureSpan;
import com.google.common.base.Predicate;

/** Utility class for subtitle layout logic. */
/* package */ final class SubtitleViewUtils {

  /**
   * Returns the text size in px, derived from {@code textSize} and {@code textSizeType}.
   *
   * <p>Returns {@link Cue#DIMEN_UNSET} if {@code textSize == Cue.DIMEN_UNSET} or {@code
   * textSizeType == Cue.TYPE_UNSET}.
   */
  public static float resolveTextSize(
      @Cue.TextSizeType int textSizeType,
      float textSize,
      int rawViewHeight,
      int viewHeightMinusPadding) {
    if (textSize == Cue.DIMEN_UNSET) {
      return Cue.DIMEN_UNSET;
    }
    switch (textSizeType) {
      case Cue.TEXT_SIZE_TYPE_ABSOLUTE:
        return textSize;
      case Cue.TEXT_SIZE_TYPE_FRACTIONAL:
        return textSize * viewHeightMinusPadding;
      case Cue.TEXT_SIZE_TYPE_FRACTIONAL_IGNORE_PADDING:
        return textSize * rawViewHeight;
      case Cue.TYPE_UNSET:
      default:
        return Cue.DIMEN_UNSET;
    }
  }

  /** Removes all styling information from {@code cue}. */
  public static void removeAllEmbeddedStyling(Cue.Builder cue) {
    cue.clearWindowColor();
    if (cue.getText() instanceof Spanned) {
      if (!(cue.getText() instanceof Spannable)) {
        cue.setText(SpannableString.valueOf(cue.getText()));
      }
      removeSpansIf(
          (Spannable) checkNotNull(cue.getText()), span -> !(span instanceof LanguageFeatureSpan));
    }
    removeEmbeddedFontSizes(cue);
  }

  /**
   * Removes all font size information from {@code cue}.
   *
   * <p>This involves:
   *
   * <ul>
   *   <li>Clearing {@link Cue.Builder#setTextSize(float, int)}.
   *   <li>Removing all {@link AbsoluteSizeSpan} and {@link RelativeSizeSpan} spans from {@link
   *       Cue#text}.
   * </ul>
   */
  public static void removeEmbeddedFontSizes(Cue.Builder cue) {
    cue.setTextSize(Cue.DIMEN_UNSET, Cue.TYPE_UNSET);
    if (cue.getText() instanceof Spanned) {
      if (!(cue.getText() instanceof Spannable)) {
        cue.setText(SpannableString.valueOf(cue.getText()));
      }
      removeSpansIf(
          (Spannable) checkNotNull(cue.getText()),
          span -> span instanceof AbsoluteSizeSpan || span instanceof RelativeSizeSpan);
    }
  }

  private static void removeSpansIf(Spannable spannable, Predicate<Object> removeFilter) {
    Object[] spans = spannable.getSpans(0, spannable.length(), Object.class);
    for (Object span : spans) {
      if (removeFilter.apply(span)) {
        spannable.removeSpan(span);
      }
    }
  }

  private SubtitleViewUtils() {}
}
