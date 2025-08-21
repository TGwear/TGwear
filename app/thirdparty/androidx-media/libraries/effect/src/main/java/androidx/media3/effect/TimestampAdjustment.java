/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.effect;

import android.content.Context;
import androidx.media3.common.audio.SpeedProvider;
import androidx.media3.common.util.SpeedProviderUtil;
import androidx.media3.common.util.TimestampConsumer;
import androidx.media3.common.util.UnstableApi;

/**
 * Changes the frame timestamps using the {@link TimestampMap}.
 *
 * <p>This effect doesn't drop any frames.
 */
@UnstableApi
public final class TimestampAdjustment implements GlEffect {

  /**
   * Maps input timestamps to output timestamps asynchronously.
   *
   * <p>Implementation can choose to calculate the timestamp and invoke the consumer on another
   * thread asynchronously.
   */
  public interface TimestampMap {

    /**
     * Calculates the output timestamp that corresponds to the input timestamp.
     *
     * <p>The implementation should invoke the {@code outputTimeConsumer} with the output timestamp,
     * on any thread.
     */
    void calculateOutputTimeUs(long inputTimeUs, TimestampConsumer outputTimeConsumer);
  }

  public final TimestampMap timestampMap;

  public final SpeedProvider speedProvider;

  /**
   * Creates an instance.
   *
   * @param timestampMap The {@link TimestampMap}
   * @param speedProvider The {@link SpeedProvider} specifying the approximate speed adjustments the
   *     {@link TimestampMap} should be applying.
   */
  public TimestampAdjustment(TimestampMap timestampMap, SpeedProvider speedProvider) {
    this.timestampMap = timestampMap;
    this.speedProvider = speedProvider;
  }

  @Override
  public GlShaderProgram toGlShaderProgram(Context context, boolean useHdr) {
    return new TimestampAdjustmentShaderProgram(timestampMap);
  }

  @Override
  public long getDurationAfterEffectApplied(long durationUs) {
    return SpeedProviderUtil.getDurationAfterSpeedProviderApplied(speedProvider, durationUs);
  }
}
