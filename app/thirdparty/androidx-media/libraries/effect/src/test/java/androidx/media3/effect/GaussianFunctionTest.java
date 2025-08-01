/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.effect;

import static com.google.common.truth.Truth.assertThat;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit tests for {@link GaussianFunction}. */
@RunWith(AndroidJUnit4.class)
public class GaussianFunctionTest {

  private final GaussianFunction function =
      new GaussianFunction(/* sigma= */ 2.55f, /* numStandardDeviations= */ 4.45f);

  @Test
  public void value_samplePositionAboveRange_returnsZero() {
    assertThat(function.value(/* samplePosition= */ function.domainEnd() + .00001f)).isEqualTo(0);
  }

  @Test
  public void value_samplePositionBelowRange_returnsZero() {
    assertThat(function.value(/* samplePosition= */ -10000000000000f)).isEqualTo(0);
  }

  @Test
  public void value_samplePositionInRange_returnsSymmetricGaussianFunction() {
    assertThat(function.value(/* samplePosition= */ 9.999f)).isEqualTo(7.1712595E-5f);
    assertThat(function.value(/* samplePosition= */ -9.999f)).isEqualTo(7.1712595E-5f);
  }
}
