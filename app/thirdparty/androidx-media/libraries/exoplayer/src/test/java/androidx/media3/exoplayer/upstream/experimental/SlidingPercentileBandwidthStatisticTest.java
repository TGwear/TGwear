/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.upstream.experimental;

import static com.google.common.truth.Truth.assertThat;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit tests for {@link SlidingPercentileBandwidthStatistic}. */
@RunWith(AndroidJUnit4.class)
public class SlidingPercentileBandwidthStatisticTest {

  @Test
  public void getBandwidthEstimate_afterConstruction_returnsNoEstimate() {
    SlidingPercentileBandwidthStatistic statistic = new SlidingPercentileBandwidthStatistic();

    assertThat(statistic.getBandwidthEstimate())
        .isEqualTo(BandwidthEstimator.ESTIMATE_NOT_AVAILABLE);
  }

  @Test
  public void getBandwidthEstimate_oneSample_returnsEstimate() {
    SlidingPercentileBandwidthStatistic statistic =
        new SlidingPercentileBandwidthStatistic(/* maxSampleCount= */ 10, /* percentile= */ 0.5);

    statistic.addSample(/* bytes= */ 10, /* durationUs= */ 10);

    assertThat(statistic.getBandwidthEstimate()).isEqualTo(8_000_000);
  }

  @Test
  public void getBandwidthEstimate_multipleSamples_returnsEstimate() {
    SlidingPercentileBandwidthStatistic statistic =
        new SlidingPercentileBandwidthStatistic(/* maxSampleCount= */ 10, /* percentile= */ 0.5);

    // Transfer bytes are chosen so that their weights (square root) is exactly an integer.
    statistic.addSample(/* bytes= */ 400, /* durationUs= */ 10);
    statistic.addSample(/* bytes= */ 100, /* durationUs= */ 10);
    statistic.addSample(/* bytes= */ 64, /* durationUs= */ 10);

    assertThat(statistic.getBandwidthEstimate()).isEqualTo(176_000_000);
  }

  @Test
  public void getBandwidthEstimate_calledMultipleTimes_returnsSameEstimate() {
    SlidingPercentileBandwidthStatistic statistic =
        new SlidingPercentileBandwidthStatistic(/* maxSampleCount= */ 10, /* percentile= */ 0.5);

    // Transfer bytes chosen so that their weight (sqrt) is an integer.
    statistic.addSample(/* bytes= */ 400, /* durationUs= */ 10);
    statistic.addSample(/* bytes= */ 100, /* durationUs= */ 10);
    statistic.addSample(/* bytes= */ 64, /* durationUs= */ 10);

    assertThat(statistic.getBandwidthEstimate()).isEqualTo(176_000_000);
    assertThat(statistic.getBandwidthEstimate()).isEqualTo(176_000_000);
  }

  @Test
  public void getBandwidthEstimate_afterMoreSamplesThanMaxSamples_usesOnlyMaxSamplesForEstimate() {
    SlidingPercentileBandwidthStatistic statistic =
        new SlidingPercentileBandwidthStatistic(/* maxSampleCount= */ 10, /* percentile= */ 0.5);

    // Add 12 samples, the first two should be discarded
    statistic.addSample(/* bytes= */ 1_000, /* durationUs= */ 10);
    statistic.addSample(/* bytes= */ 1_000, /* durationUs= */ 10);
    statistic.addSample(/* bytes= */ 16, /* durationUs= */ 10);
    statistic.addSample(/* bytes= */ 16, /* durationUs= */ 10);
    statistic.addSample(/* bytes= */ 16, /* durationUs= */ 10);
    statistic.addSample(/* bytes= */ 16, /* durationUs= */ 10);
    statistic.addSample(/* bytes= */ 16, /* durationUs= */ 10);
    statistic.addSample(/* bytes= */ 16, /* durationUs= */ 10);
    statistic.addSample(/* bytes= */ 16, /* durationUs= */ 10);
    statistic.addSample(/* bytes= */ 16, /* durationUs= */ 10);
    statistic.addSample(/* bytes= */ 16, /* durationUs= */ 10);
    statistic.addSample(/* bytes= */ 16, /* durationUs= */ 10);

    assertThat(statistic.getBandwidthEstimate()).isEqualTo(12_800_000);
  }

  @Test
  public void getBandwidthEstimate_nonMediaPercentile_returnsEstimate() {
    SlidingPercentileBandwidthStatistic statistic =
        new SlidingPercentileBandwidthStatistic(/* maxSampleCount= */ 10, /* percentile= */ 0.125);

    // Transfer bytes are chosen so that their weights (square root) is exactly an integer.
    statistic.addSample(/* bytes= */ 484, /* durationUs= */ 10);
    statistic.addSample(/* bytes= */ 100, /* durationUs= */ 10);
    statistic.addSample(/* bytes= */ 64, /* durationUs= */ 10);

    assertThat(statistic.getBandwidthEstimate()).isEqualTo(54_400_000);
  }

  @Test
  public void reset_withSamplesAdded_returnsNoEstimate() {
    SlidingPercentileBandwidthStatistic statistic =
        new SlidingPercentileBandwidthStatistic(/* maxSampleCount= */ 10, /* percentile= */ 0.5);

    statistic.addSample(/* bytes= */ 10, /* durationUs= */ 10);
    statistic.addSample(/* bytes= */ 10, /* durationUs= */ 10);
    statistic.reset();

    assertThat(statistic.getBandwidthEstimate())
        .isEqualTo(BandwidthEstimator.ESTIMATE_NOT_AVAILABLE);
  }
}
