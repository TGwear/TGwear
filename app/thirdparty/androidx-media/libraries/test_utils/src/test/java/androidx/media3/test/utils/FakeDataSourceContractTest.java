/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.test.utils;

import android.net.Uri;
import androidx.media3.datasource.DataSource;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.runner.RunWith;

/** {@link DataSource} contract tests for {@link FakeDataSource}. */
@RunWith(AndroidJUnit4.class)
public class FakeDataSourceContractTest extends DataSourceContractTest {

  private Uri simpleUri;
  private Uri unknownLengthUri;

  private byte[] simpleData;
  private byte[] unknownLengthData;
  private FakeDataSet fakeDataSet;

  @Before
  public void setUp() {
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
  protected DataSource createDataSource() {
    return new FakeDataSource(fakeDataSet);
  }
}
