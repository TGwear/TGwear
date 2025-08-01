/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.datasource.rtmp;

import static androidx.media3.common.util.Util.castNonNull;

import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.media3.common.C;
import androidx.media3.common.MediaLibraryInfo;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.datasource.BaseDataSource;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DataSpec;
import androidx.media3.datasource.TransferListener;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import io.antmedia.rtmp_client.RtmpClient;
import io.antmedia.rtmp_client.RtmpClient.RtmpIOException;
import java.io.IOException;

/** A Real-Time Messaging Protocol (RTMP) {@link DataSource}. */
@UnstableApi
public final class RtmpDataSource extends BaseDataSource {

  static {
    MediaLibraryInfo.registerModule("media3.datasource.rtmp");
  }

  /** {@link DataSource.Factory} for {@link RtmpDataSource} instances. */
  public static final class Factory implements DataSource.Factory {

    @Nullable private TransferListener transferListener;

    /**
     * Sets the {@link TransferListener} that will be used.
     *
     * <p>The default is {@code null}.
     *
     * <p>See {@link DataSource#addTransferListener(TransferListener)}.
     *
     * @param transferListener The listener that will be used.
     * @return This factory.
     */
    @CanIgnoreReturnValue
    public Factory setTransferListener(@Nullable TransferListener transferListener) {
      this.transferListener = transferListener;
      return this;
    }

    @Override
    public RtmpDataSource createDataSource() {
      RtmpDataSource dataSource = new RtmpDataSource();
      if (transferListener != null) {
        dataSource.addTransferListener(transferListener);
      }
      return dataSource;
    }
  }

  @Nullable private RtmpClient rtmpClient;
  @Nullable private Uri uri;

  public RtmpDataSource() {
    super(/* isNetwork= */ true);
  }

  @Override
  public long open(DataSpec dataSpec) throws RtmpIOException {
    transferInitializing(dataSpec);
    rtmpClient = new RtmpClient();
    rtmpClient.open(dataSpec.uri.toString(), false);

    this.uri = dataSpec.uri;
    transferStarted(dataSpec);
    return C.LENGTH_UNSET;
  }

  @Override
  public int read(byte[] buffer, int offset, int length) throws IOException {
    int bytesRead = castNonNull(rtmpClient).read(buffer, offset, length);
    if (bytesRead == -1) {
      return C.RESULT_END_OF_INPUT;
    }
    bytesTransferred(bytesRead);
    return bytesRead;
  }

  @Override
  public void close() {
    if (uri != null) {
      uri = null;
      transferEnded();
    }
    if (rtmpClient != null) {
      rtmpClient.close();
      rtmpClient = null;
    }
  }

  @Override
  @Nullable
  public Uri getUri() {
    return uri;
  }
}
