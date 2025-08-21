/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.datasource;

import static com.google.common.truth.Truth.assertThat;
import static java.lang.Math.min;

import androidx.media3.common.util.Util;
import androidx.media3.test.utils.TestUtil;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import java.util.Random;
import javax.crypto.Cipher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit tests for {@link AesFlushingCipher}. */
@RunWith(AndroidJUnit4.class)
public class AesFlushingCipherTest {

  private static final int DATA_LENGTH = 65536;
  private static final byte[] KEY = Util.getUtf8Bytes("testKey:12345678");
  private static final long NONCE = 0;
  private static final long START_OFFSET = 11;
  private static final long RANDOM_SEED = 0x12345678;

  private AesFlushingCipher encryptCipher;
  private AesFlushingCipher decryptCipher;

  @Before
  public void setUp() {
    encryptCipher = new AesFlushingCipher(Cipher.ENCRYPT_MODE, KEY, NONCE, START_OFFSET);
    decryptCipher = new AesFlushingCipher(Cipher.DECRYPT_MODE, KEY, NONCE, START_OFFSET);
  }

  @After
  public void tearDown() {
    encryptCipher = null;
    decryptCipher = null;
  }

  private static long getMaxUnchangedBytesAllowedPostEncryption(long length) {
    // Assuming that not more than 10% of the resultant bytes should be identical.
    // The value of 10% is arbitrary, ciphers standards do not name a value.
    return length / 10;
  }

  // Count the number of bytes that do not match.
  private static int getDifferingByteCount(byte[] data1, byte[] data2, int startOffset) {
    int count = 0;
    for (int i = startOffset; i < data1.length; i++) {
      if (data1[i] != data2[i]) {
        count++;
      }
    }
    return count;
  }

  // Count the number of bytes that do not match.
  private static int getDifferingByteCount(byte[] data1, byte[] data2) {
    return getDifferingByteCount(data1, data2, 0);
  }

  // Test a single encrypt and decrypt call.
  @Test
  public void single() {
    byte[] reference = TestUtil.buildTestData(DATA_LENGTH);
    byte[] data = reference.clone();

    encryptCipher.updateInPlace(data, 0, data.length);
    int unchangedByteCount = data.length - getDifferingByteCount(reference, data);
    assertThat(unchangedByteCount <= getMaxUnchangedBytesAllowedPostEncryption(data.length))
        .isTrue();

    decryptCipher.updateInPlace(data, 0, data.length);
    int differingByteCount = getDifferingByteCount(reference, data);
    assertThat(differingByteCount).isEqualTo(0);
  }

  // Test several encrypt and decrypt calls, each aligned on a 16 byte block size.
  @Test
  public void aligned() {
    byte[] reference = TestUtil.buildTestData(DATA_LENGTH);
    byte[] data = reference.clone();
    Random random = new Random(RANDOM_SEED);

    int offset = 0;
    while (offset < data.length) {
      int bytes = (1 + random.nextInt(50)) * 16;
      bytes = min(bytes, data.length - offset);
      assertThat(bytes % 16).isEqualTo(0);
      encryptCipher.updateInPlace(data, offset, bytes);
      offset += bytes;
    }

    int unchangedByteCount = data.length - getDifferingByteCount(reference, data);
    assertThat(unchangedByteCount <= getMaxUnchangedBytesAllowedPostEncryption(data.length))
        .isTrue();

    offset = 0;
    while (offset < data.length) {
      int bytes = (1 + random.nextInt(50)) * 16;
      bytes = min(bytes, data.length - offset);
      assertThat(bytes % 16).isEqualTo(0);
      decryptCipher.updateInPlace(data, offset, bytes);
      offset += bytes;
    }

    int differingByteCount = getDifferingByteCount(reference, data);
    assertThat(differingByteCount).isEqualTo(0);
  }

  // Test several encrypt and decrypt calls, not aligned on block boundary.
  @Test
  public void unAligned() {
    byte[] reference = TestUtil.buildTestData(DATA_LENGTH);
    byte[] data = reference.clone();
    Random random = new Random(RANDOM_SEED);

    // Encrypt
    int offset = 0;
    while (offset < data.length) {
      int bytes = 1 + random.nextInt(4095);
      bytes = min(bytes, data.length - offset);
      encryptCipher.updateInPlace(data, offset, bytes);
      offset += bytes;
    }

    int unchangedByteCount = data.length - getDifferingByteCount(reference, data);
    assertThat(unchangedByteCount <= getMaxUnchangedBytesAllowedPostEncryption(data.length))
        .isTrue();

    offset = 0;
    while (offset < data.length) {
      int bytes = 1 + random.nextInt(4095);
      bytes = min(bytes, data.length - offset);
      decryptCipher.updateInPlace(data, offset, bytes);
      offset += bytes;
    }

    int differingByteCount = getDifferingByteCount(reference, data);
    assertThat(differingByteCount).isEqualTo(0);
  }

  // Test decryption starting from the middle of an encrypted block.
  @Test
  public void midJoin() {
    byte[] reference = TestUtil.buildTestData(DATA_LENGTH);
    byte[] data = reference.clone();
    Random random = new Random(RANDOM_SEED);

    // Encrypt
    int offset = 0;
    while (offset < data.length) {
      int bytes = 1 + random.nextInt(4095);
      bytes = min(bytes, data.length - offset);
      encryptCipher.updateInPlace(data, offset, bytes);
      offset += bytes;
    }

    // Verify
    int unchangedByteCount = data.length - getDifferingByteCount(reference, data);
    assertThat(unchangedByteCount <= getMaxUnchangedBytesAllowedPostEncryption(data.length))
        .isTrue();

    // Setup decryption from random location
    offset = random.nextInt(4096);
    decryptCipher = new AesFlushingCipher(Cipher.DECRYPT_MODE, KEY, NONCE, offset + START_OFFSET);
    int remainingLength = data.length - offset;
    int originalOffset = offset;

    // Decrypt
    while (remainingLength > 0) {
      int bytes = 1 + random.nextInt(4095);
      bytes = min(bytes, remainingLength);
      decryptCipher.updateInPlace(data, offset, bytes);
      offset += bytes;
      remainingLength -= bytes;
    }

    // Verify
    int differingByteCount = getDifferingByteCount(reference, data, originalOffset);
    assertThat(differingByteCount).isEqualTo(0);
  }
}
