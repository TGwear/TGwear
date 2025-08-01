/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.decoder;

import androidx.annotation.Nullable;
import androidx.media3.common.util.UnstableApi;

/**
 * A media decoder.
 *
 * @param <I> The type of buffer input to the decoder.
 * @param <O> The type of buffer output from the decoder.
 * @param <E> The type of exception thrown from the decoder.
 */
@UnstableApi
public interface Decoder<I, O, E extends DecoderException> {

  /**
   * Returns the name of the decoder.
   *
   * @return The name of the decoder.
   */
  String getName();

  /**
   * Sets the timestamp from which output buffers should be produced, in microseconds.
   *
   * <p>Any decoded buffer with a timestamp less than {@code outputStartTimeUs} should be skipped by
   * the implementation and not made available via {@link #dequeueOutputBuffer}.
   *
   * <p>This method must only be called before {@linkplain #queueInputBuffer queuing the first input
   * buffer} initially or after {@link #flush()}.
   *
   * @param outputStartTimeUs The time from which output buffer should be produced, in microseconds.
   */
  void setOutputStartTimeUs(long outputStartTimeUs);

  /**
   * Dequeues the next input buffer to be filled and queued to the decoder.
   *
   * @return The input buffer, which will have been cleared, or null if a buffer isn't available.
   * @throws E If a decoder error has occurred.
   */
  @Nullable
  I dequeueInputBuffer() throws E;

  /**
   * Queues an input buffer to the decoder.
   *
   * @param inputBuffer The input buffer.
   * @throws E If a decoder error has occurred.
   */
  void queueInputBuffer(I inputBuffer) throws E;

  /**
   * Dequeues the next output buffer from the decoder.
   *
   * @return The output buffer, or null if an output buffer isn't available.
   * @throws E If a decoder error has occurred.
   */
  @Nullable
  O dequeueOutputBuffer() throws E;

  /**
   * Flushes the decoder. Ownership of dequeued input buffers is returned to the decoder. The caller
   * is still responsible for releasing any dequeued output buffers.
   */
  void flush();

  /** Releases the decoder. Must be called when the decoder is no longer needed. */
  void release();
}
