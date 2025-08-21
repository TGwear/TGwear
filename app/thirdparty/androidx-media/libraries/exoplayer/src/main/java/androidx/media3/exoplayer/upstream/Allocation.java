/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.upstream;

import androidx.media3.common.util.UnstableApi;

/**
 * An allocation within a byte array.
 *
 * <p>The allocation's length is obtained by calling {@link
 * Allocator#getIndividualAllocationLength()} on the {@link Allocator} from which it was obtained.
 */
@UnstableApi
public final class Allocation {

  /**
   * The array containing the allocated space. The allocated space might not be at the start of the
   * array, and so {@link #offset} must be used when indexing into it.
   */
  public final byte[] data;

  /** The offset of the allocated space in {@link #data}. */
  public final int offset;

  /**
   * @param data The array containing the allocated space.
   * @param offset The offset of the allocated space in {@code data}.
   */
  public Allocation(byte[] data, int offset) {
    this.data = data;
    this.offset = offset;
  }
}
