/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.datasource;

import android.net.Uri;
import androidx.media3.test.utils.TestUtil;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit tests for {@link AssetDataSource}. */
@RunWith(AndroidJUnit4.class)
public final class AssetDataSourceTest {

  private static final String DATA_PATH = "media/mp3/1024_incrementing_bytes.mp3";

  @Test
  public void readFileUri() throws Exception {
    AssetDataSource dataSource = new AssetDataSource(ApplicationProvider.getApplicationContext());
    DataSpec dataSpec = new DataSpec(Uri.parse("file:///android_asset/" + DATA_PATH));
    TestUtil.assertDataSourceContent(
        dataSource,
        dataSpec,
        TestUtil.getByteArray(ApplicationProvider.getApplicationContext(), DATA_PATH),
        true);
  }

  @Test
  public void readAssetUri() throws Exception {
    AssetDataSource dataSource = new AssetDataSource(ApplicationProvider.getApplicationContext());
    DataSpec dataSpec = new DataSpec(Uri.parse("asset:///" + DATA_PATH));
    TestUtil.assertDataSourceContent(
        dataSource,
        dataSpec,
        TestUtil.getByteArray(ApplicationProvider.getApplicationContext(), DATA_PATH),
        true);
  }
}
