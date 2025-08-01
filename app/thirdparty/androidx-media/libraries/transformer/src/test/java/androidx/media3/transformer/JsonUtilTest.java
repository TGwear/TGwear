/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.transformer;

import static com.google.common.truth.Truth.assertThat;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit tests for {@link JsonUtil} */
@RunWith(AndroidJUnit4.class)
public class JsonUtilTest {

  @Test
  public void exceptionAsJsonObjectNull() throws JSONException {
    assertThat(JsonUtil.exceptionAsJsonObject(null)).isNull();
  }

  @Test
  public void exceptionAsJsonObject_simpleRuntimeException_doesNotThrow() throws JSONException {
    RuntimeException exception = new RuntimeException();

    JSONObject jsonObject = JsonUtil.exceptionAsJsonObject(exception);

    assertThat(jsonObject.length()).isEqualTo(2);
    assertThat(jsonObject.get("type")).isEqualTo(exception.getClass());
    // Stack trace should contain the name of this method
    assertThat(jsonObject.get("stackTrace").toString())
        .contains("exceptionAsJsonObject_simpleRuntimeException_doesNotThrow");
  }

  @Test
  public void exceptionAsJsonObject_runtimeExceptionWithMessage_doesNotThrow()
      throws JSONException {
    RuntimeException exception = new RuntimeException("JsonUtilTest");

    JSONObject jsonObject = JsonUtil.exceptionAsJsonObject(exception);

    assertThat(jsonObject.length()).isEqualTo(3);
    assertThat(jsonObject.get("type")).isEqualTo(exception.getClass());
    assertThat(jsonObject.get("message")).isEqualTo("JsonUtilTest");
    // Stack trace should contain the name of this method
    assertThat(jsonObject.get("stackTrace").toString())
        .contains("exceptionAsJsonObject_runtimeExceptionWithMessage_doesNotThrow");
  }

  @Test
  public void exceptionAsJsonObject_exportException_doesNotThrow() throws JSONException {
    RuntimeException innerException = new RuntimeException();
    ExportException exception =
        ExportException.createForMuxer(innerException, ExportException.ERROR_CODE_MUXING_TIMEOUT);

    JSONObject jsonObject = JsonUtil.exceptionAsJsonObject(exception);

    assertThat(jsonObject.length()).isEqualTo(4);
    assertThat(jsonObject.get("type")).isEqualTo(exception.getClass());
    assertThat(jsonObject.get("message")).isEqualTo("Muxer error");
    assertThat(jsonObject.get("errorCode")).isEqualTo(ExportException.ERROR_CODE_MUXING_TIMEOUT);
    // Stack trace should contain the name of this method
    assertThat(jsonObject.get("stackTrace").toString())
        .contains("exceptionAsJsonObject_exportException_doesNotThrow");
  }

  @Test
  public void getDeviceDetails_keys() throws JSONException {
    JSONObject jsonObject = JsonUtil.getDeviceDetailsAsJsonObject();

    assertThat(jsonObject.length()).isEqualTo(4);
    assertThat(jsonObject.has("manufacturer")).isTrue();
    assertThat(jsonObject.has("model")).isTrue();
    assertThat(jsonObject.has("sdkVersion")).isTrue();
    assertThat(jsonObject.has("fingerprint")).isTrue();
  }
}
