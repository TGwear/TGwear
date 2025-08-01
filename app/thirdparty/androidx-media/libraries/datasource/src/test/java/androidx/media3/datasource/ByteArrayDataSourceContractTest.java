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
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.common.collect.ImmutableList;
import java.io.IOException;
import org.junit.runner.RunWith;

/** {@link DataSource} contract tests for {@link ByteArrayDataSource}. */
@RunWith(AndroidJUnit4.class)
public class ByteArrayDataSourceContractTest extends DataSourceContractTest {

  private static final Uri URI_1 = Uri.parse("uri1");
  private static final byte[] DATA_1 = TestUtil.buildTestData(20);
  private static final Uri URI_2 = Uri.parse("uri2");
  private static final byte[] DATA_2 = TestUtil.buildTestData(10);

  @Override
  protected ImmutableList<TestResource> getTestResources() {
    return ImmutableList.of(
        new TestResource.Builder().setName("data-1").setUri(URI_1).setExpectedBytes(DATA_1).build(),
        new TestResource.Builder()
            .setName("data-2")
            .setUri(URI_2)
            .setExpectedBytes(DATA_2)
            .build());
  }

  @Override
  protected Uri getNotFoundUri() {
    return Uri.parse("not-found");
  }

  @Override
  protected DataSource createDataSource() {
    return new ByteArrayDataSource(
        uri -> {
          if (uri.equals(URI_1)) {
            return DATA_1;
          } else if (uri.equals(URI_2)) {
            return DATA_2;
          } else {
            throw new IOException("Unrecognized URI: " + uri);
          }
        });
  }
}
