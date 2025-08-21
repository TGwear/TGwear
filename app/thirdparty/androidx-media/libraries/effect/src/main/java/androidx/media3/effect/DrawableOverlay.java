/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.effect;

import static androidx.media3.common.util.Assertions.checkNotNull;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import androidx.media3.common.OverlaySettings;
import androidx.media3.common.util.UnstableApi;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

/**
 * Creates a {@link TextureOverlay} from {@link Drawable}.
 *
 * <p>Uses a canvas to draw {@link DrawableOverlay} onto {@link BitmapOverlay}, which is then
 * displayed on each frame.
 */
@UnstableApi
public abstract class DrawableOverlay extends BitmapOverlay {
  private @MonotonicNonNull Bitmap lastBitmap;
  private @MonotonicNonNull Drawable lastDrawable;

  /**
   * Returns the overlay {@link Drawable} displayed at the specified timestamp.
   *
   * <p>The drawable must have its bounds set via {@link Drawable#setBounds} for drawable to be
   * displayed on the frame.
   *
   * @param presentationTimeUs The presentation timestamp of the current frame, in microseconds.
   */
  public abstract Drawable getDrawable(long presentationTimeUs);

  @Override
  public Bitmap getBitmap(long presentationTimeUs) {
    Drawable overlayDrawable = getDrawable(presentationTimeUs);
    // TODO(b/227625365): Drawable doesn't implement the equals method, so investigate other methods
    //   of detecting the need to redraw the bitmap.
    if (!overlayDrawable.equals(lastDrawable)) {
      lastDrawable = overlayDrawable;
      if (lastBitmap == null
          || lastBitmap.getWidth() != lastDrawable.getIntrinsicWidth()
          || lastBitmap.getHeight() != lastDrawable.getIntrinsicHeight()) {
        lastBitmap =
            Bitmap.createBitmap(
                lastDrawable.getIntrinsicWidth(),
                lastDrawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
      }
      Canvas canvas = new Canvas(lastBitmap);
      canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
      lastDrawable.draw(canvas);
    }
    return checkNotNull(lastBitmap);
  }

  /**
   * Creates a {@link DrawableOverlay} that shows the {@link Drawable} with the same {@link
   * StaticOverlaySettings} throughout the whole video.
   *
   * @param drawable The {@link Drawable} to be displayed.
   * @param overlaySettings The {@link StaticOverlaySettings} configuring how the overlay is
   *     displayed on the frames.
   */
  public static DrawableOverlay createStaticDrawableOverlay(
      Drawable drawable, StaticOverlaySettings overlaySettings) {
    return new DrawableOverlay() {
      @Override
      public Drawable getDrawable(long presentationTimeUs) {
        return drawable;
      }

      @Override
      public OverlaySettings getOverlaySettings(long presentationTimeUs) {
        return overlaySettings;
      }
    };
  }
}
