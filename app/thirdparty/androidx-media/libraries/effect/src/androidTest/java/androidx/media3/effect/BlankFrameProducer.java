/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package androidx.media3.effect;

import static androidx.media3.common.util.Assertions.checkNotNull;

import androidx.media3.common.C;
import androidx.media3.common.GlObjectsProvider;
import androidx.media3.common.GlTextureInfo;
import androidx.media3.common.VideoFrameProcessingException;
import androidx.media3.common.util.GlUtil;
import java.util.List;
import java.util.concurrent.Executor;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

/** Produces blank frames with the given timestamps. */
/* package */ final class BlankFrameProducer implements GlShaderProgram {
  private final int width;
  private final int height;

  private @MonotonicNonNull GlTextureInfo blankTexture;
  private @MonotonicNonNull OutputListener outputListener;

  public BlankFrameProducer(int width, int height) {
    this.width = width;
    this.height = height;
  }

  public void configureGlObjects() throws VideoFrameProcessingException {
    try {
      int texId = GlUtil.createTexture(width, height, /* useHighPrecisionColorComponents= */ false);
      int fboId = GlUtil.createFboForTexture(texId);
      blankTexture = new GlTextureInfo(texId, fboId, /* rboId= */ C.INDEX_UNSET, width, height);
      GlUtil.focusFramebufferUsingCurrentContext(fboId, width, height);
      GlUtil.clearFocusedBuffers();
    } catch (GlUtil.GlException e) {
      throw new VideoFrameProcessingException(e);
    }
  }

  public void produceBlankFrames(List<Long> presentationTimesUs) {
    checkNotNull(outputListener);
    for (long presentationTimeUs : presentationTimesUs) {
      outputListener.onOutputFrameAvailable(checkNotNull(blankTexture), presentationTimeUs);
    }
  }

  @Override
  public void setInputListener(InputListener inputListener) {}

  @Override
  public void setOutputListener(OutputListener outputListener) {
    this.outputListener = outputListener;
  }

  @Override
  public void setErrorListener(Executor executor, ErrorListener errorListener) {}

  @Override
  public void queueInputFrame(
      GlObjectsProvider glObjectsProvider, GlTextureInfo inputTexture, long presentationTimeUs) {
    // No input is queued in these tests. The BlankFrameProducer is used to produce frames.
    throw new UnsupportedOperationException();
  }

  @Override
  public void releaseOutputFrame(GlTextureInfo outputTexture) {}

  @Override
  public void signalEndOfCurrentInputStream() {
    checkNotNull(outputListener).onCurrentOutputStreamEnded();
  }

  @Override
  public void flush() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void release() {
    // Do nothing as destroying the OpenGL context destroys the texture.
  }
}
