/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.muxer;

import static androidx.media3.common.util.Assertions.checkArgument;

import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import androidx.media3.common.C;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/** Represents a single track (audio, video, metadata etc.). */
/* package */ final class Track {
  public final int id;
  public final Format format;
  public final int sortKey;
  public final List<BufferInfo> writtenSamples;
  public final List<Long> writtenChunkOffsets;
  public final List<Integer> writtenChunkSampleCounts;
  public final Deque<BufferInfo> pendingSamplesBufferInfo;
  public final Deque<ByteBuffer> pendingSamplesByteBuffer;
  public boolean hadKeyframe;
  public long endOfStreamTimestampUs;

  private final boolean sampleCopyEnabled;

  /** Creates an instance with {@code sortKey} set to 1. */
  public Track(int trackId, Format format, boolean sampleCopyEnabled) {
    this(trackId, format, /* sortKey= */ 1, sampleCopyEnabled);
  }

  /**
   * Creates an instance.
   *
   * @param trackId A unique id for the track.
   * @param format The {@link Format} for the track.
   * @param sortKey The key used for sorting the track list.
   * @param sampleCopyEnabled Whether sample copying is enabled.
   */
  public Track(int trackId, Format format, int sortKey, boolean sampleCopyEnabled) {
    id = trackId;
    this.format = format;
    this.sortKey = sortKey;
    this.sampleCopyEnabled = sampleCopyEnabled;
    writtenSamples = new ArrayList<>();
    writtenChunkOffsets = new ArrayList<>();
    writtenChunkSampleCounts = new ArrayList<>();
    pendingSamplesBufferInfo = new ArrayDeque<>();
    pendingSamplesByteBuffer = new ArrayDeque<>();
    endOfStreamTimestampUs = C.TIME_UNSET;
  }

  public void writeSampleData(ByteBuffer byteBuffer, BufferInfo bufferInfo) {
    checkArgument(
        endOfStreamTimestampUs == C.TIME_UNSET,
        "Samples can not be written after writing a sample with"
            + " MediaCodec.BUFFER_FLAG_END_OF_STREAM flag");
    //  Skip empty samples.
    if (bufferInfo.size == 0 || byteBuffer.remaining() == 0) {
      if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
        endOfStreamTimestampUs = bufferInfo.presentationTimeUs;
      }
      return;
    }

    if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_KEY_FRAME) > 0) {
      hadKeyframe = true;
    }

    // The video track must start with a key frame.
    if (!hadKeyframe && MimeTypes.isVideo(format.sampleMimeType)) {
      return;
    }

    ByteBuffer byteBufferToAdd = byteBuffer;
    if (sampleCopyEnabled) {
      // Copy sample data and release the original buffer.
      byteBufferToAdd = ByteBuffer.allocateDirect(byteBuffer.remaining());
      byteBufferToAdd.put(byteBuffer);
      byteBufferToAdd.rewind();
    }

    // Always copy the buffer info as it is retained until the track is finalized.
    BufferInfo bufferInfoToAdd = new BufferInfo();
    bufferInfoToAdd.set(
        /* newOffset= */ byteBufferToAdd.position(),
        /* newSize= */ byteBufferToAdd.remaining(),
        bufferInfo.presentationTimeUs,
        bufferInfo.flags);

    pendingSamplesBufferInfo.addLast(bufferInfoToAdd);
    pendingSamplesByteBuffer.addLast(byteBufferToAdd);
  }

  public int videoUnitTimebase() {
    return MimeTypes.isAudio(format.sampleMimeType)
        ? 48_000 // TODO: b/270583563 - Update these with actual values from mediaFormat.
        : 90_000;
  }
}
