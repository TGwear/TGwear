/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

import java.util.Map;
import java.util.HashMap;

/** Container for static helper functions related to dealing with H264 codecs. */
class H264Utils {
  public static final String H264_FMTP_PROFILE_LEVEL_ID = "profile-level-id";
  public static final String H264_FMTP_LEVEL_ASYMMETRY_ALLOWED = "level-asymmetry-allowed";
  public static final String H264_FMTP_PACKETIZATION_MODE = "packetization-mode";

  public static final String H264_PROFILE_CONSTRAINED_BASELINE = "42e0";
  public static final String H264_PROFILE_CONSTRAINED_HIGH = "640c";
  public static final String H264_LEVEL_3_1 = "1f"; // 31 in hex.
  public static final String H264_CONSTRAINED_HIGH_3_1 =
      H264_PROFILE_CONSTRAINED_HIGH + H264_LEVEL_3_1;
  public static final String H264_CONSTRAINED_BASELINE_3_1 =
      H264_PROFILE_CONSTRAINED_BASELINE + H264_LEVEL_3_1;

  public static Map<String, String> getDefaultH264Params(boolean isHighProfile) {
    final Map<String, String> params = new HashMap<>();
    params.put(VideoCodecInfo.H264_FMTP_LEVEL_ASYMMETRY_ALLOWED, "1");
    params.put(VideoCodecInfo.H264_FMTP_PACKETIZATION_MODE, "1");
    params.put(VideoCodecInfo.H264_FMTP_PROFILE_LEVEL_ID,
        isHighProfile ? VideoCodecInfo.H264_CONSTRAINED_HIGH_3_1
                      : VideoCodecInfo.H264_CONSTRAINED_BASELINE_3_1);
    return params;
  }

  public static VideoCodecInfo DEFAULT_H264_BASELINE_PROFILE_CODEC =
      new VideoCodecInfo("H264", getDefaultH264Params(/* isHighProfile= */ false));
  public static VideoCodecInfo DEFAULT_H264_HIGH_PROFILE_CODEC =
      new VideoCodecInfo("H264", getDefaultH264Params(/* isHighProfile= */ true));

  public static boolean isSameH264Profile(
      Map<String, String> params1, Map<String, String> params2) {
    return nativeIsSameH264Profile(params1, params2);
  }

  private static native boolean nativeIsSameH264Profile(
      Map<String, String> params1, Map<String, String> params2);
}
