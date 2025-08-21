/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common.util;

import static java.lang.Math.max;

import java.util.Arrays;

/** An append-only, auto-growing {@code long[]}. */
@UnstableApi
public final class LongArray {

  private static final int DEFAULT_INITIAL_CAPACITY = 32;

  private int size;
  private long[] values;

  public LongArray() {
    this(DEFAULT_INITIAL_CAPACITY);
  }

  /**
   * @param initialCapacity The initial capacity of the array.
   */
  public LongArray(int initialCapacity) {
    values = new long[initialCapacity];
  }

  /**
   * Appends a value.
   *
   * @param value The value to append.
   */
  public void add(long value) {
    if (size == values.length) {
      values = Arrays.copyOf(values, size * 2);
    }
    values[size++] = value;
  }

  /**
   * Appends all elements of the specified array.
   *
   * @param values The array whose elements are to be added.
   */
  public void addAll(long[] values) {
    int newSize = size + values.length;
    if (newSize > this.values.length) {
      this.values = Arrays.copyOf(this.values, max(this.values.length * 2, newSize));
    }
    System.arraycopy(values, 0, this.values, size, values.length);
    size = newSize;
  }

  /**
   * Returns the value at a specified index.
   *
   * @param index The index.
   * @return The corresponding value.
   * @throws IndexOutOfBoundsException If the index is less than zero, or greater than or equal to
   *     {@link #size()}.
   */
  public long get(int index) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException("Invalid index " + index + ", size is " + size);
    }
    return values[index];
  }

  /** Returns the current size of the array. */
  public int size() {
    return size;
  }

  /**
   * Copies the current values into a newly allocated primitive array.
   *
   * @return The primitive array containing the copied values.
   */
  public long[] toArray() {
    return Arrays.copyOf(values, size);
  }
}
