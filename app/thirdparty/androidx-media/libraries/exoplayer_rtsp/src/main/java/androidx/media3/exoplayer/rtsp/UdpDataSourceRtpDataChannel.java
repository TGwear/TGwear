/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.rtsp;

import static androidx.media3.common.util.Assertions.checkArgument;
import static androidx.media3.common.util.Assertions.checkState;

import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.media3.common.C;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.DataSpec;
import androidx.media3.datasource.TransferListener;
import androidx.media3.datasource.UdpDataSource;
import com.google.common.primitives.Ints;
import java.io.IOException;

/** An {@link RtpDataChannel} for UDP transport. */
/* package */ final class UdpDataSourceRtpDataChannel implements RtpDataChannel {

  private static final String DEFAULT_UDP_TRANSPORT_FORMAT = "RTP/AVP;unicast;client_port=%d-%d";

  private final UdpDataSource dataSource;

  /** The associated RTCP channel; {@code null} if the current channel is an RTCP channel. */
  @Nullable private UdpDataSourceRtpDataChannel rtcpChannel;

  /**
   * Creates a new instance.
   *
   * @param socketTimeoutMs The timeout for {@link #read} in milliseconds.
   */
  public UdpDataSourceRtpDataChannel(long socketTimeoutMs) {
    dataSource =
        new UdpDataSource(UdpDataSource.DEFAULT_MAX_PACKET_SIZE, Ints.checkedCast(socketTimeoutMs));
  }

  @Override
  public String getTransport() {
    int dataPortNumber = getLocalPort();
    checkState(dataPortNumber != C.INDEX_UNSET); // Assert open() is called.
    return Util.formatInvariant(DEFAULT_UDP_TRANSPORT_FORMAT, dataPortNumber, dataPortNumber + 1);
  }

  @Override
  public int getLocalPort() {
    int port = dataSource.getLocalPort();
    return port == UdpDataSource.UDP_PORT_UNSET ? C.INDEX_UNSET : port;
  }

  @Override
  public boolean needsClosingOnLoadCompletion() {
    return true;
  }

  @Nullable
  @Override
  public RtspMessageChannel.InterleavedBinaryDataListener getInterleavedBinaryDataListener() {
    return null;
  }

  @Override
  public void addTransferListener(TransferListener transferListener) {
    dataSource.addTransferListener(transferListener);
  }

  @Override
  public long open(DataSpec dataSpec) throws IOException {
    return dataSource.open(dataSpec);
  }

  @Nullable
  @Override
  public Uri getUri() {
    return dataSource.getUri();
  }

  @Override
  public void close() {
    dataSource.close();

    if (rtcpChannel != null) {
      rtcpChannel.close();
    }
  }

  @Override
  public int read(byte[] buffer, int offset, int length) throws IOException {
    try {
      return dataSource.read(buffer, offset, length);
    } catch (UdpDataSource.UdpDataSourceException e) {
      if (e.reason == PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT) {
        return C.RESULT_END_OF_INPUT;
      } else {
        throw e;
      }
    }
  }

  public void setRtcpChannel(UdpDataSourceRtpDataChannel rtcpChannel) {
    checkArgument(this != rtcpChannel);
    this.rtcpChannel = rtcpChannel;
  }
}
