/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor;

import static java.lang.Math.min;

import androidx.annotation.Nullable;
import androidx.media3.common.C;
import androidx.media3.common.DataReader;
import androidx.media3.common.Format;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.UnstableApi;
import java.io.EOFException;
import java.io.IOException;

/** A {@link TrackOutput} that consumes and discards all reported samples. */
@UnstableApi
public final class DiscardingTrackOutput implements TrackOutput {

  // Even though read data is discarded, data source implementations could be making use of the
  // buffer contents. For example, caches. So we cannot use a static field for this which could be
  // shared between different threads.
  private final byte[] readBuffer;

  /** Creates discarding track output. */
  public DiscardingTrackOutput() {
    readBuffer = new byte[4096];
  }

  @Override
  public void format(Format format) {
    // Do nothing.
  }

  @Override
  public int sampleData(
      DataReader input, int length, boolean allowEndOfInput, @SampleDataPart int sampleDataPart)
      throws IOException {
    int bytesToSkipByReading = min(readBuffer.length, length);
    int bytesSkipped = input.read(readBuffer, /* offset= */ 0, bytesToSkipByReading);
    if (bytesSkipped == C.RESULT_END_OF_INPUT) {
      if (allowEndOfInput) {
        return C.RESULT_END_OF_INPUT;
      }
      throw new EOFException();
    }
    return bytesSkipped;
  }

  @Override
  public void sampleData(ParsableByteArray data, int length, @SampleDataPart int sampleDataPart) {
    data.skipBytes(length);
  }

  @Override
  public void sampleMetadata(
      long timeUs,
      @C.BufferFlags int flags,
      int size,
      int offset,
      @Nullable CryptoData cryptoData) {
    // Do nothing.
  }
}
