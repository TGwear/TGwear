/*
 * Copyright (c) 2018-2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

import org.webrtc.Logging.Severity;

/**
 * Java interface for WebRTC logging. The default implementation uses webrtc.Logging.
 *
 * When injected, the Loggable will receive logging from both Java and native.
 */
public interface Loggable {
  public void onLogMessage(String message, Severity severity, String tag);
}
