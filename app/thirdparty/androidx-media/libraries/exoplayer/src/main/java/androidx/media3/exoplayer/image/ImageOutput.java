/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.image;

import android.graphics.Bitmap;
import androidx.media3.common.Timeline;
import androidx.media3.common.util.UnstableApi;

/** A listener for image output. */
@UnstableApi
public interface ImageOutput {

  /** A no-op implementation of ImageOutput. */
  ImageOutput NO_OP =
      new ImageOutput() {
        @Override
        public void onImageAvailable(long presentationTimeUs, Bitmap bitmap) {
          // Do nothing.
        }

        @Override
        public void onDisabled() {
          // Do nothing.
        }
      };

  /**
   * Called on the playback thread when a new image is available.
   *
   * <p>This method should have an implementation that runs fast.
   *
   * @param presentationTimeUs The presentation time of the image, in microseconds. This time is an
   *     offset from the start of the current {@link Timeline.Period}.
   * @param bitmap The new image available.
   */
  void onImageAvailable(long presentationTimeUs, Bitmap bitmap);

  /**
   * Called on the playback thread when the renderer is disabled.
   *
   * <p>This method should have an implementation that runs fast.
   */
  void onDisabled();
}
