/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.video;

import androidx.media3.common.util.UnstableApi;
import androidx.media3.decoder.VideoDecoderOutputBuffer;

/** Renders the {@link VideoDecoderOutputBuffer}. */
@UnstableApi
public interface VideoDecoderOutputBufferRenderer {

  /**
   * Sets the output buffer to be rendered. The renderer is responsible for releasing the buffer.
   *
   * @param outputBuffer The output buffer to be rendered.
   */
  void setOutputBuffer(VideoDecoderOutputBuffer outputBuffer);
}
