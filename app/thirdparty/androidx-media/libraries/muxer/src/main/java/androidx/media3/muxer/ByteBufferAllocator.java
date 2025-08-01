/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.muxer;

import androidx.media3.common.util.UnstableApi;
import java.nio.ByteBuffer;

/** A memory allocator for {@link ByteBuffer}. */
@UnstableApi
public interface ByteBufferAllocator {
  /** Default implementation. */
  ByteBufferAllocator DEFAULT = ByteBuffer::allocateDirect;

  /**
   * Allocates and returns a new {@link ByteBuffer}.
   *
   * @param capacity The new buffer's capacity, in bytes.
   * @throws IllegalArgumentException If the {@code capacity} is a negative integer.
   */
  ByteBuffer allocate(int capacity);
}
