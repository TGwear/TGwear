/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.transformer.mh;

import static androidx.media3.common.util.Assertions.checkState;
import static androidx.media3.transformer.AndroidTestUtil.assumeFormatsSupported;
import static androidx.media3.transformer.AndroidTestUtil.recordTestSkipped;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import android.content.Context;
import androidx.media3.common.ColorInfo;
import androidx.media3.common.Format;
import androidx.media3.common.util.GlUtil;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.mediacodec.MediaCodecUtil;
import androidx.media3.transformer.EncoderUtil;
import java.io.IOException;
import org.json.JSONException;
import org.junit.AssumptionViolatedException;

/** Utility class for checking HDR capabilities. */
public final class HdrCapabilitiesUtil {
  private static final String SKIP_REASON_NO_OPENGL_UNDER_API_29 =
      "OpenGL-based HDR to SDR tone mapping is unsupported below API 29.";
  private static final String SKIP_REASON_NO_YUV = "Device lacks YUV extension support.";

  /**
   * Assumes that the device supports OpenGL tone-mapping for the {@code inputFormat}.
   *
   * @throws AssumptionViolatedException if the device does not support OpenGL tone-mapping.
   */
  public static void assumeDeviceSupportsOpenGlToneMapping(String testId, Format inputFormat)
      throws JSONException, IOException, MediaCodecUtil.DecoderQueryException {
    Context context = getApplicationContext();
    if (Util.SDK_INT < 29) {
      recordTestSkipped(context, testId, SKIP_REASON_NO_OPENGL_UNDER_API_29);
      throw new AssumptionViolatedException(SKIP_REASON_NO_OPENGL_UNDER_API_29);
    }
    if (!GlUtil.isYuvTargetExtensionSupported()) {
      recordTestSkipped(context, testId, SKIP_REASON_NO_YUV);
      throw new AssumptionViolatedException(SKIP_REASON_NO_YUV);
    }
    assumeFormatsSupported(context, testId, inputFormat, /* outputFormat= */ null);
  }

  /**
   * Assumes that the device supports HDR editing for the given {@code colorInfo}.
   *
   * @throws AssumptionViolatedException if the device does not support HDR editing.
   */
  public static void assumeDeviceSupportsHdrEditing(String testId, Format format)
      throws JSONException, IOException {
    checkState(ColorInfo.isTransferHdr(format.colorInfo));
    if (EncoderUtil.getSupportedEncodersForHdrEditing(format.sampleMimeType, format.colorInfo)
        .isEmpty()) {
      String skipReason =
          "No HDR editing supported for sample mime type "
              + format.sampleMimeType
              + " and color info "
              + format.colorInfo;
      recordTestSkipped(getApplicationContext(), testId, skipReason);
      throw new AssumptionViolatedException(skipReason);
    }
  }

  /**
   * Assumes that the device does not support HDR editing for the given {@code colorInfo}.
   *
   * @throws AssumptionViolatedException if the device does support HDR editing.
   */
  public static void assumeDeviceDoesNotSupportHdrEditing(String testId, Format format)
      throws JSONException, IOException {
    checkState(ColorInfo.isTransferHdr(format.colorInfo));
    if (!EncoderUtil.getSupportedEncodersForHdrEditing(format.sampleMimeType, format.colorInfo)
        .isEmpty()) {
      String skipReason =
          "HDR editing supported for sample mime type "
              + format.sampleMimeType
              + " and color info "
              + format.colorInfo;
      recordTestSkipped(getApplicationContext(), testId, skipReason);
      throw new AssumptionViolatedException(skipReason);
    }
  }

  private HdrCapabilitiesUtil() {}
}
