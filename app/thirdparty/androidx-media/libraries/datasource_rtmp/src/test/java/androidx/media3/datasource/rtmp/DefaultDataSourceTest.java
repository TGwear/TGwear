/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.datasource.rtmp;

import android.net.Uri;
import androidx.media3.datasource.DataSpec;
import androidx.media3.datasource.DefaultDataSource;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit test for {@link DefaultDataSource} with RTMP URIs. */
@RunWith(AndroidJUnit4.class)
public final class DefaultDataSourceTest {

  @Test
  public void openRtmpDataSpec_instantiatesRtmpDataSourceViaReflection() throws IOException {
    DefaultDataSource dataSource =
        new DefaultDataSource(
            ApplicationProvider.getApplicationContext(),
            "userAgent",
            /* allowCrossProtocolRedirects= */ false);
    DataSpec dataSpec = new DataSpec(Uri.parse("rtmp://test.com/stream"));
    try {
      dataSource.open(dataSpec);
    } catch (UnsatisfiedLinkError e) {
      // RtmpDataSource was successfully instantiated (test run using Gradle).
    } catch (UnsupportedOperationException e) {
      // RtmpDataSource was successfully instantiated (test run using Blaze).
    }
  }
}
