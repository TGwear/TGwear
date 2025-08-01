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

import android.content.Context;
import androidx.media3.common.GlObjectsProvider;
import androidx.media3.common.GlTextureInfo;
import androidx.media3.common.VideoFrameProcessingException;
import java.util.concurrent.Executor;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

/** Applies a {@link TimestampWrapper} to apply a wrapped {@link GlEffect} on certain timestamps. */
/* package */ final class TimestampWrapperShaderProgram
    implements GlShaderProgram, GlShaderProgram.InputListener {

  private final long startTimeUs;
  private final long endTimeUs;
  private final WrappedShaderProgramInputListener wrappedShaderProgramInputListener;
  private final GlShaderProgram wrappedShaderProgram;
  private final GlShaderProgram copyShaderProgram;

  private int pendingWrappedGlShaderProgramFrames;
  private int pendingCopyGlShaderProgramFrames;

  /**
   * Creates a {@code TimestampWrapperShaderProgram} instance.
   *
   * @param context The {@link Context}.
   * @param useHdr Whether input textures come from an HDR source. If {@code true}, colors will be
   *     in linear RGB BT.2020. If {@code false}, colors will be in linear RGB BT.709.
   * @param timestampWrapper The {@link TimestampWrapper} to apply to each frame.
   */
  public TimestampWrapperShaderProgram(
      Context context, boolean useHdr, TimestampWrapper timestampWrapper)
      throws VideoFrameProcessingException {
    startTimeUs = timestampWrapper.startTimeUs;
    endTimeUs = timestampWrapper.endTimeUs;
    wrappedShaderProgram = timestampWrapper.glEffect.toGlShaderProgram(context, useHdr);
    wrappedShaderProgramInputListener = new WrappedShaderProgramInputListener();
    wrappedShaderProgram.setInputListener(wrappedShaderProgramInputListener);
    copyShaderProgram =
        new FrameCache(/* capacity= */ wrappedShaderProgramInputListener.readyFrameCount)
            .toGlShaderProgram(context, useHdr);
  }

  @Override
  public void setInputListener(InputListener inputListener) {
    wrappedShaderProgramInputListener.setListener(inputListener);
    wrappedShaderProgramInputListener.setToForwardingMode(true);
    copyShaderProgram.setInputListener(inputListener);
  }

  @Override
  public void setOutputListener(OutputListener outputListener) {
    wrappedShaderProgram.setOutputListener(outputListener);
    copyShaderProgram.setOutputListener(outputListener);
  }

  @Override
  public void setErrorListener(Executor errorListenerExecutor, ErrorListener errorListener) {
    wrappedShaderProgram.setErrorListener(errorListenerExecutor, errorListener);
    copyShaderProgram.setErrorListener(errorListenerExecutor, errorListener);
  }

  @Override
  public void queueInputFrame(
      GlObjectsProvider glObjectsProvider, GlTextureInfo inputTexture, long presentationTimeUs) {
    if (startTimeUs <= presentationTimeUs && presentationTimeUs <= endTimeUs) {
      pendingWrappedGlShaderProgramFrames++;
      wrappedShaderProgram.queueInputFrame(glObjectsProvider, inputTexture, presentationTimeUs);
    } else {
      pendingCopyGlShaderProgramFrames++;
      copyShaderProgram.queueInputFrame(glObjectsProvider, inputTexture, presentationTimeUs);
    }
  }

  @Override
  public void releaseOutputFrame(GlTextureInfo outputTexture) {
    if (pendingCopyGlShaderProgramFrames > 0) {
      copyShaderProgram.releaseOutputFrame(outputTexture);
      pendingCopyGlShaderProgramFrames--;
    } else if (pendingWrappedGlShaderProgramFrames > 0) {
      wrappedShaderProgram.releaseOutputFrame(outputTexture);
      pendingWrappedGlShaderProgramFrames--;
    } else {
      throw new IllegalArgumentException("Output texture not contained in either shader.");
    }
  }

  @Override
  public void signalEndOfCurrentInputStream() {
    // The copy shader program does not need special EOS handling, so only EOS signal along the
    // wrapped GL shader program.
    wrappedShaderProgram.signalEndOfCurrentInputStream();
  }

  @Override
  public void flush() {
    wrappedShaderProgramInputListener.setToForwardingMode(false);
    wrappedShaderProgram.flush();
    wrappedShaderProgramInputListener.setToForwardingMode(true);
    copyShaderProgram.flush();
    pendingCopyGlShaderProgramFrames = 0;
    pendingWrappedGlShaderProgramFrames = 0;
  }

  @Override
  public void release() throws VideoFrameProcessingException {
    copyShaderProgram.release();
    wrappedShaderProgram.release();
  }

  private static final class WrappedShaderProgramInputListener
      implements GlShaderProgram.InputListener {
    public int readyFrameCount;

    private boolean forwardCalls;
    private @MonotonicNonNull InputListener listener;

    @Override
    public void onReadyToAcceptInputFrame() {
      if (listener == null) {
        readyFrameCount++;
      }
      if (forwardCalls) {
        checkNotNull(listener).onReadyToAcceptInputFrame();
      }
    }

    @Override
    public void onInputFrameProcessed(GlTextureInfo inputTexture) {
      checkNotNull(listener).onInputFrameProcessed(inputTexture);
    }

    @Override
    public void onFlush() {
      // The listener is flushed from the copy shader program.
    }

    public void setListener(InputListener listener) {
      this.listener = listener;
    }

    public void setToForwardingMode(boolean forwardingMode) {
      checkState(!forwardingMode || listener != null);
      this.forwardCalls = forwardingMode;
    }
  }
}
