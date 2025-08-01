/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.effect;

import androidx.media3.common.C;
import androidx.media3.common.GlObjectsProvider;
import androidx.media3.common.GlTextureInfo;
import androidx.media3.common.audio.SpeedProvider;

/**
 * Applies the speed changes specified in a {@link SpeedProvider} change by updating the frame
 * timestamps.
 *
 * <p>Does not support seeking in effects previewing.
 */
/* package */ final class SpeedChangeShaderProgram extends PassthroughShaderProgram {

  private final OffsetSpeedProvider speedProvider;

  private long lastSpeedChangeInputTimeUs;
  private long lastSpeedChangeOutputTimeUs;

  public SpeedChangeShaderProgram(SpeedProvider speedProvider) {
    super();
    this.speedProvider = new OffsetSpeedProvider(speedProvider);
    lastSpeedChangeInputTimeUs = C.TIME_UNSET;
    lastSpeedChangeOutputTimeUs = C.TIME_UNSET;
  }

  @Override
  public void queueInputFrame(
      GlObjectsProvider glObjectsProvider, GlTextureInfo inputTexture, long presentationTimeUs) {
    long outputPresentationTimeUs;
    if (lastSpeedChangeInputTimeUs == C.TIME_UNSET) {
      outputPresentationTimeUs = presentationTimeUs;
      lastSpeedChangeInputTimeUs = presentationTimeUs;
      lastSpeedChangeOutputTimeUs = outputPresentationTimeUs;
      speedProvider.setOffset(presentationTimeUs);
    } else {
      long nextSpeedChangeInputTimeUs =
          speedProvider.getNextSpeedChangeTimeUs(lastSpeedChangeInputTimeUs);
      while (nextSpeedChangeInputTimeUs != C.TIME_UNSET
          && nextSpeedChangeInputTimeUs <= presentationTimeUs) {
        lastSpeedChangeOutputTimeUs =
            getOutputTimeUs(
                nextSpeedChangeInputTimeUs, speedProvider.getSpeed(lastSpeedChangeInputTimeUs));
        lastSpeedChangeInputTimeUs = nextSpeedChangeInputTimeUs;
        nextSpeedChangeInputTimeUs =
            speedProvider.getNextSpeedChangeTimeUs(lastSpeedChangeInputTimeUs);
      }
      outputPresentationTimeUs =
          getOutputTimeUs(presentationTimeUs, speedProvider.getSpeed(presentationTimeUs));
    }
    super.queueInputFrame(glObjectsProvider, inputTexture, outputPresentationTimeUs);
  }

  @Override
  public void signalEndOfCurrentInputStream() {
    super.signalEndOfCurrentInputStream();
    lastSpeedChangeInputTimeUs = C.TIME_UNSET;
    lastSpeedChangeOutputTimeUs = C.TIME_UNSET;
  }

  private long getOutputTimeUs(long inputTimeUs, float speed) {
    return (long)
        (lastSpeedChangeOutputTimeUs + (inputTimeUs - lastSpeedChangeInputTimeUs) / speed);
  }

  private static class OffsetSpeedProvider implements SpeedProvider {

    private final SpeedProvider speedProvider;

    private long offset;

    public OffsetSpeedProvider(SpeedProvider speedProvider) {
      this.speedProvider = speedProvider;
    }

    public void setOffset(long offset) {
      this.offset = offset;
    }

    @Override
    public float getSpeed(long timeUs) {
      return speedProvider.getSpeed(timeUs - offset);
    }

    @Override
    public long getNextSpeedChangeTimeUs(long timeUs) {
      long nextSpeedChangeTimeUs = speedProvider.getNextSpeedChangeTimeUs(timeUs - offset);
      return nextSpeedChangeTimeUs == C.TIME_UNSET ? C.TIME_UNSET : offset + nextSpeedChangeTimeUs;
    }
  }
}
