/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.datasource;

import static org.junit.Assume.assumeTrue;

import android.net.http.HttpEngine;
import android.os.Build;
import android.os.ext.SdkExtensions;
import androidx.annotation.RequiresExtension;
import androidx.media3.test.utils.DataSourceContractTest;
import androidx.media3.test.utils.HttpDataSourceTestEnv;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SdkSuppress;
import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

/** {@link DataSource} contract tests for {@link HttpEngineDataSource}. */
// @SdkSuppress doesn't support extensions but lint doesn't understand that, so have to use
// @RequiresExtension: https://issuetracker.google.com/382043552
@SuppressWarnings("UseSdkSuppress")
@SdkSuppress(minSdkVersion = Build.VERSION_CODES.S)
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@RunWith(AndroidJUnit4.class)
public class HttpEngineDataSourceContractTest extends DataSourceContractTest {

  @Rule public HttpDataSourceTestEnv httpDataSourceTestEnv = new HttpDataSourceTestEnv();
  private final ExecutorService executorService = Executors.newSingleThreadExecutor();

  @Before
  public void before() {
    assumeTrue(SdkExtensions.getExtensionVersion(Build.VERSION_CODES.S) >= 7);
  }

  @After
  public void tearDown() {
    executorService.shutdown();
  }

  @Override
  protected DataSource createDataSource() {
    HttpEngine httpEngine =
        new HttpEngine.Builder(ApplicationProvider.getApplicationContext()).build();
    return new HttpEngineDataSource.Factory(httpEngine, executorService).createDataSource();
  }

  @Override
  protected ImmutableList<TestResource> getTestResources() {
    return httpDataSourceTestEnv.getServedResources();
  }

  @Override
  protected List<TestResource> getNotFoundResources() {
    return httpDataSourceTestEnv.getNotFoundResources();
  }
}
