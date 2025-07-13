/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

/**
 * A combined video encoder that falls back on a secondary encoder if the primary encoder fails.
 */
public class VideoEncoderFallback extends WrappedNativeVideoEncoder {
  private final VideoEncoder fallback;
  private final VideoEncoder primary;

  public VideoEncoderFallback(VideoEncoder fallback, VideoEncoder primary) {
    this.fallback = fallback;
    this.primary = primary;
  }

  @Override
  public long createNativeVideoEncoder() {
    return nativeCreateEncoder(fallback, primary);
  }

  @Override
  public boolean isHardwareEncoder() {
    return primary.isHardwareEncoder();
  }

  private static native long nativeCreateEncoder(VideoEncoder fallback, VideoEncoder primary);
}
