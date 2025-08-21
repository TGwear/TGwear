/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.hls;

import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.media3.common.C;
import androidx.media3.common.util.Assertions;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DataSourceInputStream;
import androidx.media3.datasource.DataSpec;
import androidx.media3.datasource.TransferListener;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.List;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * A {@link DataSource} that decrypts data read from an upstream source, encrypted with AES-128 with
 * a 128-bit key and PKCS7 padding.
 *
 * <p>Note that this {@link DataSource} does not support being opened from arbitrary offsets. It is
 * designed specifically for reading whole files as defined in an HLS media playlist. For this
 * reason the implementation is private to the HLS package.
 */
/* package */ class Aes128DataSource implements DataSource {

  private final DataSource upstream;
  private final byte[] encryptionKey;
  private final byte[] encryptionIv;

  @Nullable private CipherInputStream cipherInputStream;

  /**
   * @param upstream The upstream {@link DataSource}.
   * @param encryptionKey The encryption key.
   * @param encryptionIv The encryption initialization vector.
   */
  public Aes128DataSource(DataSource upstream, byte[] encryptionKey, byte[] encryptionIv) {
    this.upstream = upstream;
    this.encryptionKey = encryptionKey;
    this.encryptionIv = encryptionIv;
  }

  @Override
  public final void addTransferListener(TransferListener transferListener) {
    Assertions.checkNotNull(transferListener);
    upstream.addTransferListener(transferListener);
  }

  @Override
  public final long open(DataSpec dataSpec) throws IOException {
    Cipher cipher;
    try {
      cipher = getCipherInstance();
    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
      throw new RuntimeException(e);
    }

    Key cipherKey = new SecretKeySpec(encryptionKey, "AES");
    AlgorithmParameterSpec cipherIV = new IvParameterSpec(encryptionIv);

    try {
      cipher.init(Cipher.DECRYPT_MODE, cipherKey, cipherIV);
    } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
      throw new RuntimeException(e);
    }

    DataSourceInputStream inputStream = new DataSourceInputStream(upstream, dataSpec);
    cipherInputStream = new CipherInputStream(inputStream, cipher);
    inputStream.open();

    return C.LENGTH_UNSET;
  }

  @Override
  public final int read(byte[] buffer, int offset, int length) throws IOException {
    Assertions.checkNotNull(cipherInputStream);
    int bytesRead = cipherInputStream.read(buffer, offset, length);
    if (bytesRead < 0) {
      return C.RESULT_END_OF_INPUT;
    }
    return bytesRead;
  }

  @Override
  @Nullable
  public final Uri getUri() {
    return upstream.getUri();
  }

  @Override
  public final Map<String, List<String>> getResponseHeaders() {
    return upstream.getResponseHeaders();
  }

  @Override
  public void close() throws IOException {
    if (cipherInputStream != null) {
      cipherInputStream = null;
      upstream.close();
    }
  }

  protected Cipher getCipherInstance() throws NoSuchPaddingException, NoSuchAlgorithmException {
    return Cipher.getInstance("AES/CBC/PKCS7Padding");
  }
}
