/*
 * Copyright (c) 2017-2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

/**
 * Wraps a native webrtc::VideoEncoder.
 */
public abstract class WrappedNativeVideoEncoder implements VideoEncoder {
  @Override public abstract long createNativeVideoEncoder();
  @Override public abstract boolean isHardwareEncoder();

  @Override
  public final VideoCodecStatus initEncode(Settings settings, Callback encodeCallback) {
    throw new UnsupportedOperationException("Not implemented.");
  }

  @Override
  public final VideoCodecStatus release() {
    throw new UnsupportedOperationException("Not implemented.");
  }

  @Override
  public final VideoCodecStatus encode(VideoFrame frame, EncodeInfo info) {
    throw new UnsupportedOperationException("Not implemented.");
  }

  @Override
  public final VideoCodecStatus setRateAllocation(BitrateAllocation allocation, int framerate) {
    throw new UnsupportedOperationException("Not implemented.");
  }

  @Override
  public final ScalingSettings getScalingSettings() {
    throw new UnsupportedOperationException("Not implemented.");
  }

  @Override
  public final String getImplementationName() {
    throw new UnsupportedOperationException("Not implemented.");
  }
}
