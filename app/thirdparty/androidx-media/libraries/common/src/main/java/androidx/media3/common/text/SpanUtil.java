/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common.text;

import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import androidx.media3.common.util.UnstableApi;

/**
 * Utility methods for Android <a href="https://developer.android.com/guide/topics/text/spans">span
 * styling</a>.
 */
@UnstableApi
public final class SpanUtil {

  /**
   * Adds {@code span} to {@code spannable} between {@code start} and {@code end}, removing any
   * existing spans of the same type and with the same indices and flags.
   *
   * <p>This is useful for types of spans that don't make sense to duplicate and where the
   * evaluation order might have an unexpected impact on the final text, e.g. {@link
   * ForegroundColorSpan}.
   *
   * @param spannable The {@link Spannable} to add {@code span} to.
   * @param span The span object to be added.
   * @param start The start index to add the new span at.
   * @param end The end index to add the new span at.
   * @param spanFlags The flags to pass to {@link Spannable#setSpan(Object, int, int, int)}.
   */
  public static void addOrReplaceSpan(
      Spannable spannable, Object span, int start, int end, int spanFlags) {
    Object[] existingSpans = spannable.getSpans(start, end, span.getClass());
    for (Object existingSpan : existingSpans) {
      removeIfStartEndAndFlagsMatch(spannable, existingSpan, start, end, spanFlags);
    }
    spannable.setSpan(span, start, end, spanFlags);
  }

  /**
   * Modifies the size of the text between {@code start} and {@code end} relative to any existing
   * {@link RelativeSizeSpan} instances which cover <b>at least the same range</b>.
   *
   * <p>{@link RelativeSizeSpan} instances which only cover a part of the text between {@code start}
   * and {@code end} are ignored.
   *
   * <p>A new {@link RelativeSizeSpan} instance is added between {@code start} and {@code end} with
   * its {@code sizeChange} value computed by modifying the {@code size} parameter by the {@code
   * sizeChange} of {@link RelativeSizeSpan} instances covering between {@code start} and {@code
   * end}.
   *
   * <p>{@link RelativeSizeSpan} instances with the same {@code start}, {@code end}, and {@code
   * spanFlags} are removed.
   *
   * @param spannable The {@link Spannable} to add the {@link RelativeSizeSpan} to.
   * @param size The fraction to modify the text size by.
   * @param start The start index to add the new span at.
   * @param end The end index to add the new span at.
   * @param spanFlags The flags to pass to {@link Spannable#setSpan(Object, int, int, int)}.
   */
  public static void addInheritedRelativeSizeSpan(
      Spannable spannable, float size, int start, int end, int spanFlags) {
    for (RelativeSizeSpan existingSpan : spannable.getSpans(start, end, RelativeSizeSpan.class)) {
      if (spannable.getSpanStart(existingSpan) <= start
          && spannable.getSpanEnd(existingSpan) >= end) {
        size *= existingSpan.getSizeChange();
      }
      removeIfStartEndAndFlagsMatch(spannable, existingSpan, start, end, spanFlags);
    }
    spannable.setSpan(new RelativeSizeSpan(size), start, end, spanFlags);
  }

  private static void removeIfStartEndAndFlagsMatch(
      Spannable spannable, Object span, int start, int end, int spanFlags) {
    if (spannable.getSpanStart(span) == start
        && spannable.getSpanEnd(span) == end
        && spannable.getSpanFlags(span) == spanFlags) {
      spannable.removeSpan(span);
    }
  }

  private SpanUtil() {}
}
