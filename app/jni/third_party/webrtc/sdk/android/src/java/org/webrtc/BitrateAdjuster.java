/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

/** Object that adjusts the bitrate of a hardware codec. */
interface BitrateAdjuster {
  /**
   * Sets the target bitrate in bits per second and framerate in frames per second.
   */
  void setTargets(int targetBitrateBps, double targetFramerateFps);

  /**
   * Should be used to report the size of an encoded frame to the bitrate adjuster. Use
   * getAdjustedBitrateBps to get the updated bitrate after calling this method.
   */
  void reportEncodedFrame(int size);

  /** Gets the current bitrate. */
  int getAdjustedBitrateBps();

  /** Gets the current framerate. */
  double getAdjustedFramerateFps();
}
