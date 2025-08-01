/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package androidx.media3.transformer;

import android.content.Context;
import androidx.media3.common.ColorInfo;
import androidx.media3.common.DebugViewProvider;
import androidx.media3.common.Effect;
import androidx.media3.common.SurfaceInfo;
import androidx.media3.common.VideoCompositorSettings;
import androidx.media3.common.VideoFrameProcessingException;
import androidx.media3.common.VideoFrameProcessor;
import androidx.media3.common.VideoGraph;
import java.util.List;
import java.util.concurrent.Executor;

/** The {@link VideoGraph} to support {@link Transformer} specific use cases. */
/* package */ interface TransformerVideoGraph extends VideoGraph {

  /** A factory for creating a {@link TransformerVideoGraph}. */
  interface Factory {
    /**
     * Creates a new {@link TransformerVideoGraph} instance.
     *
     * @param context A {@link Context}.
     * @param outputColorInfo The {@link ColorInfo} for the output frames.
     * @param debugViewProvider A {@link DebugViewProvider}.
     * @param listener A {@link Listener}.
     * @param listenerExecutor The {@link Executor} on which the {@code listener} is invoked.
     * @param videoCompositorSettings The {@link VideoCompositorSettings} to apply to the
     *     composition.
     * @param compositionEffects A list of {@linkplain Effect effects} to apply to the composition.
     * @param initialTimestampOffsetUs The timestamp offset for the first frame, in microseconds.
     * @param renderFramesAutomatically If {@code true}, the instance will render output frames to
     *     the {@linkplain #setOutputSurfaceInfo(SurfaceInfo) output surface} automatically as the
     *     instance is done processing them. If {@code false}, the instance will block until {@link
     *     #renderOutputFrameWithMediaPresentationTime()} is called, to render the frame.
     * @return A new instance.
     * @throws VideoFrameProcessingException If a problem occurs while creating the {@link
     *     VideoFrameProcessor}.
     */
    TransformerVideoGraph create(
        Context context,
        ColorInfo outputColorInfo,
        DebugViewProvider debugViewProvider,
        Listener listener,
        Executor listenerExecutor,
        VideoCompositorSettings videoCompositorSettings,
        List<Effect> compositionEffects,
        long initialTimestampOffsetUs,
        boolean renderFramesAutomatically)
        throws VideoFrameProcessingException;
  }

  /**
   * Returns a {@link GraphInput} object to which the {@code VideoGraph} inputs are queued.
   *
   * <p>This method must be called after successfully {@linkplain #initialize() initializing} the
   * {@code VideoGraph}.
   *
   * <p>This method must called exactly once for every input stream.
   *
   * <p>If the method throws any {@link Exception}, the caller must call {@link #release}.
   *
   * @param inputIndex The index of the input, which could be used to order the inputs.
   */
  GraphInput createInput(int inputIndex) throws VideoFrameProcessingException;

  /**
   * Renders the oldest unrendered output frame that has become {@linkplain
   * Listener#onOutputFrameAvailableForRendering(long) available for rendering} to the output
   * surface.
   *
   * <p>This method must only be called if {@code renderFramesAutomatically} was set to {@code
   * false} using the {@link Factory} and should be called exactly once for each frame that becomes
   * {@linkplain Listener#onOutputFrameAvailableForRendering(long) available for rendering}.
   *
   * <p>This will render the output frame to the {@linkplain #setOutputSurfaceInfo output surface}
   * with the presentation seen in {@link Listener#onOutputFrameAvailableForRendering(long)}.
   */
  void renderOutputFrameWithMediaPresentationTime();
}
