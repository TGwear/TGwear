/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.effect;

import androidx.media3.common.ColorInfo;
import androidx.media3.common.GlTextureInfo;
import androidx.media3.common.VideoFrameProcessingException;
import androidx.media3.common.util.UnstableApi;

/**
 * Interface for a video compositor that combines frames from multiple input sources to produce
 * output frames.
 *
 * <p>Input and output are provided via OpenGL textures.
 *
 * <p>Methods may be called from any thread.
 */
@UnstableApi
public interface VideoCompositor extends GlTextureProducer {

  /** Listener for errors. */
  interface Listener {
    /**
     * Called when an exception occurs during asynchronous frame compositing.
     *
     * <p>If this is called, the calling {@link VideoCompositor} must immediately be {@linkplain
     * VideoCompositor#release() released}.
     */
    void onError(VideoFrameProcessingException exception);

    /** Called after {@link VideoCompositor} has output its final output frame. */
    void onEnded();
  }

  /**
   * Registers a new input source.
   *
   * @param inputIndex The index of the input source which could be used to determine the order of
   *     the input sources. The same index should to be used in {@link #queueInputTexture}. All
   *     inputs must be registered before {@linkplain #queueInputTexture(int, GlTextureProducer,
   *     GlTextureInfo, ColorInfo, long) queueing} textures.
   */
  void registerInputSource(int inputIndex);

  /**
   * Signals that no more frames will come from the upstream {@link GlTextureProducer.Listener}.
   *
   * @param inputIndex The index of the input source.
   */
  void signalEndOfInputSource(int inputIndex);

  /**
   * Queues an input texture to be composited.
   *
   * @param inputIndex The index of the input source, the same index used when {@linkplain
   *     #registerInputSource(int) registering the input source}.
   * @param textureProducer The source from where the {@code inputTexture} is produced.
   * @param inputTexture The {@link GlTextureInfo} to composite.
   * @param colorInfo The {@link ColorInfo} of {@code inputTexture}.
   * @param presentationTimeUs The presentation time of {@code inputTexture}, in microseconds.
   */
  void queueInputTexture(
      int inputIndex,
      GlTextureProducer textureProducer,
      GlTextureInfo inputTexture,
      ColorInfo colorInfo,
      long presentationTimeUs);

  /**
   * Releases all resources.
   *
   * <p>This {@link VideoCompositor} instance must not be used after this method is called.
   */
  void release();
}
