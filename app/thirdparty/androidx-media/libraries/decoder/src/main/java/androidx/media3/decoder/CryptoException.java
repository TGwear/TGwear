/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.decoder;

import androidx.media3.common.util.UnstableApi;

/** Thrown when a non-platform component fails to decrypt data. */
@UnstableApi
public class CryptoException extends Exception {

  /** A component specific error code. */
  public final int errorCode;

  /**
   * @param errorCode A component specific error code.
   * @param message The detail message.
   */
  public CryptoException(int errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }
}
