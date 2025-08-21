/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.decoder.av1;

import androidx.media3.common.util.UnstableApi;
import androidx.media3.decoder.DecoderException;

/** Thrown when a libgav1 decoder error occurs. */
@UnstableApi
public final class Gav1DecoderException extends DecoderException {

  /* package */ Gav1DecoderException(String message) {
    super(message);
  }

  /* package */ Gav1DecoderException(String message, Throwable cause) {
    super(message, cause);
  }
}
