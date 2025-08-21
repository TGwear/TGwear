/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.source.chunk;

import androidx.media3.common.util.UnstableApi;
import java.util.NoSuchElementException;

/**
 * Base class for {@link MediaChunkIterator}s. Handles {@link #next()} and {@link #isEnded()}, and
 * provides a bounds check for child classes.
 */
@UnstableApi
public abstract class BaseMediaChunkIterator implements MediaChunkIterator {

  private final long fromIndex;
  private final long toIndex;

  private long currentIndex;

  /**
   * Creates base iterator.
   *
   * @param fromIndex The first available index.
   * @param toIndex The last available index.
   */
  @SuppressWarnings("nullness:method.invocation")
  public BaseMediaChunkIterator(long fromIndex, long toIndex) {
    this.fromIndex = fromIndex;
    this.toIndex = toIndex;
    reset();
  }

  @Override
  public boolean isEnded() {
    return currentIndex > toIndex;
  }

  @Override
  public boolean next() {
    currentIndex++;
    return !isEnded();
  }

  @Override
  public void reset() {
    currentIndex = fromIndex - 1;
  }

  /**
   * Verifies that the iterator points to a valid element.
   *
   * @throws NoSuchElementException If the iterator does not point to a valid element.
   */
  protected final void checkInBounds() {
    if (currentIndex < fromIndex || currentIndex > toIndex) {
      throw new NoSuchElementException();
    }
  }

  /** Returns the current index this iterator is pointing to. */
  protected final long getCurrentIndex() {
    return currentIndex;
  }
}
