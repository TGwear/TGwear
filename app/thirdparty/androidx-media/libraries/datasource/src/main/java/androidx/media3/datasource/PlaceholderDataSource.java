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
import androidx.media3.common.util.UnstableApi;
import java.io.IOException;

/** A DataSource which provides no data. {@link #open(DataSpec)} throws {@link IOException}. */
@UnstableApi
public final class PlaceholderDataSource implements DataSource {

  public static final PlaceholderDataSource INSTANCE = new PlaceholderDataSource();

  /** A factory that produces {@link PlaceholderDataSource}. */
  public static final Factory FACTORY = PlaceholderDataSource::new;

  private PlaceholderDataSource() {}

  @Override
  public void addTransferListener(TransferListener transferListener) {
    // Do nothing.
  }

  @Override
  public long open(DataSpec dataSpec) throws IOException {
    throw new IOException("PlaceholderDataSource cannot be opened");
  }

  @Override
  public int read(byte[] buffer, int offset, int length) {
    throw new UnsupportedOperationException();
  }

  @Override
  @Nullable
  public Uri getUri() {
    return null;
  }

  @Override
  public void close() {
    // do nothing.
  }
}
