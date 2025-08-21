/*
 * Copyright (c) 2018-2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

public class JavaVideoSourceTestHelper {
  @CalledByNative
  public static void startCapture(CapturerObserver observer, boolean success) {
    observer.onCapturerStarted(success);
  }

  @CalledByNative
  public static void stopCapture(CapturerObserver observer) {
    observer.onCapturerStopped();
  }

  @CalledByNative
  public static void deliverFrame(
      int width, int height, int rotation, long timestampNs, CapturerObserver observer) {
    observer.onFrameCaptured(
        new VideoFrame(JavaI420Buffer.allocate(width, height), rotation, timestampNs));
  }
}
