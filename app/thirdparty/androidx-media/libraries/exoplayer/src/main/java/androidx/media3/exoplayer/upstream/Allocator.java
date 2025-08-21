/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.upstream;

import androidx.annotation.Nullable;
import androidx.media3.common.util.UnstableApi;

/** A source of allocations. */
@UnstableApi
public interface Allocator {

  /** A node in a chain of {@link Allocation Allocations}. */
  interface AllocationNode {

    /** Returns the {@link Allocation} associated to this chain node. */
    Allocation getAllocation();

    /** Returns the next chain node, or {@code null} if this is the last node in the chain. */
    @Nullable
    AllocationNode next();
  }

  /**
   * Obtain an {@link Allocation}.
   *
   * <p>When the caller has finished with the {@link Allocation}, it should be returned by calling
   * {@link #release(Allocation)}.
   *
   * @return The {@link Allocation}.
   */
  Allocation allocate();

  /**
   * Releases an {@link Allocation} back to the allocator.
   *
   * @param allocation The {@link Allocation} being released.
   */
  void release(Allocation allocation);

  /**
   * Releases all {@link Allocation Allocations} in the chain starting at the given {@link
   * AllocationNode}.
   *
   * <p>Implementations must not make memory allocations.
   */
  void release(AllocationNode allocationNode);

  /**
   * Hints to the allocator that it should make a best effort to release any excess {@link
   * Allocation Allocations}.
   */
  void trim();

  /** Returns the total number of bytes currently allocated. */
  int getTotalBytesAllocated();

  /** Returns the length of each individual {@link Allocation}. */
  int getIndividualAllocationLength();
}
