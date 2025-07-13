/*
 * Copyright (c) 2018-2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

import java.util.HashMap;
import java.util.Map;

public class CodecsWrapperTestHelper {
  @CalledByNative
  public static VideoCodecInfo createTestVideoCodecInfo() {
    Map<String, String> params = new HashMap<String, String>();
    params.put(
        VideoCodecInfo.H264_FMTP_PROFILE_LEVEL_ID, VideoCodecInfo.H264_CONSTRAINED_BASELINE_3_1);

    VideoCodecInfo codec_info = new VideoCodecInfo("H264", params);
    return codec_info;
  }

  @CalledByNative
  public static VideoEncoder createFakeVideoEncoder() {
    return new FakeVideoEncoder();
  }
}
