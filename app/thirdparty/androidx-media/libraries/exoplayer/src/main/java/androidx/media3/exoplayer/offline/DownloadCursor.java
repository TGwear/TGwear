/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.offline;

import androidx.media3.common.util.UnstableApi;
import java.io.Closeable;

/** Provides random read-write access to the result set returned by a database query. */
@UnstableApi
public interface DownloadCursor extends Closeable {

  /** Returns the download at the current position. */
  Download getDownload();

  /** Returns the numbers of downloads in the cursor. */
  int getCount();

  /**
   * Returns the current position of the cursor in the download set. The value is zero-based. When
   * the download set is first returned the cursor will be at position -1, which is before the first
   * download. After the last download is returned another call to next() will leave the cursor past
   * the last entry, at a position of count().
   *
   * @return the current cursor position.
   */
  int getPosition();

  /**
   * Move the cursor to an absolute position. The valid range of values is -1 &lt;= position &lt;=
   * count.
   *
   * <p>This method will return true if the request destination was reachable, otherwise, it returns
   * false.
   *
   * @param position the zero-based position to move to.
   * @return whether the requested move fully succeeded.
   */
  boolean moveToPosition(int position);

  /**
   * Move the cursor to the first download.
   *
   * <p>This method will return false if the cursor is empty.
   *
   * @return whether the move succeeded.
   */
  default boolean moveToFirst() {
    return moveToPosition(0);
  }

  /**
   * Move the cursor to the last download.
   *
   * <p>This method will return false if the cursor is empty.
   *
   * @return whether the move succeeded.
   */
  default boolean moveToLast() {
    return moveToPosition(getCount() - 1);
  }

  /**
   * Move the cursor to the next download.
   *
   * <p>This method will return false if the cursor is already past the last entry in the result
   * set.
   *
   * @return whether the move succeeded.
   */
  default boolean moveToNext() {
    return moveToPosition(getPosition() + 1);
  }

  /**
   * Move the cursor to the previous download.
   *
   * <p>This method will return false if the cursor is already before the first entry in the result
   * set.
   *
   * @return whether the move succeeded.
   */
  default boolean moveToPrevious() {
    return moveToPosition(getPosition() - 1);
  }

  /** Returns whether the cursor is pointing to the first download. */
  default boolean isFirst() {
    return getPosition() == 0 && getCount() != 0;
  }

  /** Returns whether the cursor is pointing to the last download. */
  default boolean isLast() {
    int count = getCount();
    return getPosition() == (count - 1) && count != 0;
  }

  /** Returns whether the cursor is pointing to the position before the first download. */
  default boolean isBeforeFirst() {
    if (getCount() == 0) {
      return true;
    }
    return getPosition() == -1;
  }

  /** Returns whether the cursor is pointing to the position after the last download. */
  default boolean isAfterLast() {
    if (getCount() == 0) {
      return true;
    }
    return getPosition() == getCount();
  }

  /** Returns whether the cursor is closed */
  boolean isClosed();

  @Override
  void close();
}
