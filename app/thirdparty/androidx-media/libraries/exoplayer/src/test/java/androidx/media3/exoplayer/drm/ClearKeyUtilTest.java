/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.drm;

import static com.google.common.truth.Truth.assertThat;

import androidx.media3.common.util.Util;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

/** Unit test for {@link ClearKeyUtil}. */
@RunWith(AndroidJUnit4.class)
public final class ClearKeyUtilTest {

  private static final byte[] SINGLE_KEY_RESPONSE =
      Util.getUtf8Bytes(
          "{"
              + "\"keys\":["
              + "{"
              + "\"k\":\"abc_def-\","
              + "\"kid\":\"ab_cde-f\","
              + "\"kty\":\"o_c-t\","
              + "\"ignored\":\"ignored\""
              + "}"
              + "],"
              + "\"ignored\":\"ignored\""
              + "}");
  private static final byte[] MULTI_KEY_RESPONSE =
      Util.getUtf8Bytes(
          "{"
              + "\"keys\":["
              + "{"
              + "\"k\":\"abc_def-\","
              + "\"kid\":\"ab_cde-f\","
              + "\"kty\":\"oct\","
              + "\"ignored\":\"ignored\""
              + "},{"
              + "\"k\":\"ghi_jkl-\","
              + "\"kid\":\"gh_ijk-l\","
              + "\"kty\":\"oct\""
              + "}"
              + "],"
              + "\"ignored\":\"ignored\""
              + "}");
  private static final byte[] KEY_REQUEST =
      Util.getUtf8Bytes(
          "{"
              + "\"kids\":["
              + "\"abc+def/\","
              + "\"ab+cde/f\""
              + "],"
              + "\"type\":\"temporary\""
              + "}");

  @Config(sdk = 26)
  @Test
  public void adjustSingleKeyResponseDataV26() {
    // Everything but the keys should be removed. Within each key only the k, kid and kty parameters
    // should remain. Any "-" and "_" characters in the k and kid values should be replaced with "+"
    // and "/".
    byte[] expected =
        Util.getUtf8Bytes(
            "{"
                + "\"keys\":["
                + "{"
                + "\"k\":\"abc/def+\",\"kid\":\"ab/cde+f\",\"kty\":\"o_c-t\""
                + "}"
                + "]"
                + "}");
    assertThat(ClearKeyUtil.adjustResponseData(SINGLE_KEY_RESPONSE)).isEqualTo(expected);
  }

  @Config(sdk = 26)
  @Test
  public void adjustMultiKeyResponseDataV26() {
    // Everything but the keys should be removed. Within each key only the k, kid and kty parameters
    // should remain. Any "-" and "_" characters in the k and kid values should be replaced with "+"
    // and "/".
    byte[] expected =
        Util.getUtf8Bytes(
            "{"
                + "\"keys\":["
                + "{"
                + "\"k\":\"abc/def+\",\"kid\":\"ab/cde+f\",\"kty\":\"oct\""
                + "},{"
                + "\"k\":\"ghi/jkl+\",\"kid\":\"gh/ijk+l\",\"kty\":\"oct\""
                + "}"
                + "]"
                + "}");
    assertThat(ClearKeyUtil.adjustResponseData(MULTI_KEY_RESPONSE)).isEqualTo(expected);
  }

  @Config(sdk = 27)
  @Test
  public void adjustResponseDataV27() {
    // Response should be unchanged.
    assertThat(ClearKeyUtil.adjustResponseData(SINGLE_KEY_RESPONSE)).isEqualTo(SINGLE_KEY_RESPONSE);
  }

  @Config(sdk = 26)
  @Test
  public void adjustRequestDataV26() {
    // We expect "+" and "/" to be replaced with "-" and "_" respectively, for "kids".
    byte[] expected =
        Util.getUtf8Bytes(
            "{"
                + "\"kids\":["
                + "\"abc-def_\","
                + "\"ab-cde_f\""
                + "],"
                + "\"type\":\"temporary\""
                + "}");
    assertThat(ClearKeyUtil.adjustRequestData(KEY_REQUEST)).isEqualTo(expected);
  }

  @Config(sdk = 27)
  @Test
  public void adjustRequestDataV27() {
    // Request should be unchanged.
    assertThat(ClearKeyUtil.adjustRequestData(KEY_REQUEST)).isEqualTo(KEY_REQUEST);
  }
}
