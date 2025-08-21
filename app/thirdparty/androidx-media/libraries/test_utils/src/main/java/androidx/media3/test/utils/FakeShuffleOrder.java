/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.test.utils;

import androidx.media3.common.C;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.source.ShuffleOrder;

/**
 * Fake {@link ShuffleOrder} which returns a reverse order. This order is thus deterministic but
 * different from the original order.
 */
@UnstableApi
public final class FakeShuffleOrder implements ShuffleOrder {

  private final int length;

  public FakeShuffleOrder(int length) {
    this.length = length;
  }

  @Override
  public int getLength() {
    return length;
  }

  @Override
  public int getNextIndex(int index) {
    return index > 0 ? index - 1 : C.INDEX_UNSET;
  }

  @Override
  public int getPreviousIndex(int index) {
    return index < length - 1 ? index + 1 : C.INDEX_UNSET;
  }

  @Override
  public int getLastIndex() {
    return length > 0 ? 0 : C.INDEX_UNSET;
  }

  @Override
  public int getFirstIndex() {
    return length > 0 ? length - 1 : C.INDEX_UNSET;
  }

  @Override
  public ShuffleOrder cloneAndInsert(int insertionIndex, int insertionCount) {
    return new FakeShuffleOrder(length + insertionCount);
  }

  @Override
  public ShuffleOrder cloneAndRemove(int indexFrom, int indexToExclusive) {
    return new FakeShuffleOrder(length - indexToExclusive + indexFrom);
  }

  @Override
  public ShuffleOrder cloneAndClear() {
    return new FakeShuffleOrder(/* length= */ 0);
  }
}
