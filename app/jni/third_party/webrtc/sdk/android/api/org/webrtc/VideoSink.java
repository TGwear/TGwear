/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

/**
 * Java version of rtc::VideoSinkInterface.
 */
public interface VideoSink {
  /**
   * Implementations should call frame.retain() if they need to hold a reference to the frame after
   * this function returns. Each call to retain() should be followed by a call to frame.release()
   * when the reference is no longer needed.
   */
  @CalledByNative void onFrame(VideoFrame frame);
}
