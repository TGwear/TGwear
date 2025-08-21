/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor;

import androidx.media3.common.C;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.common.util.Util;

/**
 * A {@link SeekMap} implementation for FLAC streams that contain a <a
 * href="https://xiph.org/flac/format.html#metadata_block_seektable">seek table</a>.
 */
@UnstableApi
public final class FlacSeekTableSeekMap implements SeekMap {

  private final FlacStreamMetadata flacStreamMetadata;
  private final long firstFrameOffset;

  /**
   * Creates a seek map from the FLAC stream seek table.
   *
   * @param flacStreamMetadata The stream metadata.
   * @param firstFrameOffset The byte offset of the first frame in the stream.
   */
  public FlacSeekTableSeekMap(FlacStreamMetadata flacStreamMetadata, long firstFrameOffset) {
    this.flacStreamMetadata = flacStreamMetadata;
    this.firstFrameOffset = firstFrameOffset;
  }

  @Override
  public boolean isSeekable() {
    return true;
  }

  @Override
  public long getDurationUs() {
    return flacStreamMetadata.getDurationUs();
  }

  @Override
  public SeekPoints getSeekPoints(long timeUs) {
    Assertions.checkStateNotNull(flacStreamMetadata.seekTable);
    long[] pointSampleNumbers = flacStreamMetadata.seekTable.pointSampleNumbers;
    long[] pointOffsets = flacStreamMetadata.seekTable.pointOffsets;

    long targetSampleNumber = flacStreamMetadata.getSampleNumber(timeUs);
    int index =
        Util.binarySearchFloor(
            pointSampleNumbers,
            targetSampleNumber,
            /* inclusive= */ true,
            /* stayInBounds= */ false);

    long seekPointSampleNumber = index == -1 ? 0 : pointSampleNumbers[index];
    long seekPointOffsetFromFirstFrame = index == -1 ? 0 : pointOffsets[index];
    SeekPoint seekPoint = getSeekPoint(seekPointSampleNumber, seekPointOffsetFromFirstFrame);
    if (seekPoint.timeUs == timeUs || index == pointSampleNumbers.length - 1) {
      return new SeekPoints(seekPoint);
    } else {
      SeekPoint secondSeekPoint =
          getSeekPoint(pointSampleNumbers[index + 1], pointOffsets[index + 1]);
      return new SeekPoints(seekPoint, secondSeekPoint);
    }
  }

  private SeekPoint getSeekPoint(long sampleNumber, long offsetFromFirstFrame) {
    long seekTimeUs = sampleNumber * C.MICROS_PER_SECOND / flacStreamMetadata.sampleRate;
    long seekPosition = firstFrameOffset + offsetFromFirstFrame;
    return new SeekPoint(seekTimeUs, seekPosition);
  }
}
