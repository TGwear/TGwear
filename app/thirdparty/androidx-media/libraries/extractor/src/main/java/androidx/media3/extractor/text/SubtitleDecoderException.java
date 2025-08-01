/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.text;

import androidx.annotation.Nullable;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.decoder.DecoderException;

/** Thrown when an error occurs decoding subtitle data. */
@UnstableApi
public class SubtitleDecoderException extends DecoderException {

  /**
   * @param message The detail message for this exception.
   */
  public SubtitleDecoderException(String message) {
    super(message);
  }

  /**
   * @param cause The cause of this exception, or {@code null}.
   */
  public SubtitleDecoderException(@Nullable Throwable cause) {
    super(cause);
  }

  /**
   * @param message The detail message for this exception.
   * @param cause The cause of this exception, or {@code null}.
   */
  public SubtitleDecoderException(String message, @Nullable Throwable cause) {
    super(message, cause);
  }
}
