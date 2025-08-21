/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

public class CallSessionFileRotatingLogSink {
  private long nativeSink;

  public static byte[] getLogData(String dirPath) {
    if (dirPath == null) {
      throw new IllegalArgumentException("dirPath may not be null.");
    }
    return nativeGetLogData(dirPath);
  }

  public CallSessionFileRotatingLogSink(
      String dirPath, int maxFileSize, Logging.Severity severity) {
    if (dirPath == null) {
      throw new IllegalArgumentException("dirPath may not be null.");
    }
    nativeSink = nativeAddSink(dirPath, maxFileSize, severity.ordinal());
  }

  public void dispose() {
    if (nativeSink != 0) {
      nativeDeleteSink(nativeSink);
      nativeSink = 0;
    }
  }

  private static native long nativeAddSink(String dirPath, int maxFileSize, int severity);
  private static native void nativeDeleteSink(long sink);
  private static native byte[] nativeGetLogData(String dirPath);
}
