/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

import androidx.annotation.Nullable;

/** Factory for creating VideoDecoders. */
public interface VideoDecoderFactory {
  /**
   * Creates a VideoDecoder for the given codec. Supports the same codecs supported by
   * VideoEncoderFactory.
   */
  @Nullable @CalledByNative VideoDecoder createDecoder(VideoCodecInfo info);

  /**
   * Enumerates the list of supported video codecs.
   */
  @CalledByNative
  default VideoCodecInfo[] getSupportedCodecs() {
    return new VideoCodecInfo[0];
  }
}
