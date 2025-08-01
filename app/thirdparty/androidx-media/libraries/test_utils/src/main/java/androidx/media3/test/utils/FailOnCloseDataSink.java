/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.test.utils;

import androidx.media3.common.C;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.datasource.DataSink;
import androidx.media3.datasource.DataSpec;
import androidx.media3.datasource.cache.Cache;
import androidx.media3.datasource.cache.CacheDataSink;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A {@link DataSink} that can simulate caching the bytes being written to it, and then failing to
 * persist them when {@link #close()} is called.
 */
@UnstableApi
public final class FailOnCloseDataSink implements DataSink {

  /** Factory to create a {@link FailOnCloseDataSink}. */
  public static final class Factory implements DataSink.Factory {

    private final Cache cache;
    private final AtomicBoolean failOnClose;

    /**
     * Creates an instance.
     *
     * @param cache The cache to write to when not in fail-on-close mode.
     * @param failOnClose An {@link AtomicBoolean} whose value is read in each call to {@link #open}
     *     to determine whether to enable fail-on-close for the read that's being started.
     */
    public Factory(Cache cache, AtomicBoolean failOnClose) {
      this.cache = cache;
      this.failOnClose = failOnClose;
    }

    @Override
    public DataSink createDataSink() {
      return new FailOnCloseDataSink(cache, failOnClose);
    }
  }

  private final CacheDataSink wrappedSink;
  private final AtomicBoolean failOnClose;
  private boolean currentReadFailOnClose;

  /**
   * Creates an instance.
   *
   * @param cache The cache to write to when not in fail-on-close mode.
   * @param failOnClose An {@link AtomicBoolean} whose value is read in each call to {@link #open}
   *     to determine whether to enable fail-on-close for the read that's being started.
   */
  public FailOnCloseDataSink(Cache cache, AtomicBoolean failOnClose) {
    this.wrappedSink = new CacheDataSink(cache, /* fragmentSize= */ C.LENGTH_UNSET);
    this.failOnClose = failOnClose;
  }

  @Override
  public void open(DataSpec dataSpec) throws IOException {
    currentReadFailOnClose = failOnClose.get();
    if (currentReadFailOnClose) {
      return;
    }
    wrappedSink.open(dataSpec);
  }

  @Override
  public void write(byte[] buffer, int offset, int length) throws IOException {
    if (currentReadFailOnClose) {
      return;
    }
    wrappedSink.write(buffer, offset, length);
  }

  @Override
  public void close() throws IOException {
    if (currentReadFailOnClose) {
      throw new IOException("Fail on close");
    }
    wrappedSink.close();
  }
}
