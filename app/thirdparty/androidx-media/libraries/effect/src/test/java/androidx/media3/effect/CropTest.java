/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.effect;

import static com.google.common.truth.Truth.assertThat;

import androidx.media3.common.util.GlUtil;
import androidx.media3.common.util.Size;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Unit tests for {@link Crop}.
 *
 * <p>See {@code CropPixelTest} for pixel tests testing {@link Crop}.
 */
@RunWith(AndroidJUnit4.class)
public final class CropTest {
  @Test
  public void configure_noEdits_leavesFramesUnchanged() {
    int inputWidth = 200;
    int inputHeight = 150;
    Crop crop = new Crop(/* left= */ -1, /* right= */ 1, /* bottom= */ -1, /* top= */ 1);

    Size outputSize = crop.configure(inputWidth, inputHeight);
    boolean isNoOp = crop.isNoOp(inputWidth, inputHeight);

    assertThat(isNoOp).isTrue();
    assertThat(outputSize.getWidth()).isEqualTo(inputWidth);
    assertThat(outputSize.getHeight()).isEqualTo(inputHeight);
  }

  @Test
  public void configure_setCrop_changesDimensions() {
    int inputWidth = 300;
    int inputHeight = 200;
    float left = -0.5f;
    float right = 0.5f;
    float bottom = 0.5f;
    float top = 1f;
    Crop crop = new Crop(left, right, bottom, top);

    Size outputSize = crop.configure(inputWidth, inputHeight);
    boolean isNoOp = crop.isNoOp(inputWidth, inputHeight);

    assertThat(isNoOp).isFalse();
    int expectedPostCropWidth = Math.round(inputWidth * (right - left) / GlUtil.LENGTH_NDC);
    int expectedPostCropHeight = Math.round(inputHeight * (top - bottom) / GlUtil.LENGTH_NDC);
    assertThat(outputSize.getWidth()).isEqualTo(expectedPostCropWidth);
    assertThat(outputSize.getHeight()).isEqualTo(expectedPostCropHeight);
  }
}
