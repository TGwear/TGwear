/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

import androidx.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class SoftwareVideoEncoderFactory implements VideoEncoderFactory {
  private static final String TAG = "SoftwareVideoEncoderFactory";

  private final long nativeFactory;

  public SoftwareVideoEncoderFactory() {
    this.nativeFactory = nativeCreateFactory();
  }

  @Nullable
  @Override
  public VideoEncoder createEncoder(VideoCodecInfo info) {
    long nativeEncoder = nativeCreateEncoder(nativeFactory, info);
    if (nativeEncoder == 0) {
      Logging.w(TAG, "Trying to create encoder for unsupported format. " + info);
      return null;
    }

    return new WrappedNativeVideoEncoder() {
      @Override
      public long createNativeVideoEncoder() {
        return nativeEncoder;
      }

      @Override
      public boolean isHardwareEncoder() {
        return false;
      }
    };
  }

  @Override
  public VideoCodecInfo[] getSupportedCodecs() {
    return nativeGetSupportedCodecs(nativeFactory).toArray(new VideoCodecInfo[0]);
  }

  private static native long nativeCreateFactory();

  private static native long nativeCreateEncoder(long factory, VideoCodecInfo videoCodecInfo);

  private static native List<VideoCodecInfo> nativeGetSupportedCodecs(long factory);
}
