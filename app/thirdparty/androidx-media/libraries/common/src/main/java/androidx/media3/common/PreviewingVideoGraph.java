/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package androidx.media3.common;

import android.content.Context;
import androidx.media3.common.util.UnstableApi;
import java.util.List;
import java.util.concurrent.Executor;

/** A {@link VideoGraph} specific to previewing. */
@UnstableApi
public interface PreviewingVideoGraph extends VideoGraph {

  /** A factory for creating a {@link PreviewingVideoGraph}. */
  interface Factory {
    /**
     * Creates a new {@link PreviewingVideoGraph} instance.
     *
     * @param context A {@link Context}.
     * @param outputColorInfo The {@link ColorInfo} for the output frames.
     * @param debugViewProvider A {@link DebugViewProvider}.
     * @param listener A {@link Listener}.
     * @param listenerExecutor The {@link Executor} on which the {@code listener} is invoked.
     * @param videoCompositorSettings The {@link VideoCompositorSettings}.
     * @param compositionEffects A list of {@linkplain Effect effects} to apply to the composition.
     * @param initialTimestampOffsetUs The timestamp offset for the first frame, in microseconds.
     * @return A new instance.
     * @throws VideoFrameProcessingException If a problem occurs while creating the {@link
     *     VideoFrameProcessor}.
     */
    PreviewingVideoGraph create(
        Context context,
        ColorInfo outputColorInfo,
        DebugViewProvider debugViewProvider,
        Listener listener,
        Executor listenerExecutor,
        VideoCompositorSettings videoCompositorSettings,
        List<Effect> compositionEffects,
        long initialTimestampOffsetUs)
        throws VideoFrameProcessingException;

    /**
     * Returns whether the {@link VideoGraph} implementation supports {@linkplain #registerInput
     * registering} multiple inputs.
     */
    boolean supportsMultipleInputs();
  }

  /**
   * Renders the oldest unrendered output frame that has become {@linkplain
   * Listener#onOutputFrameAvailableForRendering(long) available for rendering} at the given {@code
   * renderTimeNs}.
   *
   * <p>This will either render the output frame to the {@linkplain #setOutputSurfaceInfo output
   * surface}, or drop the frame, per {@code renderTimeNs}.
   *
   * <p>The {@code renderTimeNs} may be passed to {@link
   * android.opengl.EGLExt#eglPresentationTimeANDROID} depending on the implementation.
   *
   * @param renderTimeNs The render time to use for the frame, in nanoseconds. The render time can
   *     be before or after the current system time. Use {@link
   *     VideoFrameProcessor#DROP_OUTPUT_FRAME} to drop the frame, or {@link
   *     VideoFrameProcessor#RENDER_OUTPUT_FRAME_IMMEDIATELY} to render the frame immediately.
   */
  void renderOutputFrame(long renderTimeNs);
}
