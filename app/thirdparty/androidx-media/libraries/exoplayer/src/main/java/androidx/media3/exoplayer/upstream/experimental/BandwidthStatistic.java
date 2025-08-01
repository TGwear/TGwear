/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.upstream.experimental;

import androidx.media3.common.util.UnstableApi;

/** The interface for different bandwidth estimation statistics. */
@UnstableApi
public interface BandwidthStatistic {

  /**
   * Adds a transfer sample to the statistic.
   *
   * @param bytes The number of bytes transferred.
   * @param durationUs The duration of the transfer, in microseconds.
   */
  void addSample(long bytes, long durationUs);

  /**
   * Returns the bandwidth estimate in bits per second, or {@link
   * BandwidthEstimator#ESTIMATE_NOT_AVAILABLE} if there is no estimate available yet.
   */
  long getBandwidthEstimate();

  /**
   * Resets the statistic. The statistic should drop all samples and reset to its initial state,
   * similar to right after construction.
   */
  void reset();
}
