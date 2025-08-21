/*
 * Copyright (c) 2017-2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

/**
 * Wraps a native webrtc::VideoDecoder.
 */
public abstract class WrappedNativeVideoDecoder implements VideoDecoder {
  @Override public abstract long createNative(long webrtcEnvRef);

  @Override
  public final VideoCodecStatus initDecode(Settings settings, Callback decodeCallback) {
    throw new UnsupportedOperationException("Not implemented.");
  }

  @Override
  public final VideoCodecStatus release() {
    throw new UnsupportedOperationException("Not implemented.");
  }

  @Override
  public final VideoCodecStatus decode(EncodedImage frame, DecodeInfo info) {
    throw new UnsupportedOperationException("Not implemented.");
  }

  @Override
  public final String getImplementationName() {
    throw new UnsupportedOperationException("Not implemented.");
  }
}
