/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.hls;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;

import android.net.Uri;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Tests for {@link FullSegmentEncryptionKeyCache}. */
@RunWith(AndroidJUnit4.class)
public class FullSegmentEncryptionKeyCacheTest {

  private final Uri firstUri = Uri.parse("https://www.google.com");
  private final Uri secondUri = Uri.parse("https://www.abc.xyz");
  private final byte[] encryptionKey = {5, 6, 7, 8};

  @Test
  public void putThenGetAndContains() {
    FullSegmentEncryptionKeyCache cache = new FullSegmentEncryptionKeyCache(/* maxSize= */ 5);
    cache.put(firstUri, encryptionKey);
    assertThat(cache.get(firstUri)).isEqualTo(encryptionKey);
    assertThat(cache.get(secondUri)).isNull();
    assertThat(cache.containsUri(firstUri)).isTrue();
    assertThat(cache.containsUri(secondUri)).isFalse();
  }

  @Test
  public void getNullReturnsNull() {
    FullSegmentEncryptionKeyCache cache = new FullSegmentEncryptionKeyCache(/* maxSize= */ 5);
    cache.put(firstUri, encryptionKey);
    assertThat(cache.get(null)).isNull();
  }

  @Test
  public void putNullKeyThrowsException() {
    FullSegmentEncryptionKeyCache cache = new FullSegmentEncryptionKeyCache(/* maxSize= */ 5);
    try {
      cache.put(null, encryptionKey);
      fail();
    } catch (NullPointerException expected) {
    }
  }

  @Test
  public void putNullValueThrowsException() {
    FullSegmentEncryptionKeyCache cache = new FullSegmentEncryptionKeyCache(/* maxSize= */ 5);
    try {
      cache.put(firstUri, null);
      fail();
    } catch (NullPointerException expected) {
    }
  }

  @Test
  public void containsNullThrowsException() {
    FullSegmentEncryptionKeyCache cache = new FullSegmentEncryptionKeyCache(/* maxSize= */ 5);
    try {
      cache.containsUri(null);
      fail();
    } catch (NullPointerException expected) {
    }
  }

  @Test
  public void removeNullThrowsException() {
    FullSegmentEncryptionKeyCache cache = new FullSegmentEncryptionKeyCache(/* maxSize= */ 5);
    try {
      cache.remove(null);
      fail();
    } catch (NullPointerException expected) {
    }
  }

  @Test
  public void oldestElementRemoved() {
    FullSegmentEncryptionKeyCache cache = new FullSegmentEncryptionKeyCache(/* maxSize= */ 2);

    cache.put(firstUri, encryptionKey);
    cache.put(secondUri, new byte[] {1, 2, 3, 4});
    cache.put(Uri.parse("www.nest.com"), new byte[] {1, 2, 3, 4});

    assertThat(cache.containsUri(firstUri)).isFalse();
    assertThat(cache.containsUri(secondUri)).isTrue();
  }

  /**
   * Elements need to be removed and reinserted, rather than just updated, to change their position
   * in the removal queue.
   */
  @Test
  public void updatingElementDoesntChangeAgeForRemoval() {
    FullSegmentEncryptionKeyCache cache = new FullSegmentEncryptionKeyCache(/* maxSize= */ 2);

    cache.put(firstUri, encryptionKey);
    cache.put(secondUri, new byte[] {1, 2, 3, 4});
    // Update firstUri element
    cache.put(firstUri, new byte[] {10, 11, 12, 12});
    cache.put(Uri.parse("www.nest.com"), new byte[] {1, 2, 3, 4});

    // firstUri is still removed before secondUri, despite the update
    assertThat(cache.containsUri(firstUri)).isFalse();
    assertThat(cache.containsUri(secondUri)).isTrue();
  }
}
