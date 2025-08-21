/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package androidx.media3.transformer;

import static androidx.media3.common.VideoFrameProcessor.RENDER_OUTPUT_FRAME_WITH_PRESENTATION_TIME;
import static androidx.media3.common.util.Assertions.checkState;

import android.content.Context;
import androidx.media3.common.ColorInfo;
import androidx.media3.common.DebugViewProvider;
import androidx.media3.common.Effect;
import androidx.media3.common.VideoCompositorSettings;
import androidx.media3.common.VideoFrameProcessingException;
import androidx.media3.common.VideoFrameProcessor;
import androidx.media3.effect.SingleInputVideoGraph;
import java.util.List;
import java.util.concurrent.Executor;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

/**
 * A {@link TransformerVideoGraph Transformer}-specific implementation of {@link
 * SingleInputVideoGraph}.
 */
/* package */ final class TransformerSingleInputVideoGraph extends SingleInputVideoGraph
    implements TransformerVideoGraph {

  /** A factory for creating {@link TransformerSingleInputVideoGraph} instances. */
  public static final class Factory implements TransformerVideoGraph.Factory {

    private final VideoFrameProcessor.Factory videoFrameProcessorFactory;

    public Factory(VideoFrameProcessor.Factory videoFrameProcessorFactory) {
      this.videoFrameProcessorFactory = videoFrameProcessorFactory;
    }

    @Override
    public TransformerSingleInputVideoGraph create(
        Context context,
        ColorInfo outputColorInfo,
        DebugViewProvider debugViewProvider,
        Listener listener,
        Executor listenerExecutor,
        VideoCompositorSettings videoCompositorSettings,
        List<Effect> compositionEffects,
        long initialTimestampOffsetUs,
        boolean renderFramesAutomatically) {
      return new TransformerSingleInputVideoGraph(
          context,
          videoFrameProcessorFactory,
          outputColorInfo,
          listener,
          debugViewProvider,
          listenerExecutor,
          videoCompositorSettings,
          renderFramesAutomatically,
          compositionEffects,
          initialTimestampOffsetUs);
    }
  }

  private final List<Effect> compositionEffects;
  private @MonotonicNonNull VideoFrameProcessingWrapper videoFrameProcessingWrapper;

  private TransformerSingleInputVideoGraph(
      Context context,
      VideoFrameProcessor.Factory videoFrameProcessorFactory,
      ColorInfo outputColorInfo,
      Listener listener,
      DebugViewProvider debugViewProvider,
      Executor listenerExecutor,
      VideoCompositorSettings videoCompositorSettings,
      boolean renderFramesAutomatically,
      List<Effect> compositionEffects,
      long initialTimestampOffsetUs) {
    super(
        context,
        videoFrameProcessorFactory,
        outputColorInfo,
        listener,
        debugViewProvider,
        listenerExecutor,
        videoCompositorSettings,
        renderFramesAutomatically,
        initialTimestampOffsetUs);
    this.compositionEffects = compositionEffects;
  }

  @Override
  public GraphInput createInput(int inputIndex) throws VideoFrameProcessingException {
    checkState(videoFrameProcessingWrapper == null);
    registerInput(inputIndex);
    videoFrameProcessingWrapper =
        new VideoFrameProcessingWrapper(
            getProcessor(inputIndex), compositionEffects, getInitialTimestampOffsetUs());
    return videoFrameProcessingWrapper;
  }

  @Override
  public void renderOutputFrameWithMediaPresentationTime() {
    getProcessor(getInputIndex()).renderOutputFrame(RENDER_OUTPUT_FRAME_WITH_PRESENTATION_TIME);
  }
}
