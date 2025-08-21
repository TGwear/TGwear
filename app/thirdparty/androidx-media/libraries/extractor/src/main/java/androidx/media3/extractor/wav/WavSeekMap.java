/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.wav;

import androidx.media3.common.C;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.SeekMap;
import androidx.media3.extractor.SeekPoint;

/* package */ final class WavSeekMap implements SeekMap {

  private final WavFormat wavFormat;
  private final int framesPerBlock;
  private final long firstBlockPosition;
  private final long blockCount;
  private final long durationUs;

  public WavSeekMap(
      WavFormat wavFormat, int framesPerBlock, long dataStartPosition, long dataEndPosition) {
    this.wavFormat = wavFormat;
    this.framesPerBlock = framesPerBlock;
    this.firstBlockPosition = dataStartPosition;
    this.blockCount = (dataEndPosition - dataStartPosition) / wavFormat.blockSize;
    durationUs = blockIndexToTimeUs(blockCount);
  }

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
    // Calculate the containing block index, constraining to valid indices.
    long blockIndex = (timeUs * wavFormat.frameRateHz) / (C.MICROS_PER_SECOND * framesPerBlock);
    blockIndex = Util.constrainValue(blockIndex, 0, blockCount - 1);

    long seekPosition = firstBlockPosition + (blockIndex * wavFormat.blockSize);
    long seekTimeUs = blockIndexToTimeUs(blockIndex);
    SeekPoint seekPoint = new SeekPoint(seekTimeUs, seekPosition);
    if (seekTimeUs >= timeUs || blockIndex == blockCount - 1) {
      return new SeekPoints(seekPoint);
    } else {
      long secondBlockIndex = blockIndex + 1;
      long secondSeekPosition = firstBlockPosition + (secondBlockIndex * wavFormat.blockSize);
      long secondSeekTimeUs = blockIndexToTimeUs(secondBlockIndex);
      SeekPoint secondSeekPoint = new SeekPoint(secondSeekTimeUs, secondSeekPosition);
      return new SeekPoints(seekPoint, secondSeekPoint);
    }
  }

  private long blockIndexToTimeUs(long blockIndex) {
    return Util.scaleLargeTimestamp(
        blockIndex * framesPerBlock, C.MICROS_PER_SECOND, wavFormat.frameRateHz);
  }
}
