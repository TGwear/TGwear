/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.test.utils;

import android.graphics.Bitmap;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.image.ImageOutput;
import androidx.media3.test.utils.Dumper.Dumpable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** A {@link ImageOutput} that captures image availability events. */
@UnstableApi
public final class CapturingImageOutput implements Dumpable, ImageOutput {

  private final List<Dumpable> renderedBitmaps;

  private int imageCount;

  public CapturingImageOutput() {
    renderedBitmaps = new ArrayList<>();
  }

  @Override
  public void onImageAvailable(long presentationTimeUs, Bitmap bitmap) {
    imageCount++;
    int currentImageCount = imageCount;
    int[] bitmapPixels = new int[bitmap.getWidth() * bitmap.getHeight()];
    bitmap.getPixels(
        bitmapPixels,
        /* offset= */ 0,
        /* stride= */ bitmap.getWidth(),
        /* x= */ 0,
        /* y= */ 0,
        bitmap.getWidth(),
        bitmap.getHeight());
    renderedBitmaps.add(
        dumper -> {
          dumper.startBlock("image output #" + currentImageCount);
          dumper.addTime("presentationTimeUs", presentationTimeUs);
          dumper.add("bitmap hash", Arrays.hashCode(bitmapPixels));
          dumper.endBlock();
        });
  }

  @Override
  public void onDisabled() {
    // Do nothing.
  }

  @Override
  public void dump(Dumper dumper) {
    if (imageCount > 0) {
      dumper.startBlock("ImageOutput");
      dumper.add("rendered image count", imageCount);
      for (Dumpable dumpable : renderedBitmaps) {
        dumpable.dump(dumper);
      }
      dumper.endBlock();
    }
  }
}
