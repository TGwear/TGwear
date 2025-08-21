/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.datasource.okhttp;

import androidx.media3.datasource.DataSource;
import androidx.media3.test.utils.DataSourceContractTest;
import androidx.media3.test.utils.HttpDataSourceTestEnv;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.common.collect.ImmutableList;
import java.util.List;
import okhttp3.OkHttpClient;
import org.junit.Rule;
import org.junit.runner.RunWith;

/** {@link DataSource} contract tests for {@link OkHttpDataSource}. */
@RunWith(AndroidJUnit4.class)
public class OkHttpDataSourceContractTest extends DataSourceContractTest {

  @Rule public HttpDataSourceTestEnv httpDataSourceTestEnv = new HttpDataSourceTestEnv();

  @Override
  protected DataSource createDataSource() {
    return new OkHttpDataSource.Factory(new OkHttpClient()).createDataSource();
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
