/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.datasource;

import static androidx.media3.common.util.Util.castNonNull;

import androidx.annotation.Nullable;
import androidx.media3.common.C;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.UnstableApi;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

/** A {@link DataSink} for writing to a byte array. */
@UnstableApi
public final class ByteArrayDataSink implements DataSink {

  private @MonotonicNonNull ByteArrayOutputStream stream;

  @Override
  public void open(DataSpec dataSpec) {
    if (dataSpec.length == C.LENGTH_UNSET) {
      stream = new ByteArrayOutputStream();
    } else {
      Assertions.checkArgument(dataSpec.length <= Integer.MAX_VALUE);
      stream = new ByteArrayOutputStream((int) dataSpec.length);
    }
  }

  @Override
  public void close() throws IOException {
    castNonNull(stream).close();
  }

  @Override
  public void write(byte[] buffer, int offset, int length) {
    castNonNull(stream).write(buffer, offset, length);
  }

  /**
   * Returns the data written to the sink since the last call to {@link #open(DataSpec)}, or null if
   * {@link #open(DataSpec)} has never been called.
   */
  @Nullable
  public byte[] getData() {
    return stream == null ? null : stream.toByteArray();
  }
}
