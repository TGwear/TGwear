/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common.util;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.RED;
import static android.graphics.Color.WHITE;
import static android.graphics.Color.argb;
import static android.graphics.Color.parseColor;
import static androidx.media3.common.util.ColorParser.parseTtmlColor;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import android.graphics.Color;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit test for {@link ColorParser}. */
@RunWith(AndroidJUnit4.class)
public final class ColorParserTest {

  // Negative tests.

  @Test
  public void parseUnknownColor() {
    assertThrows(
        IllegalArgumentException.class, () -> ColorParser.parseTtmlColor("colorOfAnElectron"));
  }

  @Test
  public void parseNull() {
    assertThrows(IllegalArgumentException.class, () -> ColorParser.parseTtmlColor(null));
  }

  @Test
  public void parseEmpty() {
    assertThrows(IllegalArgumentException.class, () -> ColorParser.parseTtmlColor(""));
  }

  @Test
  public void rgbColorParsingRgbValuesNegative() {
    assertThrows(
        IllegalArgumentException.class, () -> ColorParser.parseTtmlColor("rgb(-4, 55, 209)"));
  }

  // Positive tests.

  @Test
  public void hexCodeParsing() {
    assertThat(parseTtmlColor("#FFFFFF")).isEqualTo(WHITE);
    assertThat(parseTtmlColor("#FFFFFFFF")).isEqualTo(WHITE);
    assertThat(parseTtmlColor("#123456")).isEqualTo(parseColor("#FF123456"));
    // Hex colors in ColorParser are RGBA, where-as {@link Color#parseColor} takes ARGB.
    assertThat(parseTtmlColor("#FFFFFF00")).isEqualTo(parseColor("#00FFFFFF"));
    assertThat(parseTtmlColor("#12345678")).isEqualTo(parseColor("#78123456"));
  }

  @Test
  public void rgbColorParsing() {
    assertThat(parseTtmlColor("rgb(255,255,255)")).isEqualTo(WHITE);
    // Spaces are ignored.
    assertThat(parseTtmlColor("   rgb (      255, 255, 255)")).isEqualTo(WHITE);
  }

  @Test
  public void rgbColorParsingRgbValuesOutOfBounds() {
    int outOfBounds = ColorParser.parseTtmlColor("rgb(999, 999, 999)");
    @SuppressWarnings("Range") // Deliberately testing invalid values
    int color = Color.rgb(999, 999, 999);
    // Behave like the framework does.
    assertThat(outOfBounds).isEqualTo(color);
  }

  @Test
  public void rgbaColorParsing() {
    assertThat(parseTtmlColor("rgba(255,255,255,255)")).isEqualTo(WHITE);
    assertThat(parseTtmlColor("rgba(255,255,255,255)")).isEqualTo(argb(255, 255, 255, 255));
    assertThat(parseTtmlColor("rgba(0, 0, 0, 255)")).isEqualTo(BLACK);
    assertThat(parseTtmlColor("rgba(0, 0, 255, 0)")).isEqualTo(argb(0, 0, 0, 255));
    assertThat(parseTtmlColor("rgba(255, 0, 0, 255)")).isEqualTo(RED);
    assertThat(parseTtmlColor("rgba(255, 0, 255, 0)")).isEqualTo(argb(0, 255, 0, 255));
    assertThat(parseTtmlColor("rgba(255, 0, 0, 205)")).isEqualTo(argb(205, 255, 0, 0));
  }
}
