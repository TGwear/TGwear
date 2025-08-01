/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common.util;

import androidx.media3.common.C;

/** A primitive long iterator used for generating sequences of timestamps. */
@UnstableApi
public interface TimestampIterator {

  /** Returns whether there is another element. */
  boolean hasNext();

  /** Returns the next timestamp. */
  long next();

  /** Returns fresh copy of the iterator. */
  TimestampIterator copyOf();

  /**
   * Returns the last (final) timestamp this iterator generates, in microseconds.
   *
   * <p>This method returns {@link C#TIME_UNSET} if the last timestamp is unknown, or when no
   * timestamp will be generated.
   */
  default long getLastTimestampUs() {
    return C.TIME_UNSET;
  }
}
