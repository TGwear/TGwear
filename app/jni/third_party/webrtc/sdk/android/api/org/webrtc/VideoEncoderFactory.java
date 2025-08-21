/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

import androidx.annotation.Nullable;

/** Factory for creating VideoEncoders. */
public interface VideoEncoderFactory {
  public interface VideoEncoderSelector {
    /** Called with the VideoCodecInfo of the currently used encoder. */
    @CalledByNative("VideoEncoderSelector") void onCurrentEncoder(VideoCodecInfo info);

    /**
     * Called with the current available bitrate. Returns null if the encoder selector prefers to
     * keep the current encoder or a VideoCodecInfo if a new encoder is preferred.
     */
    @Nullable @CalledByNative("VideoEncoderSelector") VideoCodecInfo onAvailableBitrate(int kbps);

    /**
     * Called every time the encoder input resolution change. Returns null if the encoder selector
     * prefers to keep the current encoder or a VideoCodecInfo if a new encoder is preferred.
     */
    @Nullable
    @CalledByNative("VideoEncoderSelector")
    default VideoCodecInfo onResolutionChange(int widht, int height) {
      return null;
    }

    /**
     * Called when the currently used encoder signal itself as broken. Returns null if the encoder
     * selector prefers to keep the current encoder or a VideoCodecInfo if a new encoder is
     * preferred.
     */
    @Nullable @CalledByNative("VideoEncoderSelector") VideoCodecInfo onEncoderBroken();
  }

  /** Creates an encoder for the given video codec. */
  @Nullable @CalledByNative VideoEncoder createEncoder(VideoCodecInfo info);

  /**
   * Enumerates the list of supported video codecs. This method will only be called once and the
   * result will be cached.
   */
  @CalledByNative VideoCodecInfo[] getSupportedCodecs();

  /**
   * Enumerates the list of supported video codecs that can also be tagged with
   * implementation information. This method will only be called once and the
   * result will be cached.
   */
  @CalledByNative
  default VideoCodecInfo[] getImplementations() {
    return getSupportedCodecs();
  }

  /**
   * Returns a VideoEncoderSelector if implemented by the VideoEncoderFactory,
   * null otherwise.
   */
  @CalledByNative
  default VideoEncoderSelector getEncoderSelector() {
    return null;
  }
}
