/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.source;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import android.net.Uri;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.Timeline;
import androidx.media3.exoplayer.analytics.PlayerId;
import androidx.media3.test.utils.TestUtil;
import androidx.media3.test.utils.robolectric.RobolectricUtil;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit tests for {@link SilenceMediaSource}. */
@RunWith(AndroidJUnit4.class)
public class SilenceMediaSourceTest {

  @Test
  public void builder_setsMediaItem() {
    SilenceMediaSource mediaSource =
        new SilenceMediaSource.Factory().setDurationUs(1_000_000).createMediaSource();

    MediaItem mediaItem = mediaSource.getMediaItem();

    assertThat(mediaItem).isNotNull();
    assertThat(mediaItem.mediaId).isEqualTo(SilenceMediaSource.MEDIA_ID);
    assertThat(mediaItem.localConfiguration.uri).isEqualTo(Uri.EMPTY);
    assertThat(mediaItem.localConfiguration.mimeType).isEqualTo(MimeTypes.AUDIO_RAW);
  }

  @Test
  public void builderSetTag_setsTagOfMediaItem() {
    Object tag = new Object();

    SilenceMediaSource mediaSource =
        new SilenceMediaSource.Factory().setTag(tag).setDurationUs(1_000_000).createMediaSource();

    assertThat(mediaSource.getMediaItem().localConfiguration.tag).isEqualTo(tag);
  }

  @Test
  public void builderSetTag_setsTagOfMediaSource() {
    Object tag = new Object();

    SilenceMediaSource mediaSource =
        new SilenceMediaSource.Factory().setTag(tag).setDurationUs(1_000_000).createMediaSource();

    assertThat(mediaSource.getMediaItem().localConfiguration.tag).isEqualTo(tag);
  }

  @Test
  public void builder_setDurationUsNotCalled_throwsIllegalStateException() {
    assertThrows(IllegalStateException.class, new SilenceMediaSource.Factory()::createMediaSource);
  }

  @Test
  public void builderSetDurationUs_nonPositiveValue_throwsIllegalStateException() {
    @SuppressWarnings("Range") // Deliberately testing an invalid value
    SilenceMediaSource.Factory factory = new SilenceMediaSource.Factory().setDurationUs(-1);

    assertThrows(IllegalStateException.class, factory::createMediaSource);
  }

  @Test
  public void newInstance_setsMediaItem() {
    SilenceMediaSource mediaSource = new SilenceMediaSource(1_000_000);

    MediaItem mediaItem = mediaSource.getMediaItem();

    assertThat(mediaItem).isNotNull();
    assertThat(mediaItem.mediaId).isEqualTo(SilenceMediaSource.MEDIA_ID);
    assertThat(mediaSource.getMediaItem().localConfiguration.uri).isEqualTo(Uri.EMPTY);
    assertThat(mediaItem.localConfiguration.mimeType).isEqualTo(MimeTypes.AUDIO_RAW);
  }

  @Test
  public void canUpdateMediaItem_withFieldsChanged_returnsTrue() {
    MediaItem updatedMediaItem = TestUtil.buildFullyCustomizedMediaItem();
    MediaSource mediaSource = buildMediaSource();

    boolean canUpdateMediaItem = mediaSource.canUpdateMediaItem(updatedMediaItem);

    assertThat(canUpdateMediaItem).isTrue();
  }

  @Test
  public void updateMediaItem_createsTimelineWithUpdatedItem() throws Exception {
    MediaItem updatedMediaItem = new MediaItem.Builder().setUri("http://test.test").build();
    MediaSource mediaSource = buildMediaSource();
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

  private static MediaSource buildMediaSource() {
    return new SilenceMediaSource.Factory().setDurationUs(1234).setTag("tag").createMediaSource();
  }
}
