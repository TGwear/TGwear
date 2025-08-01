/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.datasource.cache;

import static com.google.common.truth.Truth.assertThat;

import androidx.media3.common.util.Util;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import java.io.ByteArrayOutputStream;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Tests {@link ReusableBufferedOutputStream}. */
@RunWith(AndroidJUnit4.class)
public final class ReusableBufferedOutputStreamTest {

  private static final byte[] TEST_DATA_1 = Util.getUtf8Bytes("test data 1");
  private static final byte[] TEST_DATA_2 = Util.getUtf8Bytes("2 test data");

  @Test
  public void reset() throws Exception {
    ByteArrayOutputStream byteArrayOutputStream1 = new ByteArrayOutputStream(1000);
    ReusableBufferedOutputStream outputStream =
        new ReusableBufferedOutputStream(byteArrayOutputStream1, 1000);
    outputStream.write(TEST_DATA_1);
    outputStream.close();

    ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream(1000);
    outputStream.reset(byteArrayOutputStream2);
    outputStream.write(TEST_DATA_2);
    outputStream.close();

    assertThat(byteArrayOutputStream1.toByteArray()).isEqualTo(TEST_DATA_1);
    assertThat(byteArrayOutputStream2.toByteArray()).isEqualTo(TEST_DATA_2);
  }
}
