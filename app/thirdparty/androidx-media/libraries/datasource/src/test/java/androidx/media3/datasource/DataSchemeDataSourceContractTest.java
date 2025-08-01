/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.datasource;

import static java.nio.charset.StandardCharsets.UTF_8;

import android.net.Uri;
import android.util.Base64;
import androidx.media3.test.utils.DataSourceContractTest;
import androidx.media3.test.utils.TestUtil;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.common.collect.ImmutableList;
import java.util.Random;
import org.junit.runner.RunWith;

/** {@link DataSource} contract tests for {@link ByteArrayDataSource}. */
@RunWith(AndroidJUnit4.class)
public class DataSchemeDataSourceContractTest extends DataSourceContractTest {

  private static final String DATA = TestUtil.buildTestString(20, new Random(0));
  private static final String BASE64_ENCODED_DATA =
      Base64.encodeToString(TestUtil.buildTestData(20), Base64.DEFAULT);

  @Override
  protected ImmutableList<TestResource> getTestResources() {
    return ImmutableList.of(
        new TestResource.Builder()
            .setName("plain text")
            .setUri("data:text/plain," + DATA)
            .setExpectedBytes(DATA.getBytes(UTF_8))
            .build(),
        new TestResource.Builder()
            .setName("base64 encoded text")
            .setUri("data:text/plain;base64," + BASE64_ENCODED_DATA)
            .setExpectedBytes(Base64.decode(BASE64_ENCODED_DATA, Base64.DEFAULT))
            .build());
  }

  @Override
  protected Uri getNotFoundUri() {
    return Uri.parse("data:");
  }

  @Override
  protected DataSource createDataSource() {
    return new DataSchemeDataSource();
  }
}
