/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

import org.webrtc.CalledByNative;
import org.webrtc.Loggable;
import org.webrtc.Logging.Severity;

class JNILogging {
  private final Loggable loggable;

  public JNILogging(Loggable loggable) {
    this.loggable = loggable;
  }

  @CalledByNative
  public void logToInjectable(String message, Integer severity, String tag) {
    loggable.onLogMessage(message, Severity.values()[severity], tag);
  }
}
