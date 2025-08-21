/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package androidx.media3.datasource;

import static java.lang.Math.min;

import android.net.Uri;
import androidx.media3.test.utils.DataSourceContractTest;
import androidx.media3.test.utils.TestUtil;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/** {@link DataSource} contract tests for {@link UdpDataSource}. */
@RunWith(AndroidJUnit4.class)
public class UdpDataSourceContractTest extends DataSourceContractTest {

  private UdpDataSource udpDataSource;
  private byte[] data;

  @Before
  public void setUp() {
    udpDataSource = new UdpDataSource();
    data = TestUtil.buildTestData(/* length= */ 256);
    PacketTrasmitterTransferListener transferListener = new PacketTrasmitterTransferListener(data);
    udpDataSource.addTransferListener(transferListener);
  }

  @Override
  protected DataSource createDataSource() {
    return udpDataSource;
  }

  @Override
  protected boolean unboundedReadsAreIndefinite() {
    return true;
  }

  @Override
  protected ImmutableList<TestResource> getTestResources() {
    return ImmutableList.of(
        new TestResource.Builder()
            .setName("local-udp-unicast-socket")
            .setUri("udp://localhost:" + findFreeUdpPort())
            .setExpectedBytes(data)
            .build());
  }

  @Override
  protected Uri getNotFoundUri() {
    return Uri.parse("udp://notfound.invalid:12345");
  }

  @Test
  @Ignore("UdpDataSource doesn't support DataSpec's position or length [internal: b/175856954]")
  @Override
  public void dataSpecWithPosition_readUntilEnd() {}

  @Test
  @Ignore("UdpDataSource doesn't support DataSpec's position or length [internal: b/175856954]")
  @Override
  public void dataSpecWithLength_readExpectedRange() {}

  @Test
  @Ignore("UdpDataSource doesn't support DataSpec's position or length [internal: b/175856954]")
  @Override
  public void dataSpecWithLength_readUntilEndInTwoParts() {}

  @Test
  @Ignore("UdpDataSource doesn't support DataSpec's position or length [internal: b/175856954]")
  @Override
  public void dataSpecWithPositionAndLength_readExpectedRange() {}

  @Test
  @Ignore("UdpDataSource doesn't support DataSpec's position or length [internal: b/175856954]")
  @Override
  public void dataSpecWithPositionAtEnd_readsZeroBytes() {}

  @Test
  @Ignore("UdpDataSource doesn't support DataSpec's position or length [internal: b/175856954]")
  @Override
  public void dataSpecWithPositionAtEndAndLength_readsZeroBytes() {}

  @Test
  @Ignore("UdpDataSource doesn't support DataSpec's position or length [internal: b/175856954]")
  @Override
  public void dataSpecWithPositionOutOfRange_throwsPositionOutOfRangeException() {}

  @Test
  @Ignore("UdpDataSource doesn't support DataSpec's position or length [internal: b/175856954]")
  @Override
  public void dataSpecWithEndPositionOutOfRange_readsToEnd() {}

  /**
   * Finds a free UDP port in the range of unreserved ports 50000-60000 that can be used from the
   * test or throws an {@link IllegalStateException} if no port is available.
   *
   * <p>There is no guarantee that the port returned will still be available as another process may
   * occupy it in the mean time.
   */
  private static int findFreeUdpPort() {
    for (int i = 50000; i <= 60000; i++) {
      try {
        new DatagramSocket(i).close();
        return i;
      } catch (SocketException e) {
        // Port is occupied, continue to next port.
      }
    }
    throw new IllegalStateException();
  }

  /**
   * A {@link TransferListener} that triggers UDP packet transmissions back to the UDP data source.
   */
  private static class PacketTrasmitterTransferListener implements TransferListener {
    private final byte[] data;

    public PacketTrasmitterTransferListener(byte[] data) {
      this.data = data;
    }

    @Override
    public void onTransferInitializing(DataSource source, DataSpec dataSpec, boolean isNetwork) {}

    @Override
    public void onTransferStart(DataSource source, DataSpec dataSpec, boolean isNetwork) {
      String host = dataSpec.uri.getHost();
      int port = dataSpec.uri.getPort();
      try (DatagramSocket socket = new DatagramSocket()) {
        // Split data in packets of up to 64 bytes: UDP is unreliable, it may lose, duplicate or
        // re-order packets. However, we want to transmit more than one UDP packets to thoroughly
        // test the UDP data source. We assume that UDP delivery within the same host is reliable.
        for (int offset = 0; offset < data.length; offset += 64) {
          int packetLength = min(64, data.length - offset);
          DatagramPacket packet =
              new DatagramPacket(data, offset, packetLength, InetAddress.getByName(host), port);
          socket.send(packet);
        }
      } catch (IOException e) {
        throw new IllegalStateException(e);
      }
    }

    @Override
    public void onBytesTransferred(
        DataSource source, DataSpec dataSpec, boolean isNetwork, int bytesTransferred) {}

    @Override
    public void onTransferEnd(DataSource source, DataSpec dataSpec, boolean isNetwork) {}
  }
}
