/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.source;

import static androidx.media3.common.util.Util.msToUs;
import static com.google.common.truth.Truth.assertThat;

import androidx.media3.common.C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.Timeline;
import androidx.media3.exoplayer.analytics.PlayerId;
import androidx.media3.test.utils.TestUtil;
import androidx.media3.test.utils.robolectric.RobolectricUtil;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.common.util.concurrent.SettableFuture;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit test for {@link ProgressiveMediaSource}. */
@RunWith(AndroidJUnit4.class)
public class ExternallyLoadedMediaSourceTest {

  @Test
  public void canUpdateMediaItem_withIrrelevantFieldsChanged_returnsTrue() {
    MediaItem initialMediaItem =
        new MediaItem.Builder()
            .setUri("http://test.test")
            .setImageDurationMs(5 * C.MILLIS_PER_SECOND)
            .setMimeType(MimeTypes.APPLICATION_EXTERNALLY_LOADED_IMAGE)
            .build();
    MediaItem updatedMediaItem =
        TestUtil.buildFullyCustomizedMediaItem()
            .buildUpon()
            .setUri("http://test.test")
            .setImageDurationMs(5 * C.MILLIS_PER_SECOND)
            .setMimeType(MimeTypes.APPLICATION_EXTERNALLY_LOADED_IMAGE)
            .build();
    MediaSource mediaSource = buildMediaSource(initialMediaItem);

    boolean canUpdateMediaItem = mediaSource.canUpdateMediaItem(updatedMediaItem);

    assertThat(canUpdateMediaItem).isTrue();
  }

  @Test
  public void canUpdateMediaItem_withNullLocalConfiguration_returnsFalse() {
    MediaItem initialMediaItem =
        new MediaItem.Builder()
            .setImageDurationMs(5 * C.MILLIS_PER_SECOND)
            .setUri("http://test.test")
            .build();
    MediaItem updatedMediaItem =
        new MediaItem.Builder()
            .setImageDurationMs(5 * C.MILLIS_PER_SECOND)
            .setMediaId("id")
            .build();
    MediaSource mediaSource = buildMediaSource(initialMediaItem);

    boolean canUpdateMediaItem = mediaSource.canUpdateMediaItem(updatedMediaItem);

    assertThat(canUpdateMediaItem).isFalse();
  }

  @Test
  public void canUpdateMediaItem_withChangedUri_returnsFalse() {
    MediaItem initialMediaItem =
        new MediaItem.Builder()
            .setImageDurationMs(5 * C.MILLIS_PER_SECOND)
            .setUri("http://test.test")
            .build();
    MediaItem updatedMediaItem =
        new MediaItem.Builder()
            .setImageDurationMs(5 * C.MILLIS_PER_SECOND)
            .setUri("http://test2.test")
            .build();
    MediaSource mediaSource = buildMediaSource(initialMediaItem);

    boolean canUpdateMediaItem = mediaSource.canUpdateMediaItem(updatedMediaItem);

    assertThat(canUpdateMediaItem).isFalse();
  }

  @Test
  public void updateMediaItem_createsTimelineWithUpdatedItem() throws Exception {
    MediaItem initialMediaItem =
        new MediaItem.Builder()
            .setUri("http://test.test")
            .setImageDurationMs(5 * C.MILLIS_PER_SECOND)
            .setTag("tag1")
            .build();
    MediaItem updatedMediaItem =
        new MediaItem.Builder()
            .setUri("http://test.test")
            .setImageDurationMs(5 * C.MILLIS_PER_SECOND)
            .setTag("tag2")
            .build();
    MediaSource mediaSource = buildMediaSource(initialMediaItem);
    AtomicReference<Timeline> timelineReference = new AtomicReference<>();

    mediaSource.updateMediaItem(updatedMediaItem);
    mediaSource.prepareSource(
        (source, timeline) -> timelineReference.set(timeline),
        /* mediaTransferListener= */ null,
        PlayerId.UNSET);
    RobolectricUtil.runMainLooperUntil(() -> timelineReference.get() != null);

    assertThat(
            timelineReference
                .get()
                .getWindow(/* windowIndex= */ 0, new Timeline.Window())
                .mediaItem)
        .isEqualTo(updatedMediaItem);
  }

  private static MediaSource buildMediaSource(MediaItem mediaItem) {
    return new ExternallyLoadedMediaSource.Factory(
            msToUs(mediaItem.localConfiguration.imageDurationMs), unused -> SettableFuture.create())
        .createMediaSource(mediaItem);
  }
}
