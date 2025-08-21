/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.datasource;

import static androidx.media3.datasource.HttpUtil.buildRangeRequestHeader;
import static androidx.media3.datasource.HttpUtil.getContentLength;
import static androidx.media3.datasource.HttpUtil.getDocumentSize;
import static com.google.common.truth.Truth.assertThat;

import androidx.media3.common.C;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit tests for {@link DefaultHttpDataSource}. */
@RunWith(AndroidJUnit4.class)
public class HttpUtilTest {

  @Test
  public void buildRangeRequestHeader_buildsHeader() {
    assertThat(buildRangeRequestHeader(0, C.LENGTH_UNSET)).isNull();
    assertThat(buildRangeRequestHeader(1, C.LENGTH_UNSET)).isEqualTo("bytes=1-");
    assertThat(buildRangeRequestHeader(0, 5)).isEqualTo("bytes=0-4");
    assertThat(buildRangeRequestHeader(5, 15)).isEqualTo("bytes=5-19");
  }

  @Test
  public void getContentLength_bothHeadersMissing_returnsUnset() {
    assertThat(getContentLength(null, null)).isEqualTo(C.LENGTH_UNSET);
    assertThat(getContentLength("", "")).isEqualTo(C.LENGTH_UNSET);
  }

  @Test
  public void getContentLength_onlyContentLengthHeaderSet_returnsCorrectValue() {
    assertThat(getContentLength("5", null)).isEqualTo(5);
    assertThat(getContentLength("5", "")).isEqualTo(5);
  }

  @Test
  public void getContentLength_onlyContentRangeHeaderSet_returnsCorrectValue() {
    assertThat(getContentLength(null, "bytes 5-9/100")).isEqualTo(5);
    assertThat(getContentLength("", "bytes 5-9/100")).isEqualTo(5);
    assertThat(getContentLength("", "bytes 5-9/*")).isEqualTo(5);
  }

  @Test
  public void getContentLength_bothHeadersSet_returnsCorrectValue() {
    assertThat(getContentLength("5", "bytes 5-9/100")).isEqualTo(5);
  }

  @Test
  public void getContentLength_headersInconsistent_returnsLargerValue() {
    assertThat(getContentLength("10", "bytes 0-4/100")).isEqualTo(10);
    assertThat(getContentLength("5", "bytes 0-9/100")).isEqualTo(10);
  }

  @Test
  public void getContentLength_ignoresInvalidValues() {
    assertThat(getContentLength("Invalid", "Invalid")).isEqualTo(C.LENGTH_UNSET);
    assertThat(getContentLength("Invalid", "bytes 5-9/100")).isEqualTo(5);
    assertThat(getContentLength("5", "Invalid")).isEqualTo(5);
  }

  @Test
  public void getContentLength_ignoresUnhandledRangeUnits() {
    assertThat(getContentLength(null, "unhandled 5-9/100")).isEqualTo(C.LENGTH_UNSET);
    assertThat(getContentLength("10", "unhandled 0-4/100")).isEqualTo(10);
  }

  @Test
  public void getDocumentSize_noHeader_returnsUnset() {
    assertThat(getDocumentSize(null)).isEqualTo(C.LENGTH_UNSET);
    assertThat(getDocumentSize("")).isEqualTo(C.LENGTH_UNSET);
  }

  @Test
  public void getDocumentSize_returnsSize() {
    assertThat(getDocumentSize("bytes */20")).isEqualTo(20);
    assertThat(getDocumentSize("bytes 0-4/20")).isEqualTo(20);
  }

  @Test
  public void getDocumentSize_ignoresUnhandledRangeUnits() {
    assertThat(getDocumentSize("unhandled */20")).isEqualTo(C.LENGTH_UNSET);
    assertThat(getDocumentSize("unhandled 0-4/20")).isEqualTo(C.LENGTH_UNSET);
  }
}
