/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.effect;

import static androidx.media3.common.util.Assertions.checkNotNull;
import static androidx.media3.common.util.Assertions.checkState;

import androidx.annotation.Nullable;
import androidx.media3.common.GlObjectsProvider;
import androidx.media3.common.GlTextureInfo;
import androidx.media3.common.VideoFrameProcessingException;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.effect.TimestampAdjustment.TimestampMap;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/** Changes the frame timestamps using the {@link TimestampMap}. */
@UnstableApi
public class TimestampAdjustmentShaderProgram implements GlShaderProgram {

  private final TimestampMap timestampMap;
  private final AtomicInteger pendingCallbacksCount;
  private final AtomicBoolean pendingEndOfStream;

  @Nullable private GlTextureInfo inputTexture;
  private InputListener inputListener;
  private OutputListener outputListener;

  public TimestampAdjustmentShaderProgram(TimestampMap timestampMap) {
    inputListener = new InputListener() {};
    outputListener = new OutputListener() {};

    this.timestampMap = timestampMap;
    pendingCallbacksCount = new AtomicInteger();
    pendingEndOfStream = new AtomicBoolean();
  }

  @Override
  public void setInputListener(InputListener inputListener) {
    this.inputListener = inputListener;
    if (inputTexture == null) {
      inputListener.onReadyToAcceptInputFrame();
    }
  }

  @Override
  public void setOutputListener(OutputListener outputListener) {
    this.outputListener = outputListener;
  }

  @Override
  public void setErrorListener(Executor executor, ErrorListener errorListener) {
    // No checked exceptions thrown.
  }

  @Override
  public void queueInputFrame(
      GlObjectsProvider glObjectsProvider, GlTextureInfo inputTexture, long presentationTimeUs) {
    this.inputTexture = inputTexture;
    timestampMap.calculateOutputTimeUs(
        presentationTimeUs, /* outputTimeConsumer= */ this::onOutputTimeAvailable);
    pendingCallbacksCount.incrementAndGet();
  }

  @Override
  public void signalEndOfCurrentInputStream() {
    if (pendingCallbacksCount.get() == 0) {
      outputListener.onCurrentOutputStreamEnded();
    } else {
      pendingEndOfStream.set(true);
    }
  }

  @Override
  public void releaseOutputFrame(GlTextureInfo outputTexture) {
    checkState(outputTexture.texId == checkNotNull(inputTexture).texId);
    inputListener.onInputFrameProcessed(outputTexture);
    inputListener.onReadyToAcceptInputFrame();
  }

  @Override
  public void flush() {
    // TODO - b/320242819: Investigate support for previewing.
    throw new UnsupportedOperationException("This effect is not supported for previewing.");
  }

  @Override
  public void release() throws VideoFrameProcessingException {
    inputTexture = null;
  }

  private void onOutputTimeAvailable(long outputTimeUs) {
    outputListener.onOutputFrameAvailable(checkNotNull(inputTexture), outputTimeUs);
    if (pendingEndOfStream.get()) {
      outputListener.onCurrentOutputStreamEnded();
      pendingEndOfStream.set(false);
    }
    pendingCallbacksCount.decrementAndGet();
  }
}
