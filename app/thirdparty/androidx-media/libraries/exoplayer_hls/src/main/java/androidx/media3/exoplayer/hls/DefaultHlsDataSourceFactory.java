/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.hls;

import androidx.media3.common.C;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.datasource.DataSource;

/** Default implementation of {@link HlsDataSourceFactory}. */
@UnstableApi
public final class DefaultHlsDataSourceFactory implements HlsDataSourceFactory {

  private final DataSource.Factory dataSourceFactory;

  /**
   * @param dataSourceFactory The {@link DataSource.Factory} to use for all data types.
   */
  public DefaultHlsDataSourceFactory(DataSource.Factory dataSourceFactory) {
    this.dataSourceFactory = dataSourceFactory;
  }

  @Override
  public DataSource createDataSource(@C.DataType int dataType) {
    return dataSourceFactory.createDataSource();
  }
}
