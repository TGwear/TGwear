/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.datasource.cache;

import static androidx.media3.datasource.cache.CacheKeyFactory.DEFAULT;
import static com.google.common.truth.Truth.assertThat;

import android.net.Uri;
import androidx.media3.datasource.DataSpec;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Tests {@link CacheKeyFactoryTest}. */
@RunWith(AndroidJUnit4.class)
public class CacheKeyFactoryTest {

  @Test
  public void default_dataSpecWithKey_returnsKey() {
    Uri testUri = Uri.parse("test");
    String key = "key";
    DataSpec dataSpec = new DataSpec.Builder().setUri(testUri).setKey(key).build();
    assertThat(DEFAULT.buildCacheKey(dataSpec)).isEqualTo(key);
  }

  @Test
  public void default_dataSpecWithoutKey_returnsUri() {
    Uri testUri = Uri.parse("test");
    DataSpec dataSpec = new DataSpec.Builder().setUri(testUri).build();
    assertThat(DEFAULT.buildCacheKey(dataSpec)).isEqualTo(testUri.toString());
  }
}
