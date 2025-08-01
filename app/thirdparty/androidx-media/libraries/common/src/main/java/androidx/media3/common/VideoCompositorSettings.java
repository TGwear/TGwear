/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common;

import androidx.media3.common.util.Size;
import androidx.media3.common.util.UnstableApi;
import java.util.List;

/** Settings for the {@code VideoCompositor}. */
@UnstableApi
public interface VideoCompositorSettings {
  // TODO: b/262694346 - Consider adding more features, like selecting a:
  //  * custom order for drawing (instead of primary stream on top), and
  //  * different primary source.

  VideoCompositorSettings DEFAULT =
      new VideoCompositorSettings() {
        /**
         * {@inheritDoc}
         *
         * <p>Returns the primary stream's {@link Size}.
         */
        @Override
        public Size getOutputSize(List<Size> inputSizes) {
          return inputSizes.get(0);
        }

        /**
         * {@inheritDoc}
         *
         * <p>Returns a default {@link OverlaySettings} instance.
         */
        @Override
        public OverlaySettings getOverlaySettings(int inputId, long presentationTimeUs) {
          return new OverlaySettings() {};
        }
      };

  /**
   * Returns an output texture {@link Size}, based on {@code inputSizes}.
   *
   * @param inputSizes The {@link Size} of each input frame, ordered by {@code inputId}.
   */
  Size getOutputSize(List<Size> inputSizes);

  /** Returns {@link OverlaySettings} for {@code inputId} at time {@code presentationTimeUs}. */
  OverlaySettings getOverlaySettings(int inputId, long presentationTimeUs);
}
