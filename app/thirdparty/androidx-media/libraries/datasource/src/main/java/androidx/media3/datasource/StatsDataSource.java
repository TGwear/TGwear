/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.datasource;

import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.media3.common.C;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.UnstableApi;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * {@link DataSource} wrapper which keeps track of bytes transferred, redirected uris, and response
 * headers.
 */
@UnstableApi
public final class StatsDataSource implements DataSource {

  private final DataSource dataSource;

  private long bytesRead;
  private Uri lastOpenedUri;
  private Map<String, List<String>> lastResponseHeaders;

  /**
   * Creates the stats data source.
   *
   * @param dataSource The wrapped {@link DataSource}.
   */
  public StatsDataSource(DataSource dataSource) {
    this.dataSource = Assertions.checkNotNull(dataSource);
    lastOpenedUri = Uri.EMPTY;
    lastResponseHeaders = Collections.emptyMap();
  }

  /** Resets the number of bytes read as returned from {@link #getBytesRead()} to zero. */
  public void resetBytesRead() {
    bytesRead = 0;
  }

  /** Returns the total number of bytes that have been read from the data source. */
  public long getBytesRead() {
    return bytesRead;
  }

  /**
   * Returns the {@link Uri} associated with the last {@link #open(DataSpec)} call. If redirection
   * occurred, this is the redirected uri. Returns {@link Uri#EMPTY} if {@link #open(DataSpec)} has
   * never been called.
   */
  public Uri getLastOpenedUri() {
    return lastOpenedUri;
  }

  /** Returns the response headers associated with the last {@link #open(DataSpec)} call. */
  public Map<String, List<String>> getLastResponseHeaders() {
    return lastResponseHeaders;
  }

  @Override
  public void addTransferListener(TransferListener transferListener) {
    Assertions.checkNotNull(transferListener);
    dataSource.addTransferListener(transferListener);
  }

  @Override
  public long open(DataSpec dataSpec) throws IOException {
    // Reassign defaults in case dataSource.open throws an exception.
    lastOpenedUri = dataSpec.uri;
    lastResponseHeaders = Collections.emptyMap();
    try {
      return dataSource.open(dataSpec);
    } finally {
      // TODO: b/373321956 - Remove this null-tolerance when we've fixed all DataSource
      //  implementations to return a non-null URI after a failed open() call and before close()
      //  (and updated the DataSourceContractTest to enforce this).
      Uri upstreamUri = getUri();
      if (upstreamUri != null) {
        lastOpenedUri = upstreamUri;
      }
      lastResponseHeaders = getResponseHeaders();
    }
  }

  @Override
  public int read(byte[] buffer, int offset, int length) throws IOException {
    int bytesRead = dataSource.read(buffer, offset, length);
    if (bytesRead != C.RESULT_END_OF_INPUT) {
      this.bytesRead += bytesRead;
    }
    return bytesRead;
  }

  @Override
  @Nullable
  public Uri getUri() {
    return dataSource.getUri();
  }

  @Override
  public Map<String, List<String>> getResponseHeaders() {
    return dataSource.getResponseHeaders();
  }

  @Override
  public void close() throws IOException {
    dataSource.close();
  }
}
