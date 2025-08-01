/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.effect;

import static com.google.common.truth.Truth.assertThat;

import androidx.media3.common.util.Size;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Unit tests for {@link ScaleAndRotateTransformation}.
 *
 * <p>See {@code DefaultShaderProgramPixelTest} for pixel tests testing {@link DefaultShaderProgram}
 * given a transformation matrix.
 */
@RunWith(AndroidJUnit4.class)
public final class ScaleAndRotateTransformationTest {

  @Test
  public void configure_noEdits_leavesFramesUnchanged() {
    int inputWidth = 200;
    int inputHeight = 150;
    ScaleAndRotateTransformation scaleAndRotateTransformation =
        new ScaleAndRotateTransformation.Builder().build();

    Size outputSize = scaleAndRotateTransformation.configure(inputWidth, inputHeight);
    boolean isNoOp = scaleAndRotateTransformation.isNoOp(inputWidth, inputHeight);

    assertThat(isNoOp).isTrue();
    assertThat(outputSize.getWidth()).isEqualTo(inputWidth);
    assertThat(outputSize.getHeight()).isEqualTo(inputHeight);
  }

  @Test
  public void configure_scaleNarrow_decreasesWidth() {
    int inputWidth = 200;
    int inputHeight = 150;
    ScaleAndRotateTransformation scaleAndRotateTransformation =
        new ScaleAndRotateTransformation.Builder()
            .setScale(/* scaleX= */ 0.5f, /* scaleY= */ 1f)
            .build();

    Size outputSize = scaleAndRotateTransformation.configure(inputWidth, inputHeight);
    boolean isNoOp = scaleAndRotateTransformation.isNoOp(inputWidth, inputHeight);

    assertThat(isNoOp).isFalse();
    assertThat(outputSize.getWidth()).isEqualTo(Math.round(inputWidth * 0.5f));
    assertThat(outputSize.getHeight()).isEqualTo(inputHeight);
  }

  @Test
  public void configure_scaleWide_increasesWidth() {
    int inputWidth = 200;
    int inputHeight = 150;
    ScaleAndRotateTransformation scaleAndRotateTransformation =
        new ScaleAndRotateTransformation.Builder()
            .setScale(/* scaleX= */ 2f, /* scaleY= */ 1f)
            .build();

    Size outputSize = scaleAndRotateTransformation.configure(inputWidth, inputHeight);
    boolean isNoOp = scaleAndRotateTransformation.isNoOp(inputWidth, inputHeight);

    assertThat(isNoOp).isFalse();
    assertThat(outputSize.getWidth()).isEqualTo(inputWidth * 2);
    assertThat(outputSize.getHeight()).isEqualTo(inputHeight);
  }

  @Test
  public void configure_scaleTall_increasesHeight() {
    int inputWidth = 200;
    int inputHeight = 150;
    ScaleAndRotateTransformation scaleAndRotateTransformation =
        new ScaleAndRotateTransformation.Builder()
            .setScale(/* scaleX= */ 1f, /* scaleY= */ 2f)
            .build();

    Size outputSize = scaleAndRotateTransformation.configure(inputWidth, inputHeight);
    boolean isNoOp = scaleAndRotateTransformation.isNoOp(inputWidth, inputHeight);

    assertThat(isNoOp).isFalse();
    assertThat(outputSize.getWidth()).isEqualTo(inputWidth);
    assertThat(outputSize.getHeight()).isEqualTo(inputHeight * 2);
  }

  @Test
  public void configure_rotate90_swapsDimensions() {
    int inputWidth = 200;
    int inputHeight = 150;
    ScaleAndRotateTransformation scaleAndRotateTransformation =
        new ScaleAndRotateTransformation.Builder().setRotationDegrees(90).build();

    Size outputSize = scaleAndRotateTransformation.configure(inputWidth, inputHeight);
    boolean isNoOp = scaleAndRotateTransformation.isNoOp(inputWidth, inputHeight);

    assertThat(isNoOp).isFalse();
    assertThat(outputSize.getWidth()).isEqualTo(inputHeight);
    assertThat(outputSize.getHeight()).isEqualTo(inputWidth);
  }

  @Test
  public void configure_rotate45_changesDimensions() {
    int inputWidth = 200;
    int inputHeight = 150;
    ScaleAndRotateTransformation scaleAndRotateTransformation =
        new ScaleAndRotateTransformation.Builder().setRotationDegrees(45).build();
    long expectedOutputWidthHeight = 247;

    Size outputSize = scaleAndRotateTransformation.configure(inputWidth, inputHeight);
    boolean isNoOp = scaleAndRotateTransformation.isNoOp(inputWidth, inputHeight);

    assertThat(isNoOp).isFalse();
    assertThat(outputSize.getWidth()).isEqualTo(expectedOutputWidthHeight);
    assertThat(outputSize.getHeight()).isEqualTo(expectedOutputWidthHeight);
  }

  @Test
  public void setRotation_setsRotationBetween0and360() {
    ScaleAndRotateTransformation scaleAndRotateTransformation =
        new ScaleAndRotateTransformation.Builder().setRotationDegrees(90).build();
    assertThat(scaleAndRotateTransformation.rotationDegrees).isEqualTo(90);

    scaleAndRotateTransformation =
        new ScaleAndRotateTransformation.Builder().setRotationDegrees(90 + 360).build();
    assertThat(scaleAndRotateTransformation.rotationDegrees).isEqualTo(90);

    scaleAndRotateTransformation =
        new ScaleAndRotateTransformation.Builder().setRotationDegrees(-90).build();
    assertThat(scaleAndRotateTransformation.rotationDegrees).isEqualTo(270);

    scaleAndRotateTransformation =
        new ScaleAndRotateTransformation.Builder().setRotationDegrees(-90 - 360).build();
    assertThat(scaleAndRotateTransformation.rotationDegrees).isEqualTo(270);

    scaleAndRotateTransformation =
        new ScaleAndRotateTransformation.Builder().setRotationDegrees(360).build();
    assertThat(scaleAndRotateTransformation.rotationDegrees).isEqualTo(0);
  }
}
