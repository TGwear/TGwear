/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.test.utils;

import static com.google.common.truth.Truth.assertThat;

import androidx.annotation.Nullable;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Timeline.Window;
import androidx.media3.exoplayer.analytics.PlayerId;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit test for {@link FakeMediaSourceFactory}. */
@RunWith(AndroidJUnit4.class)
public class FakeMediaSourceFactoryTest {

  @Test
  public void createMediaSource_mediaItemIsSameInstance() {
    FakeMediaSourceFactory fakeMediaSourceFactory = new FakeMediaSourceFactory();
    MediaItem mediaItem = MediaItem.fromUri("http://google.com/0");
    @Nullable AtomicReference<MediaItem> reportedMediaItem = new AtomicReference<>();

    MediaSource mediaSource = fakeMediaSourceFactory.createMediaSource(mediaItem);
    mediaSource.prepareSource(
        (source, timeline) -> {
          int firstWindowIndex = timeline.getFirstWindowIndex(/* shuffleModeEnabled= */ false);
          reportedMediaItem.set(timeline.getWindow(firstWindowIndex, new Window()).mediaItem);
        },
        /* mediaTransferListener= */ null,
        PlayerId.UNSET);

    assertThat(reportedMediaItem.get()).isSameInstanceAs(mediaItem);
    assertThat(mediaSource.getMediaItem()).isSameInstanceAs(mediaItem);
  }
}
