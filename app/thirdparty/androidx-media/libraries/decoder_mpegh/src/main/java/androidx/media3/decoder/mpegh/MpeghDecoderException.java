/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.decoder.mpegh;

import androidx.media3.common.util.UnstableApi;
import androidx.media3.decoder.DecoderException;

/** Thrown when an MPEG-H decoder error occurs. */
@UnstableApi
public class MpeghDecoderException extends DecoderException {

  public MpeghDecoderException(String message) {
    super(message, new Throwable());
  }

  public MpeghDecoderException(String message, Throwable cause) {
    super(message, cause);
  }
}
