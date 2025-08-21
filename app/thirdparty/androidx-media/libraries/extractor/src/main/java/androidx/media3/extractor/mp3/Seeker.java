/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.mp3;

import androidx.media3.common.C;
import androidx.media3.extractor.SeekMap;

/**
 * {@link SeekMap} that provides the end position of audio data and also allows mapping from
 * position (byte offset) back to time, which can be used to work out the new sample basis timestamp
 * after seeking and resynchronization.
 */
/* package */ interface Seeker extends SeekMap {

  /**
   * Maps a position (byte offset) to a corresponding sample timestamp.
   *
   * @param position A seek position (byte offset) relative to the start of the stream.
   * @return The corresponding timestamp of the next sample to be read, in microseconds.
   */
  long getTimeUs(long position);

  /**
   * Returns the position (byte offset) in the stream that is immediately after audio data, or
   * {@link C#INDEX_UNSET} if not known.
   */
  long getDataEndPosition();

  /**
   * Returns the average bitrate (usually derived from the duration and length of the file), or
   * {@link C#RATE_UNSET_INT} if not known.
   */
  int getAverageBitrate();

  /** A {@link Seeker} that does not support seeking through audio data. */
  /* package */ class UnseekableSeeker extends SeekMap.Unseekable implements Seeker {

    public UnseekableSeeker() {
      super(/* durationUs= */ C.TIME_UNSET);
    }

    @Override
    public long getTimeUs(long position) {
      return 0;
    }

    @Override
    public long getDataEndPosition() {
      // Position unset as we do not know the data end position. Note that returning 0 doesn't work.
      return C.INDEX_UNSET;
    }

    @Override
    public int getAverageBitrate() {
      return C.RATE_UNSET_INT;
    }
  }
}
