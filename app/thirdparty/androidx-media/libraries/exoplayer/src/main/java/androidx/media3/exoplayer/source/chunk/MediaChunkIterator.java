/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.source.chunk;

import androidx.media3.common.util.UnstableApi;
import androidx.media3.datasource.DataSpec;
import java.util.NoSuchElementException;

/**
 * Iterator for media chunk sequences.
 *
 * <p>The iterator initially points in front of the first available element. The first call to
 * {@link #next()} moves the iterator to the first element. Check the return value of {@link
 * #next()} or {@link #isEnded()} to determine whether the iterator reached the end of the available
 * data.
 */
@UnstableApi
public interface MediaChunkIterator {

  /** An empty media chunk iterator without available data. */
  MediaChunkIterator EMPTY =
      new MediaChunkIterator() {
        @Override
        public boolean isEnded() {
          return true;
        }

        @Override
        public boolean next() {
          return false;
        }

        @Override
        public DataSpec getDataSpec() {
          throw new NoSuchElementException();
        }

        @Override
        public long getChunkStartTimeUs() {
          throw new NoSuchElementException();
        }

        @Override
        public long getChunkEndTimeUs() {
          throw new NoSuchElementException();
        }

        @Override
        public void reset() {
          // Do nothing.
        }
      };

  /** Returns whether the iteration has reached the end of the available data. */
  boolean isEnded();

  /**
   * Moves the iterator to the next media chunk.
   *
   * <p>Check the return value or {@link #isEnded()} to determine whether the iterator reached the
   * end of the available data.
   *
   * @return Whether the iterator points to a media chunk with available data.
   */
  boolean next();

  /**
   * Returns the {@link DataSpec} used to load the media chunk.
   *
   * @throws java.util.NoSuchElementException If the method is called before the first call to
   *     {@link #next()} or when {@link #isEnded()} is true.
   */
  DataSpec getDataSpec();

  /**
   * Returns the media start time of the chunk, in microseconds.
   *
   * @throws java.util.NoSuchElementException If the method is called before the first call to
   *     {@link #next()} or when {@link #isEnded()} is true.
   */
  long getChunkStartTimeUs();

  /**
   * Returns the media end time of the chunk, in microseconds.
   *
   * @throws java.util.NoSuchElementException If the method is called before the first call to
   *     {@link #next()} or when {@link #isEnded()} is true.
   */
  long getChunkEndTimeUs();

  /** Resets the iterator to the initial position. */
  void reset();
}
