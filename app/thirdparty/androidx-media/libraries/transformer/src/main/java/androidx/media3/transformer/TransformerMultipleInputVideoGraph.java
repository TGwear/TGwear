/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package androidx.media3.transformer;

import static androidx.media3.common.VideoFrameProcessor.RENDER_OUTPUT_FRAME_WITH_PRESENTATION_TIME;

import android.content.Context;
import androidx.media3.common.ColorInfo;
import androidx.media3.common.DebugViewProvider;
import androidx.media3.common.Effect;
import androidx.media3.common.VideoCompositorSettings;
import androidx.media3.common.VideoFrameProcessingException;
import androidx.media3.common.VideoFrameProcessor;
import androidx.media3.common.VideoGraph;
import androidx.media3.effect.MultipleInputVideoGraph;
import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * A {@link TransformerVideoGraph Transformer}-specific implementation of {@link
 * MultipleInputVideoGraph}.
 */
/* package */ final class TransformerMultipleInputVideoGraph extends MultipleInputVideoGraph
    implements TransformerVideoGraph {

  /** A factory for creating {@link TransformerMultipleInputVideoGraph} instances. */
  public static final class Factory implements TransformerVideoGraph.Factory {

    private final VideoFrameProcessor.Factory videoFrameProcessorFactory;

    public Factory(VideoFrameProcessor.Factory videoFrameProcessorFactory) {
      this.videoFrameProcessorFactory = videoFrameProcessorFactory;
    }

    @Override
    public TransformerMultipleInputVideoGraph create(
        Context context,
        ColorInfo outputColorInfo,
        DebugViewProvider debugViewProvider,
        VideoGraph.Listener listener,
        Executor listenerExecutor,
        VideoCompositorSettings videoCompositorSettings,
        List<Effect> compositionEffects,
        long initialTimestampOffsetUs,
        boolean renderFramesAutomatically) {
      return new TransformerMultipleInputVideoGraph(
          context,
          videoFrameProcessorFactory,
          outputColorInfo,
          debugViewProvider,
          listener,
          listenerExecutor,
          videoCompositorSettings,
          compositionEffects,
          initialTimestampOffsetUs,
          renderFramesAutomatically);
    }
  }

  private TransformerMultipleInputVideoGraph(
      Context context,
      VideoFrameProcessor.Factory videoFrameProcessorFactory,
      ColorInfo outputColorInfo,
      DebugViewProvider debugViewProvider,
      Listener listener,
      Executor listenerExecutor,
      VideoCompositorSettings videoCompositorSettings,
      List<Effect> compositionEffects,
      long initialTimestampOffsetUs,
      boolean renderFramesAutomatically) {
    super(
        context,
        videoFrameProcessorFactory,
        outputColorInfo,
        debugViewProvider,
        listener,
        listenerExecutor,
        videoCompositorSettings,
        compositionEffects,
        initialTimestampOffsetUs,
        renderFramesAutomatically);
  }

  @Override
  public GraphInput createInput(int inputIndex) throws VideoFrameProcessingException {
    registerInput(inputIndex);
    return new VideoFrameProcessingWrapper(
        getProcessor(inputIndex),
        /* postProcessingEffects= */ ImmutableList.of(),
        getInitialTimestampOffsetUs());
  }

  @Override
  public void renderOutputFrameWithMediaPresentationTime() {
    getCompositionVideoFrameProcessor()
        .renderOutputFrame(RENDER_OUTPUT_FRAME_WITH_PRESENTATION_TIME);
  }
}
