/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.datasource;

import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import androidx.media3.test.utils.DataSourceContractTest;
import androidx.media3.test.utils.TestUtil;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.common.collect.ImmutableList;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * {@link DataSource} contract tests for {@link FileDescriptorDataSource} using {@link
 * AssetFileDescriptor}.
 */
@RunWith(AndroidJUnit4.class)
public class FileDescriptorDataSourceUsingAssetFileDescriptorContractTest
    extends DataSourceContractTest {

  private static final String ASSET_PATH = "media/mp3/1024_incrementing_bytes.mp3";

  @Override
  protected DataSource createDataSource() throws Exception {
    AssetFileDescriptor afd =
        ApplicationProvider.getApplicationContext().getAssets().openFd(ASSET_PATH);
    return new FileDescriptorDataSource(
        afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
  }

  @Override
  protected ImmutableList<TestResource> getTestResources() throws Exception {
    return ImmutableList.of(
        new TestResource.Builder()
            .setName("simple")
            .setUri(Uri.EMPTY)
            .setExpectedBytes(
                TestUtil.getByteArray(ApplicationProvider.getApplicationContext(), ASSET_PATH))
            .build());
  }

  @Override
  protected Uri getNotFoundUri() {
    throw new UnsupportedOperationException();
  }

  @Override
  @Test
  @Ignore
  public void resourceNotFound() {}

  @Override
  @Test
  @Ignore
  public void resourceNotFound_transferListenerCallbacks() {}

  @Override
  @Test
  @Ignore
  public void getUri_resourceNotFound_returnsNullIfNotOpened() {}

  @Override
  @Test
  @Ignore
  public void getResponseHeaders_resourceNotFound_isEmptyWhileNotOpen() {}
}
