/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.datasource;

import androidx.annotation.Nullable;
import androidx.media3.common.C;
import androidx.media3.common.util.UnstableApi;
import java.io.IOException;
import java.util.Arrays;

/** Utility methods for {@link DataSource}. */
@UnstableApi
public final class DataSourceUtil {

  private DataSourceUtil() {}

  /**
   * Reads data from the specified opened {@link DataSource} until it ends, and returns a byte array
   * containing the read data.
   *
   * @param dataSource The source from which to read.
   * @return The concatenation of all read data.
   * @throws IOException If an error occurs reading from the source.
   */
  public static byte[] readToEnd(DataSource dataSource) throws IOException {
    byte[] data = new byte[1024];
    int position = 0;
    int bytesRead = 0;
    while (bytesRead != C.RESULT_END_OF_INPUT) {
      if (position == data.length) {
        data = Arrays.copyOf(data, data.length * 2);
      }
      bytesRead = dataSource.read(data, position, data.length - position);
      if (bytesRead != C.RESULT_END_OF_INPUT) {
        position += bytesRead;
      }
    }
    return Arrays.copyOf(data, position);
  }

  /**
   * Reads {@code length} bytes from the specified opened {@link DataSource}, and returns a byte
   * array containing the read data.
   *
   * @param dataSource The source from which to read.
   * @param length The number of bytes to read.
   * @return The read data.
   * @throws IOException If an error occurs reading from the source.
   * @throws IllegalStateException If the end of the source was reached before {@code length} bytes
   *     could be read.
   */
  public static byte[] readExactly(DataSource dataSource, int length) throws IOException {
    byte[] data = new byte[length];
    int position = 0;
    while (position < length) {
      int bytesRead = dataSource.read(data, position, data.length - position);
      if (bytesRead == C.RESULT_END_OF_INPUT) {
        throw new IllegalStateException(
            "Not enough data could be read: " + position + " < " + length);
      }
      position += bytesRead;
    }
    return data;
  }

  /**
   * Closes a {@link DataSource}, suppressing any {@link IOException} that may occur.
   *
   * @param dataSource The {@link DataSource} to close.
   */
  public static void closeQuietly(@Nullable DataSource dataSource) {
    try {
      if (dataSource != null) {
        dataSource.close();
      }
    } catch (IOException e) {
      // Ignore.
    }
  }
}
