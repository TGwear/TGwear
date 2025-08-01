/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.drm;

import androidx.media3.common.util.Log;
import androidx.media3.common.util.Util;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/** Utility methods for ClearKey. */
/* package */ final class ClearKeyUtil {

  private static final String TAG = "ClearKeyUtil";

  private ClearKeyUtil() {}

  /**
   * Adjusts ClearKey request data obtained from the Android ClearKey CDM to be spec compliant.
   *
   * @param request The request data.
   * @return The adjusted request data.
   */
  public static byte[] adjustRequestData(byte[] request) {
    if (Util.SDK_INT >= 27) {
      return request;
    }
    // Prior to O-MR1 the ClearKey CDM encoded the values in the "kids" array using Base64 encoding
    // rather than Base64Url encoding. See [Internal: b/64388098]. We know the exact request format
    // from the platform's InitDataParser.cpp. Since there aren't any "+" or "/" symbols elsewhere
    // in the request, it's safe to fix the encoding by replacement through the whole request.
    String requestString = Util.fromUtf8Bytes(request);
    return Util.getUtf8Bytes(base64ToBase64Url(requestString));
  }

  /**
   * Adjusts ClearKey response data to be suitable for providing to the Android ClearKey CDM.
   *
   * @param response The response data.
   * @return The adjusted response data.
   */
  public static byte[] adjustResponseData(byte[] response) {
    if (Util.SDK_INT >= 27) {
      return response;
    }
    // Prior to O-MR1 the ClearKey CDM expected Base64 encoding rather than Base64Url encoding for
    // the "k" and "kid" strings. See [Internal: b/64388098]. We know that the ClearKey CDM only
    // looks at the k, kid and kty parameters in each key, so can ignore the rest of the response.
    try {
      JSONObject responseJson = new JSONObject(Util.fromUtf8Bytes(response));
      StringBuilder adjustedResponseBuilder = new StringBuilder("{\"keys\":[");
      JSONArray keysArray = responseJson.getJSONArray("keys");
      for (int i = 0; i < keysArray.length(); i++) {
        if (i != 0) {
          adjustedResponseBuilder.append(",");
        }
        JSONObject key = keysArray.getJSONObject(i);
        adjustedResponseBuilder.append("{\"k\":\"");
        adjustedResponseBuilder.append(base64UrlToBase64(key.getString("k")));
        adjustedResponseBuilder.append("\",\"kid\":\"");
        adjustedResponseBuilder.append(base64UrlToBase64(key.getString("kid")));
        adjustedResponseBuilder.append("\",\"kty\":\"");
        adjustedResponseBuilder.append(key.getString("kty"));
        adjustedResponseBuilder.append("\"}");
      }
      adjustedResponseBuilder.append("]}");
      return Util.getUtf8Bytes(adjustedResponseBuilder.toString());
    } catch (JSONException e) {
      Log.e(TAG, "Failed to adjust response data: " + Util.fromUtf8Bytes(response), e);
      return response;
    }
  }

  private static String base64ToBase64Url(String base64) {
    return base64.replace('+', '-').replace('/', '_');
  }

  private static String base64UrlToBase64(String base64Url) {
    return base64Url.replace('-', '+').replace('_', '/');
  }
}
