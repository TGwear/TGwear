/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

import org.webrtc.VideoEncoder;

/**
 * An implementation of VideoEncoder that is used for testing of functionalities of
 * VideoEncoderWrapper.
 */
class FakeVideoEncoder implements VideoEncoder {
  @Override
  public VideoCodecStatus initEncode(Settings settings, Callback encodeCallback) {
    return VideoCodecStatus.OK;
  }

  @Override
  public VideoCodecStatus release() {
    return VideoCodecStatus.OK;
  }

  @Override
  public VideoCodecStatus encode(VideoFrame frame, EncodeInfo info) {
    return VideoCodecStatus.OK;
  }

  @Override
  public VideoCodecStatus setRateAllocation(BitrateAllocation allocation, int framerate) {
    return VideoCodecStatus.OK;
  }

  @Override
  public ScalingSettings getScalingSettings() {
    return ScalingSettings.OFF;
  }

  @Override
  public ResolutionBitrateLimits[] getResolutionBitrateLimits() {
    ResolutionBitrateLimits resolution_bitrate_limits[] = {
        new ResolutionBitrateLimits(/* frameSizePixels = */ 640 * 360,
            /* minStartBitrateBps = */ 300000,
            /* minBitrateBps = */ 200000,
            /* maxBitrateBps = */ 1000000)};

    return resolution_bitrate_limits;
  }

  @Override
  public String getImplementationName() {
    return "FakeVideoEncoder";
  }
}
