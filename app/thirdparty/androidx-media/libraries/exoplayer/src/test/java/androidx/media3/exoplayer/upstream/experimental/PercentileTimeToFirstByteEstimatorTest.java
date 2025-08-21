/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.upstream.experimental;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import android.net.Uri;
import androidx.media3.common.C;
import androidx.media3.datasource.DataSpec;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import java.time.Duration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.shadows.ShadowSystemClock;

/** Unit tests for {@link PercentileTimeToFirstByteEstimator}. */
@RunWith(AndroidJUnit4.class)
public class PercentileTimeToFirstByteEstimatorTest {

  private PercentileTimeToFirstByteEstimator percentileTimeToResponseEstimator;

  @Before
  public void setUp() {
    percentileTimeToResponseEstimator =
        new PercentileTimeToFirstByteEstimator(/* numberOfSamples= */ 5, /* percentile= */ 0.5f);
  }

  @Test
  public void constructor_invalidNumberOfSamples_throwsIllegalArgumentException() {
    assertThrows(
        IllegalArgumentException.class,
        () ->
            new PercentileTimeToFirstByteEstimator(
                /* numberOfSamples= */ 0, /* percentile= */ .2f));
    assertThrows(
        IllegalArgumentException.class,
        () ->
            new PercentileTimeToFirstByteEstimator(
                /* numberOfSamples= */ -123, /* percentile= */ .2f));
  }

  @Test
  public void constructor_invalidPercentile_throwsIllegalArgumentException() {
    assertThrows(
        IllegalArgumentException.class,
        () ->
            new PercentileTimeToFirstByteEstimator(
                /* numberOfSamples= */ 11, /* percentile= */ .0f));
    assertThrows(
        IllegalArgumentException.class,
        () ->
            new PercentileTimeToFirstByteEstimator(
                /* numberOfSamples= */ 11, /* percentile= */ 1.1f));
  }

  @Test
  public void getTimeToRespondEstimateUs_noSamples_returnsTimeUnset() {
    assertThat(percentileTimeToResponseEstimator.getTimeToFirstByteEstimateUs())
        .isEqualTo(C.TIME_UNSET);
  }

  @Test
  public void getTimeToRespondEstimateUs_medianOfOddNumberOfSamples_returnsCenterSampleValue() {
    DataSpec dataSpec = new DataSpec(Uri.EMPTY);

    percentileTimeToResponseEstimator.onTransferInitializing(dataSpec);
    ShadowSystemClock.advanceBy(Duration.ofMillis(10));
    percentileTimeToResponseEstimator.onTransferStart(dataSpec);
    percentileTimeToResponseEstimator.onTransferInitializing(dataSpec);
    ShadowSystemClock.advanceBy(Duration.ofMillis(20));
    percentileTimeToResponseEstimator.onTransferStart(dataSpec);
    percentileTimeToResponseEstimator.onTransferInitializing(dataSpec);
    ShadowSystemClock.advanceBy(Duration.ofMillis(30));
    percentileTimeToResponseEstimator.onTransferStart(dataSpec);
    percentileTimeToResponseEstimator.onTransferInitializing(dataSpec);
    ShadowSystemClock.advanceBy(Duration.ofMillis(40));
    percentileTimeToResponseEstimator.onTransferStart(dataSpec);
    percentileTimeToResponseEstimator.onTransferInitializing(dataSpec);
    ShadowSystemClock.advanceBy(Duration.ofMillis(50));
    percentileTimeToResponseEstimator.onTransferStart(dataSpec);

    assertThat(percentileTimeToResponseEstimator.getTimeToFirstByteEstimateUs()).isEqualTo(30_000);
  }

  @Test
  public void
      getTimeToRespondEstimateUs_medianOfEvenNumberOfSamples_returnsLastSampleOfFirstHalfValue() {
    PercentileTimeToFirstByteEstimator percentileTimeToResponseEstimator =
        new PercentileTimeToFirstByteEstimator(/* numberOfSamples= */ 12, /* percentile= */ 0.5f);
    DataSpec dataSpec = new DataSpec(Uri.EMPTY);

    percentileTimeToResponseEstimator.onTransferInitializing(dataSpec);
    ShadowSystemClock.advanceBy(Duration.ofMillis(10));
    percentileTimeToResponseEstimator.onTransferStart(dataSpec);
    percentileTimeToResponseEstimator.onTransferInitializing(dataSpec);
    ShadowSystemClock.advanceBy(Duration.ofMillis(20));
    percentileTimeToResponseEstimator.onTransferStart(dataSpec);
    percentileTimeToResponseEstimator.onTransferInitializing(dataSpec);
    ShadowSystemClock.advanceBy(Duration.ofMillis(30));
    percentileTimeToResponseEstimator.onTransferStart(dataSpec);
    percentileTimeToResponseEstimator.onTransferInitializing(dataSpec);
    ShadowSystemClock.advanceBy(Duration.ofMillis(40));
    percentileTimeToResponseEstimator.onTransferStart(dataSpec);

    assertThat(percentileTimeToResponseEstimator.getTimeToFirstByteEstimateUs()).isEqualTo(20_000);
  }

  @Test
  public void getTimeToRespondEstimateUs_slidingMedian_returnsCenterSampleValue() {
    DataSpec dataSpec = new DataSpec(Uri.EMPTY);

    percentileTimeToResponseEstimator.onTransferInitializing(dataSpec);
    ShadowSystemClock.advanceBy(Duration.ofMillis(10));
    percentileTimeToResponseEstimator.onTransferStart(dataSpec);
    percentileTimeToResponseEstimator.onTransferInitializing(dataSpec);
    ShadowSystemClock.advanceBy(Duration.ofMillis(20));
    percentileTimeToResponseEstimator.onTransferStart(dataSpec);
    percentileTimeToResponseEstimator.onTransferInitializing(dataSpec);
    ShadowSystemClock.advanceBy(Duration.ofMillis(30));
    percentileTimeToResponseEstimator.onTransferStart(dataSpec);
    percentileTimeToResponseEstimator.onTransferInitializing(dataSpec);
    ShadowSystemClock.advanceBy(Duration.ofMillis(40));
    percentileTimeToResponseEstimator.onTransferStart(dataSpec);
    percentileTimeToResponseEstimator.onTransferInitializing(dataSpec);
    ShadowSystemClock.advanceBy(Duration.ofMillis(50));
    percentileTimeToResponseEstimator.onTransferStart(dataSpec);
    percentileTimeToResponseEstimator.onTransferInitializing(dataSpec);
    ShadowSystemClock.advanceBy(Duration.ofMillis(60));
    percentileTimeToResponseEstimator.onTransferStart(dataSpec);
    percentileTimeToResponseEstimator.onTransferInitializing(dataSpec);
    ShadowSystemClock.advanceBy(Duration.ofMillis(70));
    percentileTimeToResponseEstimator.onTransferStart(dataSpec);

    assertThat(percentileTimeToResponseEstimator.getTimeToFirstByteEstimateUs()).isEqualTo(50_000);
  }

  @Test
  public void reset_clearsTheSlidingWindows() {
    DataSpec dataSpec = new DataSpec(Uri.EMPTY);
    percentileTimeToResponseEstimator.onTransferInitializing(dataSpec);
    ShadowSystemClock.advanceBy(Duration.ofMillis(10));
    percentileTimeToResponseEstimator.onTransferStart(dataSpec);
    percentileTimeToResponseEstimator.onTransferInitializing(dataSpec);
    ShadowSystemClock.advanceBy(Duration.ofMillis(10));
    percentileTimeToResponseEstimator.onTransferStart(dataSpec);

    percentileTimeToResponseEstimator.reset();

    assertThat(percentileTimeToResponseEstimator.getTimeToFirstByteEstimateUs())
        .isEqualTo(C.TIME_UNSET);
  }
}
