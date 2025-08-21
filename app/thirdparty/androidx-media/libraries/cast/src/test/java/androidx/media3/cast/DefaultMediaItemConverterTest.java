/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.cast;

import static com.google.common.truth.Truth.assertThat;

import android.net.Uri;
import androidx.media3.common.C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.MimeTypes;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.android.gms.cast.MediaQueueItem;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Test for {@link DefaultMediaItemConverter}. */
@RunWith(AndroidJUnit4.class)
public class DefaultMediaItemConverterTest {

  @Test
  public void serialize_deserialize_minimal() {
    MediaItem.Builder builder = new MediaItem.Builder();
    MediaItem item =
        builder.setUri("http://example.com").setMimeType(MimeTypes.APPLICATION_MPD).build();

    DefaultMediaItemConverter converter = new DefaultMediaItemConverter();
    MediaQueueItem queueItem = converter.toMediaQueueItem(item);
    MediaItem reconstructedItem = converter.toMediaItem(queueItem);

    assertThat(reconstructedItem).isEqualTo(item);
  }

  @Test
  public void serialize_deserialize_complete() {
    MediaItem.Builder builder = new MediaItem.Builder();
    MediaItem item =
        builder
            .setMediaId("fooBar")
            .setUri(Uri.parse("http://example.com"))
            .setMediaMetadata(
                new MediaMetadata.Builder()
                    .setTitle("testTitle")
                    .setSubtitle("testSubtitle")
                    .setArtist("testArtist")
                    .setAlbumArtist("testAlbumArtist")
                    .setArtworkUri(Uri.parse("http://testArtworkUri"))
                    .setComposer("testComposer")
                    .setDiscNumber(42)
                    .setTrackNumber(23)
                    .build())
            .setMimeType(MimeTypes.APPLICATION_MPD)
            .setDrmConfiguration(
                new MediaItem.DrmConfiguration.Builder(C.WIDEVINE_UUID)
                    .setLicenseUri("http://license.com")
                    .setLicenseRequestHeaders(ImmutableMap.of("key", "value"))
                    .build())
            .build();

    DefaultMediaItemConverter converter = new DefaultMediaItemConverter();
    MediaQueueItem queueItem = converter.toMediaQueueItem(item);
    MediaItem reconstructedItem = converter.toMediaItem(queueItem);

    assertThat(reconstructedItem).isEqualTo(item);
  }

  @Test
  public void toMediaQueueItem_nonDefaultMediaId_usedAsContentId() {
    MediaItem.Builder builder = new MediaItem.Builder();
    MediaItem item =
        builder
            .setMediaId("fooBar")
            .setUri("http://example.com")
            .setMimeType(MimeTypes.APPLICATION_MPD)
            .build();

    DefaultMediaItemConverter converter = new DefaultMediaItemConverter();
    MediaQueueItem queueItem = converter.toMediaQueueItem(item);

    assertThat(queueItem.getMedia().getContentId()).isEqualTo("fooBar");
  }

  @Test
  public void toMediaQueueItem_defaultMediaId_uriAsContentId() {
    DefaultMediaItemConverter converter = new DefaultMediaItemConverter();
    MediaItem mediaItem =
        new MediaItem.Builder()
            .setUri("http://example.com")
            .setMimeType(MimeTypes.APPLICATION_MPD)
            .build();

    MediaQueueItem queueItem = converter.toMediaQueueItem(mediaItem);

    assertThat(queueItem.getMedia().getContentId()).isEqualTo("http://example.com");

    MediaItem secondMediaItem =
        new MediaItem.Builder()
            .setMediaId(MediaItem.DEFAULT_MEDIA_ID)
            .setUri("http://example.com")
            .setMimeType(MimeTypes.APPLICATION_MPD)
            .build();

    MediaQueueItem secondQueueItem = converter.toMediaQueueItem(secondMediaItem);

    assertThat(secondQueueItem.getMedia().getContentId()).isEqualTo("http://example.com");
  }
}
