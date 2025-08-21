/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.datasource;

import android.net.Uri;
import androidx.media3.test.utils.DataSourceContractTest;
import androidx.media3.test.utils.TestUtil;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.common.collect.ImmutableList;
import java.io.IOException;
import org.junit.Before;
import org.junit.runner.RunWith;

/** {@link DataSource} contract tests for {@link AssetDataSource}. */
@RunWith(AndroidJUnit4.class)
public class AssetDataSourceContractTest extends DataSourceContractTest {

  // We pick an arbitrary file from the assets. The selected file has a convenient size of 1024
  // bytes.
  private static final String ASSET_PATH = "media/mp3/1024_incrementing_bytes.mp3";
  private static final Uri ASSET_URI = Uri.parse("asset:///" + ASSET_PATH);

  private byte[] data;

  @Before
  public void setUp() throws IOException {
    data = TestUtil.getByteArray(ApplicationProvider.getApplicationContext(), ASSET_PATH);
  }

  @Override
  protected ImmutableList<TestResource> getTestResources() {
    return ImmutableList.of(
        new TestResource.Builder()
            .setName("simple")
            .setUri(ASSET_URI)
            .setExpectedBytes(data)
            .build());
  }

  @Override
  protected Uri getNotFoundUri() {
    return Uri.parse("asset:///nonexistentdir/nonexistentfile");
  }

  @Override
  protected DataSource createDataSource() {
    return new AssetDataSource(ApplicationProvider.getApplicationContext());
  }
}
