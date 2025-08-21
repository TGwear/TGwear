/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.mp3;

import androidx.media3.common.C;
import androidx.media3.extractor.ConstantBitrateSeekMap;
import androidx.media3.extractor.MpegAudioUtil;

/**
 * MP3 seeker that doesn't rely on metadata and seeks assuming the source has a constant bitrate.
 */
/* package */ final class ConstantBitrateSeeker extends ConstantBitrateSeekMap implements Seeker {

  private final long firstFramePosition;
  private final int bitrate;
  private final int frameSize;
  private final boolean allowSeeksIfLengthUnknown;
  private final long dataEndPosition;

  /**
   * Constructs an instance.
   *
   * @param inputLength The length of the stream in bytes, or {@link C#LENGTH_UNSET} if unknown.
   * @param firstFramePosition The position of the first frame in the stream.
   * @param mpegAudioHeader The MPEG audio header associated with the first frame.
   * @param allowSeeksIfLengthUnknown Whether to allow seeking even if the length of the content is
   *     unknown.
   */
  public ConstantBitrateSeeker(
      long inputLength,
      long firstFramePosition,
      MpegAudioUtil.Header mpegAudioHeader,
      boolean allowSeeksIfLengthUnknown) {
    // Set the seeker frame size to the size of the first frame (even though some constant bitrate
    // streams have variable frame sizes due to padding) to avoid the need to re-synchronize for
    // constant frame size streams.
    this(
        inputLength,
        firstFramePosition,
        mpegAudioHeader.bitrate,
        mpegAudioHeader.frameSize,
        allowSeeksIfLengthUnknown);
  }

  /** See {@link ConstantBitrateSeekMap#ConstantBitrateSeekMap(long, long, int, int, boolean)}. */
  public ConstantBitrateSeeker(
      long inputLength,
      long firstFramePosition,
      int bitrate,
      int frameSize,
      boolean allowSeeksIfLengthUnknown) {
    super(inputLength, firstFramePosition, bitrate, frameSize, allowSeeksIfLengthUnknown);
    this.firstFramePosition = firstFramePosition;
    this.bitrate = bitrate;
    this.frameSize = frameSize;
    this.allowSeeksIfLengthUnknown = allowSeeksIfLengthUnknown;
    dataEndPosition = inputLength != C.LENGTH_UNSET ? inputLength : C.INDEX_UNSET;
  }

  @Override
  public long getTimeUs(long position) {
    return getTimeUsAtPosition(position);
  }

  @Override
  public long getDataEndPosition() {
    return dataEndPosition;
  }

  @Override
  public int getAverageBitrate() {
    return bitrate;
  }

  public ConstantBitrateSeeker copyWithNewDataEndPosition(long dataEndPosition) {
    return new ConstantBitrateSeeker(
        /* inputLength= */ dataEndPosition,
        firstFramePosition,
        bitrate,
        frameSize,
        allowSeeksIfLengthUnknown);
  }
}
