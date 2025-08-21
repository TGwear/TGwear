/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.datasource.rtmp;

import androidx.annotation.Nullable;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.TransferListener;

/**
 * @deprecated Use {@link RtmpDataSource.Factory} instead.
 */
@Deprecated
@UnstableApi
public final class RtmpDataSourceFactory implements DataSource.Factory {

  @Nullable private final TransferListener listener;

  public RtmpDataSourceFactory() {
    this(null);
  }

  /**
   * @param listener An optional listener.
   */
  public RtmpDataSourceFactory(@Nullable TransferListener listener) {
    this.listener = listener;
  }

  @Override
  public RtmpDataSource createDataSource() {
    RtmpDataSource dataSource = new RtmpDataSource();
    if (listener != null) {
      dataSource.addTransferListener(listener);
    }
    return dataSource;
  }
}
