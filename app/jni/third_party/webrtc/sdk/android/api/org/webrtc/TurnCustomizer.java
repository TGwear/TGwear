/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

/** Java wrapper for a C++ TurnCustomizer. */
public class TurnCustomizer {
  private long nativeTurnCustomizer;

  public TurnCustomizer(long nativeTurnCustomizer) {
    this.nativeTurnCustomizer = nativeTurnCustomizer;
  }

  public void dispose() {
    checkTurnCustomizerExists();
    nativeFreeTurnCustomizer(nativeTurnCustomizer);
    nativeTurnCustomizer = 0;
  }

  private static native void nativeFreeTurnCustomizer(long turnCustomizer);

  /** Return a pointer to webrtc::TurnCustomizer. */
  @CalledByNative
  long getNativeTurnCustomizer() {
    checkTurnCustomizerExists();
    return nativeTurnCustomizer;
  }

  private void checkTurnCustomizerExists() {
    if (nativeTurnCustomizer == 0) {
      throw new IllegalStateException("TurnCustomizer has been disposed.");
    }
  }
}
