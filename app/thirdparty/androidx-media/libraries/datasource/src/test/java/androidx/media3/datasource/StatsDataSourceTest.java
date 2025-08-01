/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.datasource;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import android.net.Uri;
import androidx.media3.test.utils.FakeDataSet;
import androidx.media3.test.utils.FakeDataSource;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public final class StatsDataSourceTest {

  @Test
  public void getLastOpenedUri_openSucceeds_returnsRedirectedUriAfterClosure() throws Exception {
    Uri redirectedUri = Uri.parse("bar");
    FakeDataSet fakeDataSet = new FakeDataSet();
    fakeDataSet.setRandomData(redirectedUri, /* length= */ 10);
    StatsDataSource statsDataSource =
        new StatsDataSource(
            new ResolvingDataSource(
                new FakeDataSource(fakeDataSet),
                dataSpec -> dataSpec.buildUpon().setUri(redirectedUri).build()));

    statsDataSource.open(new DataSpec(Uri.parse("foo")));
    statsDataSource.close();

    assertThat(statsDataSource.getLastOpenedUri()).isEqualTo(redirectedUri);
  }

  @Test
  public void getLastOpenedUri_openFails_returnsRedirectedUriAfterClosure() throws Exception {
    Uri redirectedUri = Uri.parse("bar");
    StatsDataSource statsDataSource =
        new StatsDataSource(
            new ResolvingDataSource(
                new FakeDataSource(),
                dataSpec -> dataSpec.buildUpon().setUri(redirectedUri).build()));

    assertThrows(IOException.class, () -> statsDataSource.open(new DataSpec(Uri.parse("foo"))));
    statsDataSource.close();

    assertThat(statsDataSource.getLastOpenedUri()).isEqualTo(redirectedUri);
  }
}
