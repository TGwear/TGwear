/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

/** BitrateAdjuster that tracks bitrate and framerate but does not adjust them. */
class BaseBitrateAdjuster implements BitrateAdjuster {
  protected int targetBitrateBps;
  protected double targetFramerateFps;

  @Override
  public void setTargets(int targetBitrateBps, double targetFramerateFps) {
    this.targetBitrateBps = targetBitrateBps;
    this.targetFramerateFps = targetFramerateFps;
  }

  @Override
  public void reportEncodedFrame(int size) {
    // No op.
  }

  @Override
  public int getAdjustedBitrateBps() {
    return targetBitrateBps;
  }

  @Override
  public double getAdjustedFramerateFps() {
    return targetFramerateFps;
  }
}
