/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.effect;

import androidx.media3.common.GlObjectsProvider;
import androidx.media3.common.GlTextureInfo;
import androidx.media3.effect.GlShaderProgram.InputListener;
import androidx.media3.effect.GlShaderProgram.OutputListener;

/**
 * Connects a producing and a consuming {@link GlShaderProgram} instance.
 *
 * <p>This listener should be set as {@link InputListener} on the consuming {@link GlShaderProgram}
 * and as {@link OutputListener} on the producing {@link GlShaderProgram}.
 */
/* package */ final class ChainingGlShaderProgramListener
    implements GlShaderProgram.InputListener, GlShaderProgram.OutputListener {

  private final GlShaderProgram producingGlShaderProgram;
  private final FrameConsumptionManager frameConsumptionManager;
  private final VideoFrameProcessingTaskExecutor videoFrameProcessingTaskExecutor;

  /**
   * Creates a new instance.
   *
   * @param glObjectsProvider The {@link GlObjectsProvider} for using EGL and GLES.
   * @param producingGlShaderProgram The {@link GlShaderProgram} for which this listener will be set
   *     as {@link OutputListener}.
   * @param consumingGlShaderProgram The {@link GlShaderProgram} for which this listener will be set
   *     as {@link InputListener}.
   * @param videoFrameProcessingTaskExecutor The {@link VideoFrameProcessingTaskExecutor} that is
   *     used for OpenGL calls. All calls to the producing/consuming {@link GlShaderProgram} will be
   *     executed by the {@link VideoFrameProcessingTaskExecutor}. The caller is responsible for
   *     releasing the {@link VideoFrameProcessingTaskExecutor}.
   */
  public ChainingGlShaderProgramListener(
      GlObjectsProvider glObjectsProvider,
      GlShaderProgram producingGlShaderProgram,
      GlShaderProgram consumingGlShaderProgram,
      VideoFrameProcessingTaskExecutor videoFrameProcessingTaskExecutor) {
    this.producingGlShaderProgram = producingGlShaderProgram;
    frameConsumptionManager =
        new FrameConsumptionManager(
            glObjectsProvider, consumingGlShaderProgram, videoFrameProcessingTaskExecutor);
    this.videoFrameProcessingTaskExecutor = videoFrameProcessingTaskExecutor;
  }

  @Override
  public synchronized void onReadyToAcceptInputFrame() {
    frameConsumptionManager.onReadyToAcceptInputFrame();
  }

  @Override
  public void onInputFrameProcessed(GlTextureInfo inputTexture) {
    videoFrameProcessingTaskExecutor.submit(
        () -> producingGlShaderProgram.releaseOutputFrame(inputTexture));
  }

  @Override
  public synchronized void onFlush() {
    frameConsumptionManager.onFlush();
    videoFrameProcessingTaskExecutor.submit(producingGlShaderProgram::flush);
  }

  @Override
  public synchronized void onOutputFrameAvailable(
      GlTextureInfo outputTexture, long presentationTimeUs) {
    frameConsumptionManager.queueInputFrame(outputTexture, presentationTimeUs);
  }

  @Override
  public synchronized void onCurrentOutputStreamEnded() {
    frameConsumptionManager.signalEndOfCurrentStream();
  }
}
