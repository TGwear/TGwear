/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

import androidx.annotation.Nullable;
import java.util.List;

public class SoftwareVideoDecoderFactory implements VideoDecoderFactory {
  private static final String TAG = "SoftwareVideoDecoderFactory";

  private final long nativeFactory;

  public SoftwareVideoDecoderFactory() {
    this.nativeFactory = nativeCreateFactory();
  }

  @Nullable
  @Override
  public VideoDecoder createDecoder(VideoCodecInfo info) {
    if (!nativeIsSupported(nativeFactory, info)) {
      Logging.w(TAG, "Trying to create decoder for unsupported format. " + info);
      return null;
    }
    return new WrappedNativeVideoDecoder() {
      @Override
      public long createNative(long webrtcEnvRef) {
        return nativeCreate(nativeFactory, webrtcEnvRef, info);
      }
    };
  }

  @Override
  public VideoCodecInfo[] getSupportedCodecs() {
    return nativeGetSupportedCodecs(nativeFactory).toArray(new VideoCodecInfo[0]);
  }

  private static native long nativeCreateFactory();

  private static native boolean nativeIsSupported(long factory, VideoCodecInfo info);

  private static native long nativeCreate(
      long factory, long webrtcEnvRef, VideoCodecInfo info);

  private static native List<VideoCodecInfo> nativeGetSupportedCodecs(long factory);
}
