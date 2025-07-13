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
import java.util.LinkedHashSet;

/** Helper class that combines HW and SW encoders. */
public class DefaultVideoEncoderFactory implements VideoEncoderFactory {
  private final VideoEncoderFactory hardwareVideoEncoderFactory;
  private final VideoEncoderFactory softwareVideoEncoderFactory = new SoftwareVideoEncoderFactory();

  /** Create encoder factory using default hardware encoder factory. */
  public DefaultVideoEncoderFactory(
      EglBase.Context eglContext, boolean enableIntelVp8Encoder, boolean enableH264HighProfile) {
    this.hardwareVideoEncoderFactory =
        new HardwareVideoEncoderFactory(eglContext, enableIntelVp8Encoder, enableH264HighProfile);
  }

  /** Create encoder factory using explicit hardware encoder factory. */
  DefaultVideoEncoderFactory(VideoEncoderFactory hardwareVideoEncoderFactory) {
    this.hardwareVideoEncoderFactory = hardwareVideoEncoderFactory;
  }

  @Nullable
  @Override
  public VideoEncoder createEncoder(VideoCodecInfo info) {
    final VideoEncoder softwareEncoder = softwareVideoEncoderFactory.createEncoder(info);
    final VideoEncoder hardwareEncoder = hardwareVideoEncoderFactory.createEncoder(info);
    if (hardwareEncoder != null && softwareEncoder != null) {
      // Both hardware and software supported, wrap it in a software fallback
      return new VideoEncoderFallback(
          /* fallback= */ softwareEncoder, /* primary= */ hardwareEncoder);
    }
    return hardwareEncoder != null ? hardwareEncoder : softwareEncoder;
  }

  @Override
  public VideoCodecInfo[] getSupportedCodecs() {
    LinkedHashSet<VideoCodecInfo> supportedCodecInfos = new LinkedHashSet<VideoCodecInfo>();

    supportedCodecInfos.addAll(Arrays.asList(softwareVideoEncoderFactory.getSupportedCodecs()));
    supportedCodecInfos.addAll(Arrays.asList(hardwareVideoEncoderFactory.getSupportedCodecs()));

    return supportedCodecInfos.toArray(new VideoCodecInfo[supportedCodecInfos.size()]);
  }
}
