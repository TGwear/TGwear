/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.datasource;

import android.net.Uri;
import androidx.media3.test.utils.AssetContentProvider;
import androidx.media3.test.utils.DataSourceContractTest;
import androidx.media3.test.utils.TestUtil;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.common.collect.ImmutableList;
import org.junit.runner.RunWith;

/** {@link DataSource} contract tests for {@link ContentDataSource}. */
@RunWith(AndroidJUnit4.class)
public final class ContentDataSourceContractTest extends DataSourceContractTest {

  private static final String AUTHORITY = "androidx.media3.datasource.test.AssetContentProvider";
  private static final String DATA_PATH = "media/mp3/1024_incrementing_bytes.mp3";

  @Override
  protected DataSource createDataSource() {
    return new ContentDataSource(ApplicationProvider.getApplicationContext());
  }

  @Override
  protected ImmutableList<TestResource> getTestResources() throws Exception {
    byte[] completeData =
        TestUtil.getByteArray(ApplicationProvider.getApplicationContext(), DATA_PATH);
    return ImmutableList.of(
        new TestResource.Builder()
            .setName("simple (pipe=false)")
            .setUri(AssetContentProvider.buildUri(AUTHORITY, DATA_PATH, /* pipeMode= */ false))
            .setExpectedBytes(completeData)
            .build(),
        new TestResource.Builder()
            .setName("simple (pipe=true)")
            .setUri(AssetContentProvider.buildUri(AUTHORITY, DATA_PATH, /* pipeMode= */ true))
            .setExpectedBytes(completeData)
            .build());
  }

  @Override
  protected Uri getNotFoundUri() {
    return AssetContentProvider.buildUri(AUTHORITY, "not/a/real/path", /* pipeMode= */ false);
  }
}
