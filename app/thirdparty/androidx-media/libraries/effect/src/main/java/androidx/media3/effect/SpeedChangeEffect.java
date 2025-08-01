/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.effect;

import static androidx.media3.common.util.Assertions.checkArgument;

import android.content.Context;
import androidx.annotation.FloatRange;
import androidx.media3.common.C;
import androidx.media3.common.audio.SpeedProvider;
import androidx.media3.common.util.SpeedProviderUtil;
import androidx.media3.common.util.UnstableApi;

/**
 * Applies a speed change by updating the frame timestamps.
 *
 * <p>This effect doesn't drop any frames.
 *
 * <p>This effect is not supported for effects previewing.
 */
@UnstableApi
public final class SpeedChangeEffect implements GlEffect {

  private final SpeedProvider speedProvider;

  /** Creates an instance that applies the same {@code speed} change to all the timestamps. */
  public SpeedChangeEffect(@FloatRange(from = 0, fromInclusive = false) float speed) {
    checkArgument(speed > 0f);
    speedProvider =
        new SpeedProvider() {
          @Override
          public float getSpeed(long timeUs) {
            return speed;
          }

          @Override
          public long getNextSpeedChangeTimeUs(long timeUs) {
            return C.TIME_UNSET;
          }
        };
  }

  /**
   * Creates an instance.
   *
   * @param speedProvider The {@link SpeedProvider} specifying the speed changes. Applied on each
   *     stream assuming the first frame timestamp of the input media is 0.
   */
  public SpeedChangeEffect(SpeedProvider speedProvider) {
    this.speedProvider = speedProvider;
  }

  @Override
  public GlShaderProgram toGlShaderProgram(Context context, boolean useHdr) {
    return new SpeedChangeShaderProgram(speedProvider);
  }

  @Override
  public boolean isNoOp(int inputWidth, int inputHeight) {
    return speedProvider.getSpeed(/* timeUs= */ 0) == 1
        && speedProvider.getNextSpeedChangeTimeUs(/* timeUs= */ 0) == C.TIME_UNSET;
  }

  @Override
  public long getDurationAfterEffectApplied(long durationUs) {
    return SpeedProviderUtil.getDurationAfterSpeedProviderApplied(speedProvider, durationUs);
  }
}
