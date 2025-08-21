/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common.text;

import static androidx.media3.test.utils.truth.SpannedSubject.assertThat;
import static com.google.common.truth.Truth.assertThat;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Tests for {@link SpanUtil}. */
@RunWith(AndroidJUnit4.class)
public class SpanUtilTest {

  @Test
  public void addOrReplaceSpan_replacesSameTypeAndIndexes() {
    Spannable spannable = SpannableString.valueOf("test text");
    spannable.setSpan(
        new ForegroundColorSpan(Color.CYAN),
        /* start= */ 2,
        /* end= */ 5,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

    ForegroundColorSpan newSpan = new ForegroundColorSpan(Color.BLUE);
    SpanUtil.addOrReplaceSpan(
        spannable, newSpan, /* start= */ 2, /* end= */ 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

    Object[] spans = spannable.getSpans(0, spannable.length(), Object.class);
    assertThat(spans).asList().containsExactly(newSpan);
  }

  @Test
  public void addOrReplaceSpan_ignoresDifferentType() {
    Spannable spannable = SpannableString.valueOf("test text");
    ForegroundColorSpan originalSpan = new ForegroundColorSpan(Color.CYAN);
    spannable.setSpan(originalSpan, /* start= */ 2, /* end= */ 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

    BackgroundColorSpan newSpan = new BackgroundColorSpan(Color.BLUE);
    SpanUtil.addOrReplaceSpan(spannable, newSpan, 2, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

    Object[] spans = spannable.getSpans(0, spannable.length(), Object.class);
    assertThat(spans).asList().containsExactly(originalSpan, newSpan).inOrder();
  }

  @Test
  public void addOrReplaceSpan_ignoresDifferentStartEndAndFlags() {
    Spannable spannable = SpannableString.valueOf("test text");
    ForegroundColorSpan originalSpan = new ForegroundColorSpan(Color.CYAN);
    spannable.setSpan(originalSpan, /* start= */ 2, /* end= */ 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

    ForegroundColorSpan differentStart = new ForegroundColorSpan(Color.GREEN);
    SpanUtil.addOrReplaceSpan(
        spannable, differentStart, /* start= */ 3, /* end= */ 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    ForegroundColorSpan differentEnd = new ForegroundColorSpan(Color.BLUE);
    SpanUtil.addOrReplaceSpan(
        spannable, differentEnd, /* start= */ 2, /* end= */ 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    ForegroundColorSpan differentFlags = new ForegroundColorSpan(Color.GREEN);
    SpanUtil.addOrReplaceSpan(
        spannable, differentFlags, /* start= */ 2, /* end= */ 5, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

    Object[] spans = spannable.getSpans(0, spannable.length(), Object.class);
    assertThat(spans)
        .asList()
        .containsExactly(originalSpan, differentStart, differentEnd, differentFlags)
        .inOrder();
  }

  @Test
  public void addInheritedRelativeSizeSpan_noExistingSpans() {
    Spannable spannable = SpannableString.valueOf("test text");

    SpanUtil.addInheritedRelativeSizeSpan(
        spannable,
        /* size= */ 0.5f,
        /* start= */ 2,
        /* end= */ 5,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

    assertThat(spannable).hasRelativeSizeSpanBetween(2, 5).withSizeChange(0.5f);
  }

  @Test
  public void addInheritedRelativeSizeSpan_existingSpanWithSameRange_replaced() {
    Spannable spannable = SpannableString.valueOf("test text");
    spannable.setSpan(
        new RelativeSizeSpan(1.6f), /* start= */ 2, /* end= */ 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

    SpanUtil.addInheritedRelativeSizeSpan(
        spannable,
        /* size= */ 0.5f,
        /* start= */ 2,
        /* end= */ 5,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

    RelativeSizeSpan[] spans = spannable.getSpans(2, 5, RelativeSizeSpan.class);
    assertThat(spans).hasLength(1);
    assertThat(spans[0].getSizeChange()).isWithin(0.0000001f).of(0.8f);
  }

  @Test
  public void addInheritedRelativeSizeSpan_existingLongerSpan() {
    Spannable spannable = SpannableString.valueOf("test text");
    spannable.setSpan(
        new RelativeSizeSpan(1.6f), /* start= */ 1, /* end= */ 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

    SpanUtil.addInheritedRelativeSizeSpan(
        spannable,
        /* size= */ 0.5f,
        /* start= */ 2,
        /* end= */ 5,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

    RelativeSizeSpan[] spans = spannable.getSpans(2, 5, RelativeSizeSpan.class);
    assertThat(spans).hasLength(2);
    assertThat(spannable).hasRelativeSizeSpanBetween(2, 5).withSizeChange(0.8f);
  }

  @Test
  public void addInheritedRelativeSizeSpan_existingIncompleteSpans_ignored() {
    Spannable spannable = SpannableString.valueOf("test text");
    spannable.setSpan(
        new RelativeSizeSpan(2.3f), /* start= */ 1, /* end= */ 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    spannable.setSpan(
        new RelativeSizeSpan(1.6f), /* start= */ 3, /* end= */ 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    spannable.setSpan(
        new RelativeSizeSpan(2.3f), /* start= */ 3, /* end= */ 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

    SpanUtil.addInheritedRelativeSizeSpan(
        spannable,
        /* size= */ 0.5f,
        /* start= */ 2,
        /* end= */ 5,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

    RelativeSizeSpan[] spans = spannable.getSpans(2, 5, RelativeSizeSpan.class);
    assertThat(spans).hasLength(4);
    assertThat(spannable).hasRelativeSizeSpanBetween(2, 5).withSizeChange(0.5f);
  }
}
