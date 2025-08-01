/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.effect;

import static androidx.media3.common.util.Assertions.checkState;

import androidx.media3.common.C;
import androidx.media3.common.GlObjectsProvider;
import androidx.media3.common.GlTextureInfo;
import androidx.media3.common.VideoFrameProcessingException;
import androidx.media3.common.util.UnstableApi;
import com.google.common.util.concurrent.MoreExecutors;
import java.util.concurrent.Executor;

/**
 * A {@linkplain GlShaderProgram} that passes a frame from the input to the output listener without
 * copying.
 *
 * <p>This shader program can only process one input frame at a time.
 */
@UnstableApi
public class PassthroughShaderProgram implements GlShaderProgram {

  private InputListener inputListener;
  private OutputListener outputListener;
  private ErrorListener errorListener;
  private Executor errorListenerExecutor;
  private int texIdInUse;

  public PassthroughShaderProgram() {
    inputListener = new InputListener() {};
    outputListener = new OutputListener() {};
    errorListener = (frameProcessingException) -> {};
    errorListenerExecutor = MoreExecutors.directExecutor();
    texIdInUse = C.INDEX_UNSET;
  }

  @Override
  public void setInputListener(InputListener inputListener) {
    this.inputListener = inputListener;
    if (texIdInUse == C.INDEX_UNSET) {
      inputListener.onReadyToAcceptInputFrame();
    }
  }

  @Override
  public void setOutputListener(OutputListener outputListener) {
    this.outputListener = outputListener;
  }

  @Override
  public void setErrorListener(Executor executor, ErrorListener errorListener) {
    this.errorListenerExecutor = executor;
    this.errorListener = errorListener;
  }

  @Override
  public void queueInputFrame(
      GlObjectsProvider glObjectsProvider, GlTextureInfo inputTexture, long presentationTimeUs) {
    texIdInUse = inputTexture.texId;
    outputListener.onOutputFrameAvailable(inputTexture, presentationTimeUs);
  }

  @Override
  public void releaseOutputFrame(GlTextureInfo outputTexture) {
    checkState(outputTexture.texId == texIdInUse);
    texIdInUse = C.INDEX_UNSET;
    inputListener.onInputFrameProcessed(outputTexture);
    inputListener.onReadyToAcceptInputFrame();
  }

  @Override
  public void signalEndOfCurrentInputStream() {
    outputListener.onCurrentOutputStreamEnded();
  }

  @Override
  public void flush() {
    texIdInUse = C.INDEX_UNSET;
    inputListener.onFlush();
    inputListener.onReadyToAcceptInputFrame();
  }

  @Override
  public void release() throws VideoFrameProcessingException {
    texIdInUse = C.INDEX_UNSET;
  }

  protected final InputListener getInputListener() {
    return inputListener;
  }

  protected final void onError(Exception e) {
    errorListenerExecutor.execute(
        () -> errorListener.onError(VideoFrameProcessingException.from(e)));
  }
}
