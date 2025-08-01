/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.source.mediaparser;

import android.media.MediaParser;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.media3.common.C;
import androidx.media3.common.DataReader;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.common.util.Util;
import java.io.IOException;

/** {@link MediaParser.SeekableInputReader} implementation wrapping a {@link DataReader}. */
@RequiresApi(30)
@UnstableApi
public final class InputReaderAdapterV30 implements MediaParser.SeekableInputReader {

  @Nullable private DataReader dataReader;
  private long resourceLength;
  private long currentPosition;
  private long lastSeekPosition;

  /**
   * Sets the wrapped {@link DataReader}.
   *
   * @param dataReader The {@link DataReader} to wrap.
   * @param length The length of the resource from which {@code dataReader} reads.
   */
  public void setDataReader(DataReader dataReader, long length) {
    this.dataReader = dataReader;
    resourceLength = length;
    lastSeekPosition = C.INDEX_UNSET;
  }

  /** Sets the absolute position in the resource from which the wrapped {@link DataReader} reads. */
  public void setCurrentPosition(long position) {
    currentPosition = position;
  }

  /**
   * Returns the last value passed to {@link #seekToPosition(long)} and sets the stored value to
   * {@link C#INDEX_UNSET}.
   */
  public long getAndResetSeekPosition() {
    long lastSeekPosition = this.lastSeekPosition;
    this.lastSeekPosition = C.INDEX_UNSET;
    return lastSeekPosition;
  }

  // SeekableInputReader implementation.

  @Override
  public void seekToPosition(long position) {
    lastSeekPosition = position;
  }

  @Override
  public int read(byte[] bytes, int offset, int readLength) throws IOException {
    int bytesRead = Util.castNonNull(dataReader).read(bytes, offset, readLength);
    currentPosition += bytesRead;
    return bytesRead;
  }

  @Override
  public long getPosition() {
    return currentPosition;
  }

  @Override
  public long getLength() {
    return resourceLength;
  }
}
