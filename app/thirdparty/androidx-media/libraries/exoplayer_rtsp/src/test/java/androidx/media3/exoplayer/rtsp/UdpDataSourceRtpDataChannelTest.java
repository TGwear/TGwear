/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.rtsp;

import static com.google.common.truth.Truth.assertThat;

import androidx.media3.datasource.UdpDataSource;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit test for {@link UdpDataSourceRtpDataChannel}. */
@RunWith(AndroidJUnit4.class)
public class UdpDataSourceRtpDataChannelTest {

  @Test
  public void getInterleavedBinaryDataListener_returnsNull() {
    UdpDataSourceRtpDataChannel udpDataSourceRtpDataChannel =
        new UdpDataSourceRtpDataChannel(UdpDataSource.DEFAULT_SOCKET_TIMEOUT_MILLIS);

    assertThat(udpDataSourceRtpDataChannel.getInterleavedBinaryDataListener()).isNull();
  }
}
