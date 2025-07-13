/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

/**
 * BitrateAdjuster that adjusts the bitrate to compensate for changes in the framerate.  Used with
 * hardware codecs that assume the framerate never changes.
 */
class FramerateBitrateAdjuster extends BaseBitrateAdjuster {
  private static final int DEFAULT_FRAMERATE_FPS = 30;

  @Override
  public void setTargets(int targetBitrateBps, double targetFramerateFps) {
    // Keep frame rate unchanged and adjust bit rate.
    this.targetFramerateFps = DEFAULT_FRAMERATE_FPS;
    this.targetBitrateBps = (int) (targetBitrateBps * DEFAULT_FRAMERATE_FPS / targetFramerateFps);
  }
}
