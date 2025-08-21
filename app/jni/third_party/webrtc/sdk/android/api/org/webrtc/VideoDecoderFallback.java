/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

/**
 * A combined video decoder that falls back on a secondary decoder if the primary decoder fails.
 */
public class VideoDecoderFallback extends WrappedNativeVideoDecoder {
  private final VideoDecoder fallback;
  private final VideoDecoder primary;

  public VideoDecoderFallback(VideoDecoder fallback, VideoDecoder primary) {
    this.fallback = fallback;
    this.primary = primary;
  }

  @Override
  public long createNative(long webrtcEnvRef) {
    return nativeCreate(webrtcEnvRef, fallback, primary);
  }

  private static native long nativeCreate(
      long webrtcEnvRef, VideoDecoder fallback, VideoDecoder primary);
}
