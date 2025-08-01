/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.decoder;

import androidx.annotation.CallSuper;
import androidx.media3.common.util.UnstableApi;

/** Output buffer decoded by a {@link Decoder}. */
@UnstableApi
public abstract class DecoderOutputBuffer extends Buffer {

  /** Buffer owner. */
  public interface Owner<S extends DecoderOutputBuffer> {

    /**
     * Releases the buffer.
     *
     * @param outputBuffer Output buffer.
     */
    void releaseOutputBuffer(S outputBuffer);
  }

  /** The presentation timestamp for the buffer, in microseconds. */
  public long timeUs;

  /**
   * The number of buffers immediately prior to this one that were skipped in the {@link Decoder}.
   */
  public int skippedOutputBufferCount;

  /**
   * Whether this buffer should be skipped, usually because the decoding process generated no data
   * or invalid data.
   */
  public boolean shouldBeSkipped;

  /** Releases the output buffer for reuse. Must be called when the buffer is no longer needed. */
  public abstract void release();

  @Override
  @CallSuper
  public void clear() {
    super.clear();
    timeUs = 0;
    skippedOutputBufferCount = 0;
    shouldBeSkipped = false;
  }
}
