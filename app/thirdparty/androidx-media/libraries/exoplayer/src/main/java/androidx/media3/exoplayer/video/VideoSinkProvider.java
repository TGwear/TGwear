/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package androidx.media3.exoplayer.video;

import android.view.Surface;
import androidx.media3.common.util.Size;

/** A provider of {@link VideoSink VideoSinks}. */
/* package */ interface VideoSinkProvider {

  /**
   * Returns the {@link VideoSink} to forward video frames for processing.
   *
   * @param inputIndex The index of the {@link VideoSink}.
   * @return The {@link VideoSink} at the given index.
   */
  VideoSink getSink(int inputIndex);

  /** Sets the output surface info. */
  void setOutputSurfaceInfo(Surface outputSurface, Size outputResolution);

  /** Clears the set output surface info. */
  void clearOutputSurfaceInfo();

  /** Releases the sink provider. */
  void release();
}
