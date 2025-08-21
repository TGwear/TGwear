/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer;

import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.audio.AudioRendererEventListener;
import androidx.media3.exoplayer.metadata.MetadataOutput;
import androidx.media3.exoplayer.text.TextOutput;
import androidx.media3.exoplayer.video.VideoRendererEventListener;

/** Builds {@link Renderer} instances for use by an {@link ExoPlayer}. */
@UnstableApi
public interface RenderersFactory {

  /**
   * Builds the {@link Renderer} instances for an {@link ExoPlayer}.
   *
   * @param eventHandler A handler to use when invoking event listeners and outputs.
   * @param videoRendererEventListener An event listener for video renderers.
   * @param audioRendererEventListener An event listener for audio renderers.
   * @param textRendererOutput An output for text renderers.
   * @param metadataRendererOutput An output for metadata renderers.
   * @return The {@link Renderer instances}.
   */
  Renderer[] createRenderers(
      Handler eventHandler,
      VideoRendererEventListener videoRendererEventListener,
      AudioRendererEventListener audioRendererEventListener,
      TextOutput textRendererOutput,
      MetadataOutput metadataRendererOutput);

  /**
   * Provides a secondary {@link Renderer} instance for an {@link ExoPlayer} to use for pre-warming.
   *
   * <p>The created secondary {@code Renderer} should match its primary in its reported track type
   * support and {@link RendererCapabilities}.
   *
   * @param renderer The primary {@code Renderer} for which to create the backup.
   * @param eventHandler A handler to use when invoking event listeners and outputs.
   * @param videoRendererEventListener An event listener for video renderers.
   * @param audioRendererEventListener An event listener for audio renderers.
   * @param textRendererOutput An output for text renderers.
   * @param metadataRendererOutput An output for metadata renderers.
   * @return The {@link Renderer instances}.
   */
  @Nullable
  default Renderer createSecondaryRenderer(
      Renderer renderer,
      Handler eventHandler,
      VideoRendererEventListener videoRendererEventListener,
      AudioRendererEventListener audioRendererEventListener,
      TextOutput textRendererOutput,
      MetadataOutput metadataRendererOutput) {
    return null;
  }
}
