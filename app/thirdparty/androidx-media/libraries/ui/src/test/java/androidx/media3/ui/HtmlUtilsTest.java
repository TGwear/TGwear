/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.ui;

import static com.google.common.truth.Truth.assertThat;

import android.graphics.Color;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Tests for {@link HtmlUtils}. */
@RunWith(AndroidJUnit4.class)
public class HtmlUtilsTest {

  @Test
  public void toCssRgba_exactAlpha() {
    String cssRgba = HtmlUtils.toCssRgba(Color.argb(51, 13, 23, 37));
    assertThat(cssRgba).isEqualTo("rgba(13,23,37,0.200)");
  }

  @Test
  public void toCssRgba_truncatedAlpha() {
    String cssRgba = HtmlUtils.toCssRgba(Color.argb(100, 13, 23, 37));
    assertThat(cssRgba).isEqualTo("rgba(13,23,37,0.392)");
  }
}
