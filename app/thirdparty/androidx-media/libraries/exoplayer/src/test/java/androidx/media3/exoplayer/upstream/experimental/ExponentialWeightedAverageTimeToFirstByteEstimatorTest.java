/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.upstream.experimental;

import static androidx.media3.exoplayer.upstream.experimental.ExponentialWeightedAverageTimeToFirstByteEstimator.DEFAULT_SMOOTHING_FACTOR;
import static com.google.common.truth.Truth.assertThat;

import android.net.Uri;
import androidx.media3.common.C;
import androidx.media3.datasource.DataSpec;
import androidx.media3.test.utils.FakeClock;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit test for {@link ExponentialWeightedAverageTimeToFirstByteEstimator}. */
@RunWith(AndroidJUnit4.class)
public class ExponentialWeightedAverageTimeToFirstByteEstimatorTest {

  @Test
  public void timeToFirstByteEstimate_afterConstruction_notAvailable() {
    ExponentialWeightedAverageTimeToFirstByteEstimator estimator =
        new ExponentialWeightedAverageTimeToFirstByteEstimator();

    assertThat(estimator.getTimeToFirstByteEstimateUs()).isEqualTo(C.TIME_UNSET);
  }

  @Test
  public void timeToFirstByteEstimate_afterReset_notAvailable() {
    FakeClock clock = new FakeClock(0);
    ExponentialWeightedAverageTimeToFirstByteEstimator estimator =
        new ExponentialWeightedAverageTimeToFirstByteEstimator(DEFAULT_SMOOTHING_FACTOR, clock);
    DataSpec dataSpec = new DataSpec.Builder().setUri(Uri.EMPTY).build();

    // Initialize and start two transfers.
    estimator.onTransferInitializing(dataSpec);
    clock.advanceTime(10);
    estimator.onTransferStart(dataSpec);
    // Second transfer.
    estimator.onTransferInitializing(dataSpec);
    clock.advanceTime(10);
    estimator.onTransferStart(dataSpec);
    assertThat(estimator.getTimeToFirstByteEstimateUs()).isGreaterThan(0);
    estimator.reset();

    assertThat(estimator.getTimeToFirstByteEstimateUs()).isEqualTo(C.TIME_UNSET);
  }

  @Test
  public void timeToFirstByteEstimate_afterTwoSamples_returnsEstimate() {
    FakeClock clock = new FakeClock(0);
    ExponentialWeightedAverageTimeToFirstByteEstimator estimator =
        new ExponentialWeightedAverageTimeToFirstByteEstimator(DEFAULT_SMOOTHING_FACTOR, clock);
    DataSpec dataSpec = new DataSpec.Builder().setUri(Uri.EMPTY).build();

    // Initialize and start two transfers.
    estimator.onTransferInitializing(dataSpec);
    clock.advanceTime(10);
    estimator.onTransferStart(dataSpec);
    // Second transfer.
    estimator.onTransferInitializing(dataSpec);
    clock.advanceTime(5);
    estimator.onTransferStart(dataSpec);

    // (0.85 * 10ms) + (0.15 * 5ms) = 9.25ms => 9250us
    assertThat(estimator.getTimeToFirstByteEstimateUs()).isEqualTo(9250);
  }

  @Test
  public void timeToFirstByteEstimate_withUserDefinedSmoothingFactor_returnsEstimate() {
    FakeClock clock = new FakeClock(0);
    ExponentialWeightedAverageTimeToFirstByteEstimator estimator =
        new ExponentialWeightedAverageTimeToFirstByteEstimator(/* smoothingFactor= */ 0.9, clock);
    DataSpec dataSpec = new DataSpec.Builder().setUri(Uri.EMPTY).build();

    // Initialize and start two transfers.
    estimator.onTransferInitializing(dataSpec);
    clock.advanceTime(10);
    estimator.onTransferStart(dataSpec);
    // Second transfer.
    estimator.onTransferInitializing(dataSpec);
    clock.advanceTime(5);
    estimator.onTransferStart(dataSpec);

    // (0.9 * 10ms) + (0.1 * 5ms) = 9.5ms => 9500 us
    assertThat(estimator.getTimeToFirstByteEstimateUs()).isEqualTo(9500);
  }
}
