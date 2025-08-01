/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.source;

import static androidx.media3.common.util.Assertions.checkArgument;
import static androidx.media3.common.util.Assertions.checkState;
import static java.lang.Math.min;

import android.util.SparseArray;
import androidx.media3.common.C;
import androidx.media3.common.util.Consumer;

/**
 * Stores value objects associated with spans of integer keys.
 *
 * <p>This implementation is optimised for consecutive {@link #get(int)} calls with keys that are
 * close to each other in value.
 *
 * <p>Spans are defined by their own {@code startKey} (inclusive) and the {@code startKey} of the
 * next span (exclusive). The last span is open-ended.
 *
 * @param <V> The type of values stored in this collection.
 */
/* package */ final class SpannedData<V> {

  private int memoizedReadIndex;

  private final SparseArray<V> spans;
  private final Consumer<V> removeCallback;

  /** Constructs an empty instance. */
  public SpannedData() {
    this(/* removeCallback= */ value -> {});
  }

  /**
   * Constructs an empty instance that invokes {@code removeCallback} on each value that is removed
   * from the collection.
   */
  public SpannedData(Consumer<V> removeCallback) {
    spans = new SparseArray<>();
    this.removeCallback = removeCallback;
    memoizedReadIndex = C.INDEX_UNSET;
  }

  /**
   * Returns the value associated with the span covering {@code key}.
   *
   * <p>The collection must not be {@link #isEmpty() empty}.
   *
   * @param key The key to lookup in the collection. Must be greater than or equal to the previous
   *     value passed to {@link #discardTo(int)} (or zero after {@link #clear()} has been called).
   * @return The value associated with the provided key.
   */
  public V get(int key) {
    if (memoizedReadIndex == C.INDEX_UNSET) {
      memoizedReadIndex = 0;
    }
    while (memoizedReadIndex > 0 && key < spans.keyAt(memoizedReadIndex)) {
      memoizedReadIndex--;
    }
    while (memoizedReadIndex < spans.size() - 1 && key >= spans.keyAt(memoizedReadIndex + 1)) {
      memoizedReadIndex++;
    }
    return spans.valueAt(memoizedReadIndex);
  }

  /**
   * Adds a new span to the end starting at {@code startKey} and containing {@code value}.
   *
   * <p>{@code startKey} must be greater than or equal to the start key of the previous span. If
   * they're equal, the previous span is overwritten and it's passed to {@code removeCallback} (if
   * set).
   */
  public void appendSpan(int startKey, V value) {
    if (memoizedReadIndex == C.INDEX_UNSET) {
      checkState(spans.size() == 0);
      memoizedReadIndex = 0;
    }

    if (spans.size() > 0) {
      int lastStartKey = spans.keyAt(spans.size() - 1);
      checkArgument(startKey >= lastStartKey);
      if (lastStartKey == startKey) {
        removeCallback.accept(spans.valueAt(spans.size() - 1));
      }
    }
    spans.append(startKey, value);
  }

  /**
   * Returns the value associated with the end span. This is either the last value passed to {@link
   * #appendSpan(int, Object)}, or the value of the span covering the index passed to {@link
   * #discardFrom(int)}.
   *
   * <p>The collection must not be {@link #isEmpty() empty}.
   */
  public V getEndValue() {
    return spans.valueAt(spans.size() - 1);
  }

  /**
   * Discard the spans from the start up to {@code discardToKey}.
   *
   * <p>The span associated with {@code discardToKey} is not discarded (which means the last span is
   * never discarded).
   */
  public void discardTo(int discardToKey) {
    for (int i = 0; i < spans.size() - 1 && discardToKey >= spans.keyAt(i + 1); i++) {
      removeCallback.accept(spans.valueAt(i));
      spans.removeAt(i);
      if (memoizedReadIndex > 0) {
        memoizedReadIndex--;
      }
    }
  }

  /**
   * Discard the spans from the end back to {@code discardFromKey}.
   *
   * <p>The span associated with {@code discardFromKey} is not discarded.
   */
  public void discardFrom(int discardFromKey) {
    for (int i = spans.size() - 1; i >= 0 && discardFromKey < spans.keyAt(i); i--) {
      removeCallback.accept(spans.valueAt(i));
      spans.removeAt(i);
    }
    memoizedReadIndex = spans.size() > 0 ? min(memoizedReadIndex, spans.size() - 1) : C.INDEX_UNSET;
  }

  /** Remove all spans. */
  public void clear() {
    for (int i = 0; i < spans.size(); i++) {
      removeCallback.accept(spans.valueAt(i));
    }
    memoizedReadIndex = C.INDEX_UNSET;
    spans.clear();
  }

  /** Returns true if the collection is empty. */
  public boolean isEmpty() {
    return spans.size() == 0;
  }
}
