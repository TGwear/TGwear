/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.upstream.experimental;

import static androidx.media3.exoplayer.upstream.experimental.BandwidthEstimator.ESTIMATE_NOT_AVAILABLE;

import androidx.media3.common.util.UnstableApi;

/** A {@link BandwidthStatistic} that calculates estimates using an exponential weighted average. */
@UnstableApi
public class ExponentialWeightedAverageStatistic implements BandwidthStatistic {

  /** The default smoothing factor. */
  public static final double DEFAULT_SMOOTHING_FACTOR = 0.9999;

  private final double smoothingFactor;

  private long bitrateEstimate;

  /** Creates an instance with {@link #DEFAULT_SMOOTHING_FACTOR}. */
  public ExponentialWeightedAverageStatistic() {
    this(DEFAULT_SMOOTHING_FACTOR);
  }

  /**
   * Creates an instance.
   *
   * @param smoothingFactor The exponential smoothing factor.
   */
  public ExponentialWeightedAverageStatistic(double smoothingFactor) {
    this.smoothingFactor = smoothingFactor;
    bitrateEstimate = ESTIMATE_NOT_AVAILABLE;
  }

  @Override
  public void addSample(long bytes, long durationUs) {
    long bitrate = bytes * 8_000_000 / durationUs;
    if (bitrateEstimate == ESTIMATE_NOT_AVAILABLE) {
      bitrateEstimate = bitrate;
      return;
    }
    // Weight smoothing factor by sqrt(bytes).
    double factor = Math.pow(smoothingFactor, Math.sqrt((double) bytes));
    bitrateEstimate = (long) (factor * bitrateEstimate + (1f - factor) * bitrate);
  }

  @Override
  public long getBandwidthEstimate() {
    return bitrateEstimate;
  }

  @Override
  public void reset() {
    bitrateEstimate = ESTIMATE_NOT_AVAILABLE;
  }
}
