/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.mediacodec;

import static androidx.media3.common.util.Assertions.checkArgument;

import androidx.annotation.IntRange;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.media3.common.C;
import androidx.media3.decoder.DecoderInputBuffer;
import java.nio.ByteBuffer;

/** Buffer to which multiple sample buffers can be appended for batch processing */
/* package */ final class BatchBuffer extends DecoderInputBuffer {

  /** The default maximum number of samples that can be appended before the buffer is full. */
  public static final int DEFAULT_MAX_SAMPLE_COUNT = 32;

  /**
   * The maximum size of the buffer in bytes. This prevents excessive memory usage for high bitrate
   * streams. The limit is equivalent of 75s of mp3 at highest bitrate (320kb/s) and 30s of AAC LC
   * at highest bitrate (800kb/s). That limit is ignored for the first sample.
   */
  @VisibleForTesting /* package */ static final int MAX_SIZE_BYTES = 3 * 1000 * 1024;

  private long lastSampleTimeUs;
  private int sampleCount;
  private int maxSampleCount;

  public BatchBuffer() {
    super(DecoderInputBuffer.BUFFER_REPLACEMENT_MODE_DIRECT);
    maxSampleCount = DEFAULT_MAX_SAMPLE_COUNT;
  }

  @Override
  public void clear() {
    super.clear();
    sampleCount = 0;
  }

  /** Sets the maximum number of samples that can be appended before the buffer is full. */
  public void setMaxSampleCount(@IntRange(from = 1) int maxSampleCount) {
    checkArgument(maxSampleCount > 0);
    this.maxSampleCount = maxSampleCount;
  }

  /**
   * Returns the timestamp of the first sample in the buffer. The return value is undefined if
   * {@link #hasSamples()} is {@code false}.
   */
  public long getFirstSampleTimeUs() {
    return timeUs;
  }

  /**
   * Returns the timestamp of the last sample in the buffer. The return value is undefined if {@link
   * #hasSamples()} is {@code false}.
   */
  public long getLastSampleTimeUs() {
    return lastSampleTimeUs;
  }

  /** Returns the number of samples in the buffer. */
  public int getSampleCount() {
    return sampleCount;
  }

  /** Returns whether the buffer contains one or more samples. */
  public boolean hasSamples() {
    return sampleCount > 0;
  }

  /**
   * Attempts to append the provided buffer.
   *
   * @param buffer The buffer to try and append.
   * @return Whether the buffer was successfully appended.
   * @throws IllegalArgumentException If the {@code buffer} is encrypted, has supplemental data, or
   *     is an end of stream buffer, none of which are supported.
   */
  public boolean append(DecoderInputBuffer buffer) {
    checkArgument(!buffer.isEncrypted());
    checkArgument(!buffer.hasSupplementalData());
    checkArgument(!buffer.isEndOfStream());
    if (!canAppendSampleBuffer(buffer)) {
      return false;
    }
    if (sampleCount++ == 0) {
      timeUs = buffer.timeUs;
      if (buffer.isKeyFrame()) {
        setFlags(C.BUFFER_FLAG_KEY_FRAME);
      }
    }
    @Nullable ByteBuffer bufferData = buffer.data;
    if (bufferData != null) {
      ensureSpaceForWrite(bufferData.remaining());
      data.put(bufferData);
    }
    lastSampleTimeUs = buffer.timeUs;
    return true;
  }

  private boolean canAppendSampleBuffer(DecoderInputBuffer buffer) {
    if (!hasSamples()) {
      // Always allow appending when the buffer is empty, else no progress can be made.
      return true;
    }
    if (sampleCount >= maxSampleCount) {
      return false;
    }
    @Nullable ByteBuffer bufferData = buffer.data;
    if (bufferData != null
        && data != null
        && data.position() + bufferData.remaining() > MAX_SIZE_BYTES) {
      return false;
    }
    return true;
  }
}
