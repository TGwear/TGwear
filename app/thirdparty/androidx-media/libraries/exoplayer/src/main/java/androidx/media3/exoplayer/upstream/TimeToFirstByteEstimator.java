/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.upstream;

import androidx.media3.common.C;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.datasource.DataSpec;

/** Provides an estimate of the time to first byte of a transfer. */
@UnstableApi
public interface TimeToFirstByteEstimator {
  /**
   * Returns the estimated time to first byte of the response body, in microseconds, or {@link
   * C#TIME_UNSET} if no estimate is available.
   */
  long getTimeToFirstByteEstimateUs();

  /** Resets the estimator. */
  void reset();

  /**
   * Called when a transfer is being initialized.
   *
   * @param dataSpec Describes the data for which the transfer is initialized.
   */
  void onTransferInitializing(DataSpec dataSpec);

  /**
   * Called when a transfer starts.
   *
   * @param dataSpec Describes the data being transferred.
   */
  void onTransferStart(DataSpec dataSpec);
}
