/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.datasource;

import static androidx.media3.common.util.Util.castNonNull;
import static java.lang.Math.min;

import androidx.annotation.Nullable;
import androidx.media3.common.util.UnstableApi;
import java.io.IOException;
import javax.crypto.Cipher;

/** A wrapping {@link DataSink} that encrypts the data being consumed. */
@UnstableApi
public final class AesCipherDataSink implements DataSink {

  private final DataSink wrappedDataSink;
  private final byte[] secretKey;
  @Nullable private final byte[] scratch;

  @Nullable private AesFlushingCipher cipher;

  /**
   * Create an instance whose {@code write} methods have the side effect of overwriting the input
   * {@code data}. Use this constructor for maximum efficiency in the case that there is no
   * requirement for the input data arrays to remain unchanged.
   *
   * @param secretKey The key data.
   * @param wrappedDataSink The wrapped {@link DataSink}.
   */
  public AesCipherDataSink(byte[] secretKey, DataSink wrappedDataSink) {
    this(secretKey, wrappedDataSink, null);
  }

  /**
   * Create an instance whose {@code write} methods are free of side effects. Use this constructor
   * when the input data arrays are required to remain unchanged.
   *
   * @param secretKey The key data.
   * @param wrappedDataSink The wrapped {@link DataSink}.
   * @param scratch Scratch space. Data is encrypted into this array before being written to the
   *     wrapped {@link DataSink}. It should be of appropriate size for the expected writes. If a
   *     write is larger than the size of this array the write will still succeed, but multiple
   *     cipher calls will be required to complete the operation. If {@code null} then encryption
   *     will overwrite the input {@code data}.
   */
  public AesCipherDataSink(byte[] secretKey, DataSink wrappedDataSink, @Nullable byte[] scratch) {
    this.wrappedDataSink = wrappedDataSink;
    this.secretKey = secretKey;
    this.scratch = scratch;
  }

  @Override
  public void open(DataSpec dataSpec) throws IOException {
    wrappedDataSink.open(dataSpec);
    cipher =
        new AesFlushingCipher(
            Cipher.ENCRYPT_MODE,
            secretKey,
            dataSpec.key,
            dataSpec.uriPositionOffset + dataSpec.position);
  }

  @Override
  public void write(byte[] buffer, int offset, int length) throws IOException {
    if (scratch == null) {
      // In-place mode. Writes over the input data.
      castNonNull(cipher).updateInPlace(buffer, offset, length);
      wrappedDataSink.write(buffer, offset, length);
    } else {
      // Use scratch space. The original data remains intact.
      int bytesProcessed = 0;
      while (bytesProcessed < length) {
        int bytesToProcess = min(length - bytesProcessed, scratch.length);
        castNonNull(cipher)
            .update(buffer, offset + bytesProcessed, bytesToProcess, scratch, /* outOffset= */ 0);
        wrappedDataSink.write(scratch, /* offset= */ 0, bytesToProcess);
        bytesProcessed += bytesToProcess;
      }
    }
  }

  @Override
  public void close() throws IOException {
    cipher = null;
    wrappedDataSink.close();
  }
}
