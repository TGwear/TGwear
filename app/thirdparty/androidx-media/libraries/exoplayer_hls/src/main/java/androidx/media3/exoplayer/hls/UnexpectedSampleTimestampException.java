/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.hls;

import androidx.media3.common.C;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.source.SampleQueue;
import androidx.media3.exoplayer.source.chunk.MediaChunk;
import java.io.IOException;

/**
 * Thrown when an attempt is made to write a sample to a {@link SampleQueue} whose timestamp is
 * inconsistent with the chunk from which it originates.
 */
/* package */ final class UnexpectedSampleTimestampException extends IOException {

  /** The {@link MediaChunk} that contained the rejected sample. */
  public final MediaChunk mediaChunk;

  /**
   * The timestamp of the last sample that was loaded from {@link #mediaChunk} and successfully
   * written to the {@link SampleQueue}, in microseconds. {@link C#TIME_UNSET} if the first sample
   * in the chunk was rejected.
   */
  public final long lastAcceptedSampleTimeUs;

  /** The timestamp of the rejected sample, in microseconds. */
  public final long rejectedSampleTimeUs;

  /**
   * Constructs an instance.
   *
   * @param mediaChunk The {@link MediaChunk} with the unexpected sample timestamp.
   * @param lastAcceptedSampleTimeUs The timestamp of the last sample that was loaded from the chunk
   *     and successfully written to the {@link SampleQueue}, in microseconds. {@link C#TIME_UNSET}
   *     if the first sample in the chunk was rejected.
   * @param rejectedSampleTimeUs The timestamp of the rejected sample, in microseconds.
   */
  public UnexpectedSampleTimestampException(
      MediaChunk mediaChunk, long lastAcceptedSampleTimeUs, long rejectedSampleTimeUs) {
    super(
        "Unexpected sample timestamp: "
            + Util.usToMs(rejectedSampleTimeUs)
            + " in chunk ["
            + mediaChunk.startTimeUs
            + ", "
            + mediaChunk.endTimeUs
            + "]");
    this.mediaChunk = mediaChunk;
    this.lastAcceptedSampleTimeUs = lastAcceptedSampleTimeUs;
    this.rejectedSampleTimeUs = rejectedSampleTimeUs;
  }
}
