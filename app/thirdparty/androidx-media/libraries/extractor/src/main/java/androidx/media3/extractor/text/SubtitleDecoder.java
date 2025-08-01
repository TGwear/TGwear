/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.text;

import androidx.media3.common.util.UnstableApi;
import androidx.media3.decoder.Decoder;

/** Decodes {@link Subtitle}s from {@link SubtitleInputBuffer}s. */
@UnstableApi
public interface SubtitleDecoder
    extends Decoder<SubtitleInputBuffer, SubtitleOutputBuffer, SubtitleDecoderException> {

  /**
   * Informs the decoder of the current playback position.
   *
   * <p>Must be called prior to each attempt to dequeue output buffers from the decoder.
   *
   * @param positionUs The current playback position in microseconds.
   */
  void setPositionUs(long positionUs);
}
