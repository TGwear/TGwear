/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.datasource.cache;

import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This is a subclass of {@link BufferedOutputStream} with a {@link #reset(OutputStream)} method
 * that allows an instance to be re-used with another underlying output stream.
 */
/* package */ final class ReusableBufferedOutputStream extends BufferedOutputStream {

  private boolean closed;

  public ReusableBufferedOutputStream(OutputStream out) {
    super(out);
  }

  public ReusableBufferedOutputStream(OutputStream out, int size) {
    super(out, size);
  }

  @Override
  public void close() throws IOException {
    closed = true;

    Throwable thrown = null;
    try {
      flush();
    } catch (Throwable e) {
      thrown = e;
    }
    try {
      out.close();
    } catch (Throwable e) {
      if (thrown == null) {
        thrown = e;
      }
    }
    if (thrown != null) {
      Util.sneakyThrow(thrown);
    }
  }

  /**
   * Resets this stream and uses the given output stream for writing. This stream must be closed
   * before resetting.
   *
   * @param out New output stream to be used for writing.
   * @throws IllegalStateException If the stream isn't closed.
   */
  public void reset(OutputStream out) {
    Assertions.checkState(closed);
    this.out = out;
    count = 0;
    closed = false;
  }
}
