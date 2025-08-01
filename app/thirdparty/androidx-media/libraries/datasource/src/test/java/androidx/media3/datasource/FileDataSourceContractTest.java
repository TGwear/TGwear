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
import com.google.common.io.Files;
import java.io.File;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;

/** {@link DataSource} contract tests for {@link FileDataSource}. */
@RunWith(AndroidJUnit4.class)
public class FileDataSourceContractTest extends DataSourceContractTest {

  private static final byte[] DATA = TestUtil.buildTestData(20);

  @Rule public final TemporaryFolder tempFolder = new TemporaryFolder();

  private Uri uri;

  @Before
  public void writeFile() throws Exception {
    File file = tempFolder.newFile();
    Files.write(DATA, file);
    uri = Uri.fromFile(file);
  }

  @Override
  protected ImmutableList<TestResource> getTestResources() {
    return ImmutableList.of(
        new TestResource.Builder().setName("simple").setUri(uri).setExpectedBytes(DATA).build());
  }

  @Override
  protected Uri getNotFoundUri() {
    return Uri.fromFile(tempFolder.getRoot().toPath().resolve("nonexistent").toFile());
  }

  @Override
  protected DataSource createDataSource() {
    return new FileDataSource();
  }
}
