/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

import org.webrtc.VideoDecoder;

/**
 * This class contains the Java glue code for JNI generation of VideoDecoder.
 */
class VideoDecoderWrapper {
  @CalledByNative
  static VideoDecoder.Callback createDecoderCallback(final long nativeDecoder) {
    return (VideoFrame frame, Integer decodeTimeMs,
               Integer qp) -> nativeOnDecodedFrame(nativeDecoder, frame, decodeTimeMs, qp);
  }

  private static native void nativeOnDecodedFrame(
      long nativeVideoDecoderWrapper, VideoFrame frame, Integer decodeTimeMs, Integer qp);
}
