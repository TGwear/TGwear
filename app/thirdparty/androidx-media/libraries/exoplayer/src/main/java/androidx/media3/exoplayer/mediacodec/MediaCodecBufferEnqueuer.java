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
import androidx.media3.decoder.CryptoInfo;

/**
 * Interface to queue buffers to a {@link MediaCodec}.
 *
 * <p>All methods must be called from the same thread.
 */
/* package */ interface MediaCodecBufferEnqueuer {

  /**
   * Starts this instance.
   *
   * <p>Call this method after creating an instance and before queueing input buffers.
   */
  void start();

  /**
   * Submits an input buffer for decoding.
   *
   * @see android.media.MediaCodec#queueInputBuffer
   */
  void queueInputBuffer(int index, int offset, int size, long presentationTimeUs, int flags);

  /**
   * Submits an input buffer that potentially contains encrypted data for decoding.
   *
   * <p>Note: This method behaves as {@link MediaCodec#queueSecureInputBuffer} with the difference
   * that {@code info} is of type {@link CryptoInfo} and not {@link MediaCodec.CryptoInfo}.
   *
   * @see MediaCodec#queueSecureInputBuffer
   */
  void queueSecureInputBuffer(
      int index, int offset, CryptoInfo info, long presentationTimeUs, int flags);

  /**
   * Submits new codec parameters that should be applied from the next queued input buffer.
   *
   * @see MediaCodec#setParameters(Bundle)
   */
  void setParameters(Bundle parameters);

  /** Flushes the instance. */
  void flush();

  /** Shuts down the instance. Make sure to call this method to release its internal resources. */
  void shutdown();

  /** Blocks the current thread until all input buffers pending queueing are submitted. */
  void waitUntilQueueingComplete() throws InterruptedException;

  /** Throw any exception that occurred during the enqueueing process. */
  void maybeThrowException();
}
