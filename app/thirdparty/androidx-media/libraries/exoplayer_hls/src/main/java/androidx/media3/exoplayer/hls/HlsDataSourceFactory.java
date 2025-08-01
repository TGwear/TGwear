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

/** Creates {@link DataSource}s for HLS playlists, encryption and media chunks. */
@UnstableApi
public interface HlsDataSourceFactory {

  /**
   * Creates a {@link DataSource} for the given data type.
   *
   * @param dataType The {@link C.DataType} for which the {@link DataSource} will be used.
   * @return A {@link DataSource} for the given data type.
   */
  DataSource createDataSource(@C.DataType int dataType);
}
