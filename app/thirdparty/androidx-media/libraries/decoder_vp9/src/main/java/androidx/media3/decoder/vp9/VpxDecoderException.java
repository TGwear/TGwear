/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.decoder.vp9;

import androidx.media3.common.util.UnstableApi;
import androidx.media3.decoder.DecoderException;

/**
 * Thrown when a libvpx decoder error occurs.
 */
@UnstableApi
public final class VpxDecoderException extends DecoderException {

    /* package */ VpxDecoderException(String message) {
        super(message);
    }

    /* package */ VpxDecoderException(String message, Throwable cause) {
        super(message, cause);
    }
}
