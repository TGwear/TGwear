/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor;

import androidx.media3.common.util.UnstableApi;
import androidx.media3.common.util.Util;
import java.util.Arrays;

/** Defines chunks of samples within a media stream. */
@UnstableApi
public final class ChunkIndex implements SeekMap {

  /** The number of chunks. */
  public final int length;

  /** The chunk sizes, in bytes. */
  public final int[] sizes;

  /** The chunk byte offsets. */
  public final long[] offsets;

  /** The chunk durations, in microseconds. */
  public final long[] durationsUs;

  /** The start time of each chunk, in microseconds. */
  public final long[] timesUs;

  private final long durationUs;

  /**
   * @param sizes The chunk sizes, in bytes.
   * @param offsets The chunk byte offsets.
   * @param durationsUs The chunk durations, in microseconds.
   * @param timesUs The start time of each chunk, in microseconds.
   */
  public ChunkIndex(int[] sizes, long[] offsets, long[] durationsUs, long[] timesUs) {
    this.sizes = sizes;
    this.offsets = offsets;
    this.durationsUs = durationsUs;
    this.timesUs = timesUs;
    length = sizes.length;
    if (length > 0) {
      durationUs = durationsUs[length - 1] + timesUs[length - 1];
    } else {
      durationUs = 0;
    }
  }

  /**
   * Obtains the index of the chunk corresponding to a given time.
   *
   * @param timeUs The time, in microseconds.
   * @return The index of the corresponding chunk.
   */
  public int getChunkIndex(long timeUs) {
    return Util.binarySearchFloor(timesUs, timeUs, /* inclusive= */ true, /* stayInBounds= */ true);
  }

  // SeekMap implementation.

  @Override
  public boolean isSeekable() {
    return true;
  }

  @Override
  public long getDurationUs() {
    return durationUs;
  }

  @Override
  public SeekPoints getSeekPoints(long timeUs) {
    int chunkIndex = getChunkIndex(timeUs);
    SeekPoint seekPoint = new SeekPoint(timesUs[chunkIndex], offsets[chunkIndex]);
    if (seekPoint.timeUs >= timeUs || chunkIndex == length - 1) {
      return new SeekPoints(seekPoint);
    } else {
      SeekPoint nextSeekPoint = new SeekPoint(timesUs[chunkIndex + 1], offsets[chunkIndex + 1]);
      return new SeekPoints(seekPoint, nextSeekPoint);
    }
  }

  @Override
  public String toString() {
    return "ChunkIndex("
        + "length="
        + length
        + ", sizes="
        + Arrays.toString(sizes)
        + ", offsets="
        + Arrays.toString(offsets)
        + ", timeUs="
        + Arrays.toString(timesUs)
        + ", durationsUs="
        + Arrays.toString(durationsUs)
        + ")";
  }
}
