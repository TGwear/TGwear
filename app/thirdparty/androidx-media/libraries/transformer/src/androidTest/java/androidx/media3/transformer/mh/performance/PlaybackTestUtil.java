/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package androidx.media3.transformer.mh.performance;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import androidx.media3.common.OverlaySettings;
import androidx.media3.effect.OverlayEffect;
import androidx.media3.effect.StaticOverlaySettings;
import androidx.media3.effect.TextOverlay;
import com.google.common.collect.ImmutableList;

/** Utilities for playback tests. */
/* package */ final class PlaybackTestUtil {

  private static final int DEFAULT_TEXT_SIZE = 300;

  private PlaybackTestUtil() {}

  /** Creates an {@link OverlayEffect} that draws the timestamp onto frames. */
  public static OverlayEffect createTimestampOverlay() {
    return createTimestampOverlay(DEFAULT_TEXT_SIZE);
  }

  /**
   * Creates an {@link OverlayEffect} that draws the timestamp onto frames with a specified text
   * size.
   */
  public static OverlayEffect createTimestampOverlay(int textSize) {
    return new OverlayEffect(
        ImmutableList.of(
            new TimestampTextOverlay(0, -0.7f, textSize),
            new TimestampTextOverlay(0, 0, textSize),
            new TimestampTextOverlay(0, 0.7f, textSize)));
  }

  private static class TimestampTextOverlay extends TextOverlay {

    private final float x;
    private final float y;
    private final int size;

    public TimestampTextOverlay(float x, float y, int size) {
      this.x = x;
      this.y = y;
      this.size = size;
    }

    @Override
    public SpannableString getText(long presentationTimeUs) {
      SpannableString text = new SpannableString(String.valueOf(presentationTimeUs));
      text.setSpan(
          new ForegroundColorSpan(Color.WHITE),
          /* start= */ 0,
          text.length(),
          Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      text.setSpan(
          new AbsoluteSizeSpan(size),
          /* start= */ 0,
          text.length(),
          Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      text.setSpan(
          new TypefaceSpan(/* family= */ "sans-serif"),
          /* start= */ 0,
          text.length(),
          Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      return text;
    }

    @Override
    public OverlaySettings getOverlaySettings(long presentationTimeUs) {
      return new StaticOverlaySettings.Builder().setBackgroundFrameAnchor(x, y).build();
    }
  }
}
