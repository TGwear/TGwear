/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

/**
 * Interface for observering a capturer. Passed to {@link VideoCapturer#initialize}. Provided by
 * {@link VideoSource#getCapturerObserver}.
 *
 * All callbacks must be executed on a single thread.
 */
public interface CapturerObserver {
  /** Notify if the capturer have been started successfully or not. */
  void onCapturerStarted(boolean success);
  /** Notify that the capturer has been stopped. */
  void onCapturerStopped();

  /** Delivers a captured frame. */
  void onFrameCaptured(VideoFrame frame);
}
