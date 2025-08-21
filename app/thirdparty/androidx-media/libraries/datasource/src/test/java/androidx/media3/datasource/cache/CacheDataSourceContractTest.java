/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.datasource.cache;

import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.DataSource;
import androidx.media3.test.utils.DataSourceContractTest;
import androidx.media3.test.utils.FakeDataSet;
import androidx.media3.test.utils.FakeDataSource;
import androidx.media3.test.utils.TestUtil;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.common.collect.ImmutableList;
import java.io.File;
import java.io.IOException;
import org.junit.Before;
import org.junit.runner.RunWith;

/** {@link DataSource} contract tests for {@link CacheDataSource}. */
@RunWith(AndroidJUnit4.class)
public class CacheDataSourceContractTest extends DataSourceContractTest {

  private Uri simpleUri;
  private Uri unknownLengthUri;
  private byte[] simpleData;
  private byte[] unknownLengthData;
  private FakeDataSet fakeDataSet;

  private FakeDataSource upstreamDataSource;

  @Before
  public void setUp() throws IOException {
    simpleUri = Uri.parse("test://simple.test");
    unknownLengthUri = Uri.parse("test://unknown-length.test");
    simpleData = TestUtil.buildTestData(/* length= */ 20);
    unknownLengthData = TestUtil.buildTestData(/* length= */ 40);
    fakeDataSet =
        new FakeDataSet()
            .newData(simpleUri)
            .appendReadData(simpleData)
            .endData()
            .newData(unknownLengthUri)
            .setSimulateUnknownLength(true)
            .appendReadData(unknownLengthData)
            .endData();
  }

  @Override
  protected ImmutableList<TestResource> getTestResources() {
    return ImmutableList.of(
        new TestResource.Builder()
            .setName("simple")
            .setUri(simpleUri)
            .setExpectedBytes(simpleData)
            .build(),
        new TestResource.Builder()
            .setName("unknown length")
            .setUri(unknownLengthUri)
            .setExpectedBytes(unknownLengthData)
            .build());
  }

  @Override
  protected Uri getNotFoundUri() {
    return Uri.parse("test://not-found.test");
  }

  @Override
  protected DataSource createDataSource() throws IOException {
    File tempFolder =
        Util.createTempDirectory(ApplicationProvider.getApplicationContext(), "ExoPlayerTest");
    SimpleCache cache =
        new SimpleCache(tempFolder, new NoOpCacheEvictor(), TestUtil.getInMemoryDatabaseProvider());
    upstreamDataSource = new FakeDataSource(fakeDataSet);
    return new CacheDataSource(cache, upstreamDataSource);
  }

  @Override
  @Nullable
  protected DataSource getTransferListenerDataSource() {
    return upstreamDataSource;
  }
}
