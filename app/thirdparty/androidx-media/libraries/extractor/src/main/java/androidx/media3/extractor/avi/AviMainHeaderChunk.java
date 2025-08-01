/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.avi;

import androidx.media3.common.util.ParsableByteArray;

/** Wrapper around the AVIMAINHEADER structure */
/* package */ final class AviMainHeaderChunk implements AviChunk {

  private static final int AVIF_HAS_INDEX = 0x10;

  public static AviMainHeaderChunk parseFrom(ParsableByteArray body) {
    int microSecPerFrame = body.readLittleEndianInt();
    body.skipBytes(8); // Skip dwMaxBytesPerSec (4 bytes), dwPaddingGranularity (4 bytes).
    int flags = body.readLittleEndianInt();
    int totalFrames = body.readLittleEndianInt();
    body.skipBytes(4); // dwInitialFrames (4 bytes).
    int streams = body.readLittleEndianInt();
    body.skipBytes(12); // dwSuggestedBufferSize (4 bytes), dwWidth (4 bytes), dwHeight (4 bytes).
    return new AviMainHeaderChunk(microSecPerFrame, flags, totalFrames, streams);
  }

  public final int frameDurationUs;
  public final int flags;
  public final int totalFrames;
  public final int streams;

  private AviMainHeaderChunk(int frameDurationUs, int flags, int totalFrames, int streams) {
    this.frameDurationUs = frameDurationUs;
    this.flags = flags;
    this.totalFrames = totalFrames;
    this.streams = streams;
  }

  @Override
  public int getType() {
    return AviExtractor.FOURCC_avih;
  }

  public boolean hasIndex() {
    return (flags & AVIF_HAS_INDEX) == AVIF_HAS_INDEX;
  }
}
