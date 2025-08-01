/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package androidx.media3.effect;

import android.content.Context;
import androidx.media3.common.ColorInfo;
import androidx.media3.common.DebugViewProvider;
import androidx.media3.common.Effect;
import androidx.media3.common.PreviewingVideoGraph;
import androidx.media3.common.VideoCompositorSettings;
import androidx.media3.common.VideoFrameProcessor;
import androidx.media3.common.util.UnstableApi;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * A {@link PreviewingVideoGraph Previewing} specific implementation of {@link
 * SingleInputVideoGraph}.
 */
@UnstableApi
public final class PreviewingSingleInputVideoGraph extends SingleInputVideoGraph
    implements PreviewingVideoGraph {

  /** A factory for creating a {@link PreviewingSingleInputVideoGraph}. */
  public static final class Factory implements PreviewingVideoGraph.Factory {

    private final VideoFrameProcessor.Factory videoFrameProcessorFactory;

    /**
     * Creates a new factory that uses the {@link DefaultVideoFrameProcessor.Factory} with its
     * default values.
     */
    public Factory() {
      this(new DefaultVideoFrameProcessor.Factory.Builder().build());
    }

    /**
     * Creates an instance that uses the supplied {@code videoFrameProcessorFactory} to create
     * {@link VideoFrameProcessor} instances.
     */
    public Factory(VideoFrameProcessor.Factory videoFrameProcessorFactory) {
      this.videoFrameProcessorFactory = videoFrameProcessorFactory;
    }

    @Override
    public PreviewingVideoGraph create(
        Context context,
        ColorInfo outputColorInfo,
        DebugViewProvider debugViewProvider,
        Listener listener,
        Executor listenerExecutor,
        VideoCompositorSettings videoCompositorSettings,
        List<Effect> compositionEffects,
        long initialTimestampOffsetUs) {
      return new PreviewingSingleInputVideoGraph(
          context,
          videoFrameProcessorFactory,
          outputColorInfo,
          debugViewProvider,
          listener,
          listenerExecutor,
          initialTimestampOffsetUs);
    }

    @Override
    public boolean supportsMultipleInputs() {
      return false;
    }
  }

  private PreviewingSingleInputVideoGraph(
      Context context,
      VideoFrameProcessor.Factory videoFrameProcessorFactory,
      ColorInfo outputColorInfo,
      DebugViewProvider debugViewProvider,
      Listener listener,
      Executor listenerExecutor,
      long initialTimestampOffsetUs) {
    super(
        context,
        videoFrameProcessorFactory,
        outputColorInfo,
        listener,
        debugViewProvider,
        listenerExecutor,
        VideoCompositorSettings.DEFAULT,
        // Previewing needs frame render timing.
        /* renderFramesAutomatically= */ false,
        initialTimestampOffsetUs);
  }

  @Override
  public void renderOutputFrame(long renderTimeNs) {
    getProcessor(getInputIndex()).renderOutputFrame(renderTimeNs);
  }
}
