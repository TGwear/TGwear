/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.transformer;

import static com.google.common.truth.Truth.assertThat;

import androidx.media3.common.audio.ChannelMixingMatrix;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit tests for {@link ChannelMixingMatrix}. */
@RunWith(AndroidJUnit4.class)
public class ChannelMixingMatrixTest {

  @Test
  public void onesOnDiagonal_1To1_hasCorrectProperties() {
    int inputCount = 1;
    int outputCount = 1;
    float[] coefficients = new float[] {1f};
    ChannelMixingMatrix matrix = new ChannelMixingMatrix(inputCount, outputCount, coefficients);
    assertThat(matrix.isZero()).isFalse();
    assertThat(matrix.isSquare()).isTrue();
    assertThat(matrix.isDiagonal()).isTrue();
    assertThat(matrix.isIdentity()).isTrue();
  }

  @Test
  public void onesOnDiagonal_2To3_hasCorrectProperties() {
    int inputCount = 2;
    int outputCount = 3;
    float[] coefficients =
        new float[] {
          1f, 0f, 0f,
          0f, 1f, 0f,
        };
    ChannelMixingMatrix matrix = new ChannelMixingMatrix(inputCount, outputCount, coefficients);
    assertThat(matrix.isZero()).isFalse();
    assertThat(matrix.isSquare()).isFalse();
    assertThat(matrix.isDiagonal()).isFalse();
    assertThat(matrix.isIdentity()).isFalse();
  }

  @Test
  public void onesOnDiagonal_3To3_hasCorrectProperties() {
    int inputCount = 3;
    int outputCount = 3;
    float[] coefficients =
        new float[] {
          1f, 0f, 0f,
          0f, 1f, 0f,
          0f, 0f, 1f
        };
    ChannelMixingMatrix matrix = new ChannelMixingMatrix(inputCount, outputCount, coefficients);
    assertThat(matrix.isZero()).isFalse();
    assertThat(matrix.isSquare()).isTrue();
    assertThat(matrix.isDiagonal()).isTrue();
    assertThat(matrix.isIdentity()).isTrue();
  }

  @Test
  public void allZeroValues_3To2_hasCorrectProperties() {
    int inputCount = 3;
    int outputCount = 2;
    float[] coefficients =
        new float[] {
          0f, 0f,
          0f, 0f,
          0f, 0f,
        };

    ChannelMixingMatrix matrix = new ChannelMixingMatrix(inputCount, outputCount, coefficients);
    assertThat(matrix.isZero()).isTrue();
    assertThat(matrix.isSquare()).isFalse();
    assertThat(matrix.isDiagonal()).isFalse();
    assertThat(matrix.isIdentity()).isFalse();
  }

  @Test
  public void allZeroValues_3To3_hasCorrectProperties() {
    int inputCount = 3;
    int outputCount = 3;
    float[] coefficients =
        new float[] {
          0f, 0f, 0f,
          0f, 0f, 0f,
          0f, 0f, 0f,
        };

    ChannelMixingMatrix matrix = new ChannelMixingMatrix(inputCount, outputCount, coefficients);
    assertThat(matrix.isZero()).isTrue();
    assertThat(matrix.isSquare()).isTrue();
    assertThat(matrix.isDiagonal()).isTrue();
    assertThat(matrix.isIdentity()).isFalse();
  }

  @Test
  public void allZeroValues_3To4_hasCorrectProperties() {
    int inputCount = 3;
    int outputCount = 4;
    float[] coefficients =
        new float[] {
          0f, 0f, 0f, 0f,
          0f, 0f, 0f, 0f,
          0f, 0f, 0f, 0f,
        };

    ChannelMixingMatrix matrix = new ChannelMixingMatrix(inputCount, outputCount, coefficients);
    assertThat(matrix.isZero()).isTrue();
    assertThat(matrix.isSquare()).isFalse();
    assertThat(matrix.isDiagonal()).isFalse();
    assertThat(matrix.isIdentity()).isFalse();
  }

  @Test
  public void oneNonZeroValue_3To4_hasCorrectProperties() {
    int inputCount = 3;
    int outputCount = 4;
    float[] coefficients =
        new float[] {
          0f, 0f, 0f, 0f,
          0f, 0f, 0f, 0.2f,
          0f, 0f, 0f, 0f,
        };

    ChannelMixingMatrix matrix = new ChannelMixingMatrix(inputCount, outputCount, coefficients);
    assertThat(matrix.isZero()).isFalse();
    assertThat(matrix.isSquare()).isFalse();
    assertThat(matrix.isDiagonal()).isFalse();
    assertThat(matrix.isIdentity()).isFalse();
  }

  @Test
  public void zeroValuesOnDiagonal_2To2_hasCorrectProperties() {
    int inputCount = 2;
    int outputCount = 2;
    float[] coefficients =
        new float[] {
          0f, 1f,
          2f, 0f,
        };

    ChannelMixingMatrix matrix = new ChannelMixingMatrix(inputCount, outputCount, coefficients);
    assertThat(matrix.isZero()).isFalse();
    assertThat(matrix.isSquare()).isTrue();
    assertThat(matrix.isDiagonal()).isFalse();
    assertThat(matrix.isIdentity()).isFalse();
  }

  @Test
  public void nonZeroValuesOnDiagonal_4To4_hasCorrectProperties() {
    int inputCount = 4;
    int outputCount = 4;
    float[] coefficients =
        new float[] {
          1f, 0f, 0f, 0f,
          0f, 2f, 0f, 0f,
          0f, 0f, 3f, 0f,
          0f, 0f, 0f, 0f,
        };

    ChannelMixingMatrix matrix = new ChannelMixingMatrix(inputCount, outputCount, coefficients);
    assertThat(matrix.isZero()).isFalse();
    assertThat(matrix.isSquare()).isTrue();
    assertThat(matrix.isDiagonal()).isTrue();
    assertThat(matrix.isIdentity()).isFalse();
  }

  @Test
  public void allNonZeroValues_2To4_hasCorrectProperties() {
    int inputCount = 2;
    int outputCount = 4;
    float[] coefficients =
        new float[] {
          1f, 3f, 5f, 10f,
          4f, 2f, 9f, 123f,
        };

    ChannelMixingMatrix matrix = new ChannelMixingMatrix(inputCount, outputCount, coefficients);
    assertThat(matrix.isZero()).isFalse();
    assertThat(matrix.isSquare()).isFalse();
    assertThat(matrix.isDiagonal()).isFalse();
    assertThat(matrix.isIdentity()).isFalse();
  }
}
