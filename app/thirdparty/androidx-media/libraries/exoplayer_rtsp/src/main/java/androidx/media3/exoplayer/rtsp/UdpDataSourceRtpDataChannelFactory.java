/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.rtsp;

import androidx.media3.datasource.DataSourceUtil;
import java.io.IOException;

/** Factory for {@link UdpDataSourceRtpDataChannel}. */
/* package */ final class UdpDataSourceRtpDataChannelFactory implements RtpDataChannel.Factory {

  private final long socketTimeoutMs;

  /**
   * Creates a new instance.
   *
   * @param socketTimeoutMs A positive number of milliseconds to wait before lack of received RTP
   *     packets is treated as the end of input.
   */
  public UdpDataSourceRtpDataChannelFactory(long socketTimeoutMs) {
    this.socketTimeoutMs = socketTimeoutMs;
  }

  @Override
  public RtpDataChannel createAndOpenDataChannel(int trackId) throws IOException {
    UdpDataSourceRtpDataChannel firstChannel = new UdpDataSourceRtpDataChannel(socketTimeoutMs);
    UdpDataSourceRtpDataChannel secondChannel = new UdpDataSourceRtpDataChannel(socketTimeoutMs);

    try {
      // From RFC3550 Section 11: "For UDP and similar protocols, RTP SHOULD use an even destination
      // port number and the corresponding RTCP stream SHOULD use the next higher (odd) destination
      // port number". Some RTSP servers are strict about this rule. We open a data channel first,
      // and depending its port number, open the next data channel with a port number that is either
      // the higher or the lower.

      // Using port zero will cause the system to generate a port.
      firstChannel.open(RtpUtils.getIncomingRtpDataSpec(/* portNumber= */ 0));
      int firstPort = firstChannel.getLocalPort();
      boolean isFirstPortEven = firstPort % 2 == 0;
      int portToOpen = isFirstPortEven ? firstPort + 1 : firstPort - 1;
      secondChannel.open(RtpUtils.getIncomingRtpDataSpec(/* portNumber= */ portToOpen));

      if (isFirstPortEven) {
        firstChannel.setRtcpChannel(secondChannel);
        return firstChannel;
      } else {
        secondChannel.setRtcpChannel(firstChannel);
        return secondChannel;
      }
    } catch (IOException e) {
      DataSourceUtil.closeQuietly(firstChannel);
      DataSourceUtil.closeQuietly(secondChannel);
      throw e;
    }
  }

  @Override
  public RtpDataChannel.Factory createFallbackDataChannelFactory() {
    return new TransferRtpDataChannelFactory(/* timeoutMs= */ socketTimeoutMs);
  }
}
