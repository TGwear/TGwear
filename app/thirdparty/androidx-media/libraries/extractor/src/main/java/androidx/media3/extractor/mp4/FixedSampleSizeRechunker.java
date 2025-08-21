/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.mp4;

import static java.lang.Math.max;
import static java.lang.Math.min;

import androidx.media3.common.C;
import androidx.media3.common.util.Util;

/**
 * Rechunks fixed sample size media in which every sample is a key frame (e.g. uncompressed audio).
 */
/* package */ final class FixedSampleSizeRechunker {

  /** The result of a rechunking operation. */
  public static final class Results {

    public final long[] offsets;
    public final int[] sizes;
    public final int maximumSize;
    public final long[] timestamps;
    public final int[] flags;
    public final long duration;
    public final long totalSize;

    private Results(
        long[] offsets,
        int[] sizes,
        int maximumSize,
        long[] timestamps,
        int[] flags,
        long duration,
        long totalSize) {
      this.offsets = offsets;
      this.sizes = sizes;
      this.maximumSize = maximumSize;
      this.timestamps = timestamps;
      this.flags = flags;
      this.duration = duration;
      this.totalSize = totalSize;
    }
  }

  /** Maximum number of bytes for each buffer in rechunked output. */
  private static final int MAX_SAMPLE_SIZE = 8 * 1024;

  /**
   * Rechunk the given fixed sample size input to produce a new sequence of samples.
   *
   * @param fixedSampleSize Size in bytes of each sample.
   * @param chunkOffsets Chunk offsets in the MP4 stream to rechunk.
   * @param chunkSampleCounts Sample counts for each of the MP4 stream's chunks.
   * @param timestampDeltaInTimeUnits Timestamp delta between each sample in time units.
   */
  public static Results rechunk(
      int fixedSampleSize,
      long[] chunkOffsets,
      int[] chunkSampleCounts,
      long timestampDeltaInTimeUnits) {
    int maxSampleCount = MAX_SAMPLE_SIZE / fixedSampleSize;

    // Count the number of new, rechunked buffers.
    int rechunkedSampleCount = 0;
    for (int chunkSampleCount : chunkSampleCounts) {
      rechunkedSampleCount += Util.ceilDivide(chunkSampleCount, maxSampleCount);
    }

    long[] offsets = new long[rechunkedSampleCount];
    int[] sizes = new int[rechunkedSampleCount];
    int maximumSize = 0;
    long[] timestamps = new long[rechunkedSampleCount];
    int[] flags = new int[rechunkedSampleCount];
    int totalSize = 0;

    int originalSampleIndex = 0;
    int newSampleIndex = 0;
    for (int chunkIndex = 0; chunkIndex < chunkSampleCounts.length; chunkIndex++) {
      int chunkSamplesRemaining = chunkSampleCounts[chunkIndex];
      long sampleOffset = chunkOffsets[chunkIndex];

      while (chunkSamplesRemaining > 0) {
        int bufferSampleCount = min(maxSampleCount, chunkSamplesRemaining);

        offsets[newSampleIndex] = sampleOffset;
        sizes[newSampleIndex] = fixedSampleSize * bufferSampleCount;
        totalSize += sizes[newSampleIndex];
        maximumSize = max(maximumSize, sizes[newSampleIndex]);
        timestamps[newSampleIndex] = (timestampDeltaInTimeUnits * originalSampleIndex);
        flags[newSampleIndex] = C.BUFFER_FLAG_KEY_FRAME;

        sampleOffset += sizes[newSampleIndex];
        originalSampleIndex += bufferSampleCount;

        chunkSamplesRemaining -= bufferSampleCount;
        newSampleIndex++;
      }
    }
    long duration = timestampDeltaInTimeUnits * originalSampleIndex;

    return new Results(offsets, sizes, maximumSize, timestamps, flags, duration, totalSize);
  }

  private FixedSampleSizeRechunker() {
    // Prevent instantiation.
  }
}
