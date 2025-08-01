/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package androidx.media3.effect;

import static androidx.media3.common.util.Assertions.checkArgument;
import static java.lang.Math.round;

import androidx.media3.common.GlObjectsProvider;
import androidx.media3.common.GlTextureInfo;
import androidx.media3.common.VideoFrameProcessingException;

/**
 * Drops frames by only keeping every nth frame, where n is the {@code inputFrameRate} divided by
 * the {@code targetFrameRate}.
 *
 * <p>For example, if the input stream came in at 60fps and the targeted frame rate was 20fps, every
 * 3rd frame would be kept. If n is not an integer, then we round to the nearest one.
 */
/* package */ final class SimpleFrameDroppingShaderProgram extends PassthroughShaderProgram {

  private final int n;

  private int framesReceived;

  /**
   * Creates a new instance.
   *
   * @param inputFrameRate The number of frames per second the input stream should have.
   * @param targetFrameRate The number of frames per second the output video should roughly have.
   */
  public SimpleFrameDroppingShaderProgram(float inputFrameRate, float targetFrameRate) {
    super();
    n = round(inputFrameRate / targetFrameRate);
    checkArgument(n >= 1, "The input frame rate should be greater than the target frame rate.");
  }

  @Override
  public void queueInputFrame(
      GlObjectsProvider glObjectsProvider, GlTextureInfo inputTexture, long presentationTimeUs) {
    if (framesReceived % n == 0) {
      super.queueInputFrame(glObjectsProvider, inputTexture, presentationTimeUs);
    } else {
      getInputListener().onInputFrameProcessed(inputTexture);
      getInputListener().onReadyToAcceptInputFrame();
    }
    framesReceived++;
  }

  @Override
  public void signalEndOfCurrentInputStream() {
    super.signalEndOfCurrentInputStream();
    framesReceived = 0;
  }

  @Override
  public void flush() {
    super.flush();
    framesReceived = 0;
  }

  @Override
  public void release() throws VideoFrameProcessingException {
    super.release();
    framesReceived = 0;
  }
}
