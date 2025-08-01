/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.rtsp;

import static androidx.media3.common.util.Assertions.checkState;
import static java.lang.Math.min;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.media3.common.C;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.BaseDataSource;
import androidx.media3.datasource.DataSpec;
import androidx.media3.exoplayer.rtsp.RtspMessageChannel.InterleavedBinaryDataListener;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;

/** An {@link RtpDataChannel} that transfers received data in-memory. */
/* package */ final class TransferRtpDataChannel extends BaseDataSource
    implements RtpDataChannel, RtspMessageChannel.InterleavedBinaryDataListener {

  private static final String DEFAULT_TCP_TRANSPORT_FORMAT =
      "RTP/AVP/TCP;unicast;interleaved=%d-%d";

  private final LinkedBlockingQueue<byte[]> packetQueue;
  private final long pollTimeoutMs;

  private byte[] unreadData;
  private int channelNumber;

  /**
   * Creates a new instance.
   *
   * @param pollTimeoutMs The number of milliseconds which {@link #read} waits for a packet to be
   *     available. After the time has expired, {@link C#RESULT_END_OF_INPUT} is returned.
   */
  public TransferRtpDataChannel(long pollTimeoutMs) {
    super(/* isNetwork= */ true);
    this.pollTimeoutMs = pollTimeoutMs;
    packetQueue = new LinkedBlockingQueue<>();
    unreadData = new byte[0];
    channelNumber = C.INDEX_UNSET;
  }

  @Override
  public String getTransport() {
    checkState(channelNumber != C.INDEX_UNSET); // Assert open() is called.
    return Util.formatInvariant(DEFAULT_TCP_TRANSPORT_FORMAT, channelNumber, channelNumber + 1);
  }

  @Override
  public int getLocalPort() {
    return channelNumber;
  }

  @Override
  public boolean needsClosingOnLoadCompletion() {
    // TCP channel is managed by the RTSP mesasge channel and does not need closing from here.
    return false;
  }

  @Override
  public InterleavedBinaryDataListener getInterleavedBinaryDataListener() {
    return this;
  }

  @Override
  public long open(DataSpec dataSpec) {
    this.channelNumber = dataSpec.uri.getPort();
    return C.LENGTH_UNSET;
  }

  @Override
  public void close() {}

  @Nullable
  @Override
  public Uri getUri() {
    return null;
  }

  @Override
  public int read(byte[] buffer, int offset, int length) {
    if (length == 0) {
      return 0;
    }

    int bytesRead = 0;
    int bytesToRead = min(length, unreadData.length);
    System.arraycopy(unreadData, /* srcPos= */ 0, buffer, offset, bytesToRead);
    bytesRead += bytesToRead;
    unreadData = Arrays.copyOfRange(unreadData, bytesToRead, unreadData.length);

    if (bytesRead == length) {
      return bytesRead;
    }

    @Nullable byte[] data;
    try {
      data = packetQueue.poll(pollTimeoutMs, MILLISECONDS);
      if (data == null) {
        return C.RESULT_END_OF_INPUT;
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      return C.RESULT_END_OF_INPUT;
    }

    bytesToRead = min(length - bytesRead, data.length);
    System.arraycopy(data, /* srcPos= */ 0, buffer, offset + bytesRead, bytesToRead);
    if (bytesToRead < data.length) {
      unreadData = Arrays.copyOfRange(data, bytesToRead, data.length);
    }
    return bytesRead + bytesToRead;
  }

  @Override
  public void onInterleavedBinaryDataReceived(byte[] data) {
    packetQueue.add(data);
  }
}
