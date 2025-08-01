/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common.util;

import static androidx.media3.common.util.Assertions.checkArgument;
import static androidx.media3.common.util.Util.durationUsToSampleCount;
import static androidx.media3.common.util.Util.sampleCountToDurationUs;
import static java.lang.Math.floor;
import static java.lang.Math.min;

import androidx.media3.common.C;
import androidx.media3.common.audio.SpeedProvider;

/** Utilities for {@link SpeedProvider}. */
@UnstableApi
public class SpeedProviderUtil {

  private SpeedProviderUtil() {}

  /**
   * Returns the duration of the output when the given {@link SpeedProvider} is applied given an
   * input stream with the given {@code durationUs}.
   */
  public static long getDurationAfterSpeedProviderApplied(
      SpeedProvider speedProvider, long durationUs) {
    long speedChangeTimeUs = 0;
    double outputDurationUs = 0;
    while (speedChangeTimeUs < durationUs) {
      long nextSpeedChangeTimeUs = speedProvider.getNextSpeedChangeTimeUs(speedChangeTimeUs);
      if (nextSpeedChangeTimeUs == C.TIME_UNSET) {
        nextSpeedChangeTimeUs = Long.MAX_VALUE;
      }
      outputDurationUs +=
          (min(nextSpeedChangeTimeUs, durationUs) - speedChangeTimeUs)
              / (double) speedProvider.getSpeed(speedChangeTimeUs);
      speedChangeTimeUs = nextSpeedChangeTimeUs;
    }
    // Use floor to be consistent with Util#scaleLargeTimestamp().
    return (long) floor(outputDurationUs);
  }

  /**
   * Returns the speed at the specified sample position.
   *
   * <p>This method is consistent with the alignment done by {@link
   * #getNextSpeedChangeSamplePosition}.
   */
  public static float getSampleAlignedSpeed(
      SpeedProvider speedProvider, long samplePosition, int sampleRate) {
    checkArgument(samplePosition >= 0);
    checkArgument(sampleRate > 0);

    long durationUs = sampleCountToDurationUs(samplePosition, sampleRate);
    return speedProvider.getSpeed(durationUs);
  }

  /**
   * Returns the sample position of the next speed change or {@link C#INDEX_UNSET} if none is set.
   *
   * <p>If the next speed change falls between sample boundaries, this method will return the next
   * closest sample position, which ensures that speed regions stay consistent with {@link
   * #getSampleAlignedSpeed}.
   */
  public static long getNextSpeedChangeSamplePosition(
      SpeedProvider speedProvider, long samplePosition, int sampleRate) {
    checkArgument(samplePosition >= 0);
    checkArgument(sampleRate > 0);

    long durationUs = sampleCountToDurationUs(samplePosition, sampleRate);
    long nextSpeedChangeTimeUs = speedProvider.getNextSpeedChangeTimeUs(durationUs);

    if (nextSpeedChangeTimeUs == C.TIME_UNSET) {
      return C.INDEX_UNSET;
    }

    // Use RoundingMode#UP to return next closest sample if duration falls between samples.
    return durationUsToSampleCount(nextSpeedChangeTimeUs, sampleRate);
  }
}
