/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.demo.transformer;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import androidx.media3.common.C;
import androidx.media3.common.OverlaySettings;
import androidx.media3.effect.StaticOverlaySettings;
import androidx.media3.effect.TextOverlay;
import androidx.media3.effect.TextureOverlay;
import java.util.Locale;

/**
 * A {@link TextureOverlay} that displays a "time elapsed" timer in the bottom left corner of the
 * frame.
 */
/* package */ final class TimerOverlay extends TextOverlay {

  private final StaticOverlaySettings overlaySettings;

  public TimerOverlay() {
    overlaySettings =
        new StaticOverlaySettings.Builder()
            // Place the timer in the bottom left corner of the screen with some padding from the
            // edges.
            .setOverlayFrameAnchor(/* x= */ -1f, /* y= */ -1f)
            .setBackgroundFrameAnchor(/* x= */ -0.7f, /* y= */ -0.95f)
            .build();
  }

  @Override
  public SpannableString getText(long presentationTimeUs) {
    SpannableString text =
        new SpannableString(
            String.format(Locale.US, "%.02f", presentationTimeUs / (float) C.MICROS_PER_SECOND));
    text.setSpan(
        new ForegroundColorSpan(Color.WHITE),
        /* start= */ 0,
        text.length(),
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    return text;
  }

  @Override
  public OverlaySettings getOverlaySettings(long presentationTimeUs) {
    return overlaySettings;
  }
}
