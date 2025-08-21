/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.smoothstreaming.offline;

import androidx.media3.common.MediaItem;
import androidx.media3.common.MimeTypes;
import androidx.media3.exoplayer.Renderer;
import androidx.media3.exoplayer.offline.DownloadHelper;
import androidx.media3.test.utils.FakeDataSource;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit test to verify creation of a SmoothStreaming {@link DownloadHelper}. */
@RunWith(AndroidJUnit4.class)
public final class DownloadHelperTest {

  @Test
  public void staticDownloadHelperForSmoothStreaming_doesNotThrow() {
    DownloadHelper.forMediaItem(
        ApplicationProvider.getApplicationContext(),
        new MediaItem.Builder().setUri("http://uri").setMimeType(MimeTypes.APPLICATION_SS).build(),
        (handler, videoListener, audioListener, text, metadata) -> new Renderer[0],
        new FakeDataSource.Factory());
    DownloadHelper.forMediaItem(
        new MediaItem.Builder().setUri("http://uri").setMimeType(MimeTypes.APPLICATION_SS).build(),
        DownloadHelper.DEFAULT_TRACK_SELECTOR_PARAMETERS,
        (handler, videoListener, audioListener, text, metadata) -> new Renderer[0],
        new FakeDataSource.Factory(),
        /* drmSessionManager= */ null);
  }
}
