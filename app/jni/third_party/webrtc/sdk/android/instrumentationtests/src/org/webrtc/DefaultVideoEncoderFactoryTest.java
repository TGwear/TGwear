/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

import static org.junit.Assert.assertEquals;

import androidx.annotation.Nullable;
import androidx.test.filters.SmallTest;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;

/** Unit tests for {@link DefaultVideoEncoderFactory}. */
public class DefaultVideoEncoderFactoryTest {
  static class CustomHardwareVideoEncoderFactory implements VideoEncoderFactory {
    private VideoCodecInfo supportedCodec;

    public CustomHardwareVideoEncoderFactory(VideoCodecInfo supportedCodec) {
      this.supportedCodec = supportedCodec;
    }

    @Override
    public @Nullable VideoEncoder createEncoder(VideoCodecInfo info) {
      return null;
    }

    @Override
    public VideoCodecInfo[] getSupportedCodecs() {
      return new VideoCodecInfo[] {supportedCodec};
    }
  }

  @Before
  public void setUp() {
    NativeLibrary.initialize(new NativeLibrary.DefaultLoader(), TestConstants.NATIVE_LIBRARY);
  }

  @SmallTest
  @Test
  public void getSupportedCodecs_hwVp8SameParamsAsSwVp8_oneVp8() {
    VideoCodecInfo hwVp8Encoder = new VideoCodecInfo("VP8", new HashMap<>());
    VideoEncoderFactory hwFactory = new CustomHardwareVideoEncoderFactory(hwVp8Encoder);
    DefaultVideoEncoderFactory defFactory = new DefaultVideoEncoderFactory(hwFactory);
    VideoCodecInfo[] supportedCodecs = defFactory.getSupportedCodecs();
    assertEquals(3, supportedCodecs.length);
    assertEquals("VP8", supportedCodecs[0].name);
    assertEquals("AV1", supportedCodecs[1].name);
    assertEquals("VP9", supportedCodecs[2].name);
  }

  @SmallTest
  @Test
  public void getSupportedCodecs_hwVp8WithDifferentParams_twoVp8() {
    VideoCodecInfo hwVp8Encoder = new VideoCodecInfo("VP8", new HashMap<String, String>() {
      { put("param", "value"); }
    });
    VideoEncoderFactory hwFactory = new CustomHardwareVideoEncoderFactory(hwVp8Encoder);
    DefaultVideoEncoderFactory defFactory = new DefaultVideoEncoderFactory(hwFactory);
    VideoCodecInfo[] supportedCodecs = defFactory.getSupportedCodecs();
    assertEquals(4, supportedCodecs.length);
    assertEquals("VP8", supportedCodecs[0].name);
    assertEquals("AV1", supportedCodecs[1].name);
    assertEquals("VP9", supportedCodecs[2].name);
    assertEquals("VP8", supportedCodecs[3].name);
    assertEquals(1, supportedCodecs[3].params.size());
    assertEquals("value", supportedCodecs[3].params.get("param"));
  }
}
