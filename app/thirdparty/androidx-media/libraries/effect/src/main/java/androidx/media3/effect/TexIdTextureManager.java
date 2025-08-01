/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.effect;

import static androidx.media3.common.util.Assertions.checkNotNull;
import static androidx.media3.effect.DebugTraceUtil.COMPONENT_TEX_ID_TEXTURE_MANAGER;
import static androidx.media3.effect.DebugTraceUtil.COMPONENT_VFP;
import static androidx.media3.effect.DebugTraceUtil.EVENT_QUEUE_TEXTURE;
import static androidx.media3.effect.DebugTraceUtil.EVENT_SIGNAL_EOS;

import android.opengl.GLES10;
import androidx.media3.common.C;
import androidx.media3.common.FrameInfo;
import androidx.media3.common.GlObjectsProvider;
import androidx.media3.common.GlTextureInfo;
import androidx.media3.common.OnInputFrameProcessedListener;
import androidx.media3.common.VideoFrameProcessingException;
import androidx.media3.common.util.GlUtil;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

/**
 * Forwards frames made available via {@linkplain GLES10#GL_TEXTURE_2D traditional GLES textures} to
 * a {@link GlShaderProgram} for consumption.
 */
/* package */ final class TexIdTextureManager extends TextureManager {
  private @MonotonicNonNull FrameConsumptionManager frameConsumptionManager;

  private @MonotonicNonNull OnInputFrameProcessedListener frameProcessedListener;
  private @MonotonicNonNull FrameInfo inputFrameInfo;
  private final GlObjectsProvider glObjectsProvider;

  /**
   * Creates a new instance.
   *
   * @param glObjectsProvider The {@link GlObjectsProvider} for using EGL and GLES.
   * @param videoFrameProcessingTaskExecutor The {@link VideoFrameProcessingTaskExecutor}.
   */
  public TexIdTextureManager(
      GlObjectsProvider glObjectsProvider,
      VideoFrameProcessingTaskExecutor videoFrameProcessingTaskExecutor) {
    super(videoFrameProcessingTaskExecutor);
    this.glObjectsProvider = glObjectsProvider;
  }

  @Override
  public void onReadyToAcceptInputFrame() {
    checkNotNull(frameConsumptionManager);
    videoFrameProcessingTaskExecutor.submit(frameConsumptionManager::onReadyToAcceptInputFrame);
  }

  @Override
  public void onInputFrameProcessed(GlTextureInfo inputTexture) {
    videoFrameProcessingTaskExecutor.submit(
        () ->
            checkNotNull(frameProcessedListener)
                .onInputFrameProcessed(inputTexture.texId, GlUtil.createGlSyncFence()));
  }

  @Override
  public void setSamplingGlShaderProgram(GlShaderProgram samplingGlShaderProgram) {
    frameConsumptionManager =
        new FrameConsumptionManager(
            glObjectsProvider, samplingGlShaderProgram, videoFrameProcessingTaskExecutor);
  }

  @Override
  public void queueInputTexture(int inputTexId, long presentationTimeUs) {
    FrameInfo frameInfo = checkNotNull(this.inputFrameInfo);
    checkNotNull(frameProcessedListener);
    videoFrameProcessingTaskExecutor.submit(
        () -> {
          GlTextureInfo inputTexture =
              new GlTextureInfo(
                  inputTexId,
                  /* fboId= */ C.INDEX_UNSET,
                  /* rboId= */ C.INDEX_UNSET,
                  frameInfo.format.width,
                  frameInfo.format.height);
          checkNotNull(frameConsumptionManager).queueInputFrame(inputTexture, presentationTimeUs);
          DebugTraceUtil.logEvent(
              COMPONENT_VFP,
              EVENT_QUEUE_TEXTURE,
              presentationTimeUs,
              /* extraFormat= */ "%dx%d",
              /* extraArgs...= */ frameInfo.format.width,
              frameInfo.format.height);
        });
  }

  @Override
  public void setOnInputFrameProcessedListener(OnInputFrameProcessedListener listener) {
    frameProcessedListener = listener;
  }

  @Override
  public void setInputFrameInfo(FrameInfo inputFrameInfo, boolean automaticReregistration) {
    this.inputFrameInfo = inputFrameInfo;
  }

  @Override
  public int getPendingFrameCount() {
    return checkNotNull(frameConsumptionManager).getPendingFrameCount();
  }

  @Override
  public void signalEndOfCurrentInputStream() {
    videoFrameProcessingTaskExecutor.submit(
        () -> {
          checkNotNull(frameConsumptionManager).signalEndOfCurrentStream();
          DebugTraceUtil.logEvent(
              COMPONENT_TEX_ID_TEXTURE_MANAGER, EVENT_SIGNAL_EOS, C.TIME_END_OF_SOURCE);
        });
  }

  @Override
  public void release() {
    // Do nothing.
  }

  // Methods that must be called on the GL thread.

  @Override
  protected synchronized void flush() throws VideoFrameProcessingException {
    checkNotNull(frameConsumptionManager).onFlush();
    super.flush();
  }
}
