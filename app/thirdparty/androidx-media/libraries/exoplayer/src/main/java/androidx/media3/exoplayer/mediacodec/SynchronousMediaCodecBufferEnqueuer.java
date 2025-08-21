/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.mediacodec;

import android.media.MediaCodec;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.media3.decoder.CryptoInfo;

@RequiresApi(23)
/* package */ class SynchronousMediaCodecBufferEnqueuer implements MediaCodecBufferEnqueuer {

  private final MediaCodec codec;

  public SynchronousMediaCodecBufferEnqueuer(MediaCodec codec) {
    this.codec = codec;
  }

  @Override
  public void start() {
    // Do nothing.
  }

  @Override
  public void queueInputBuffer(
      int index, int offset, int size, long presentationTimeUs, int flags) {
    codec.queueInputBuffer(index, offset, size, presentationTimeUs, flags);
  }

  @Override
  public void queueSecureInputBuffer(
      int index, int offset, CryptoInfo info, long presentationTimeUs, int flags) {
    codec.queueSecureInputBuffer(
        index, offset, info.getFrameworkCryptoInfo(), presentationTimeUs, flags);
  }

  @Override
  public void setParameters(Bundle parameters) {
    codec.setParameters(parameters);
  }

  @Override
  public void flush() {
    // Do nothing.
  }

  @Override
  public void shutdown() {
    // Do nothing.
  }

  @Override
  public void waitUntilQueueingComplete() {
    // Do nothing.
  }

  @Override
  public void maybeThrowException() {
    // Do nothing.
  }
}
