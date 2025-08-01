/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.session.legacy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.os.Parcel;
import androidx.media3.session.legacy.MediaBrowserCompat.MediaItem;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Test {@link MediaItem}. */
@RunWith(AndroidJUnit4.class)
public class MediaItemTest {
  private static final String DESCRIPTION = "test_description";
  private static final String MEDIA_ID = "test_media_id";
  private static final String TITLE = "test_title";
  private static final String SUBTITLE = "test_subtitle";

  @Test
  public void testBrowsableMediaItem() {
    MediaDescriptionCompat description =
        new MediaDescriptionCompat.Builder()
            .setDescription(DESCRIPTION)
            .setMediaId(MEDIA_ID)
            .setTitle(TITLE)
            .setSubtitle(SUBTITLE)
            .build();
    MediaItem mediaItem = new MediaItem(description, MediaItem.FLAG_BROWSABLE);

    assertEquals(description.toString(), mediaItem.getDescription().toString());
    assertEquals(MEDIA_ID, mediaItem.getMediaId());
    assertEquals(MediaItem.FLAG_BROWSABLE, mediaItem.getFlags());
    assertTrue(mediaItem.isBrowsable());
    assertFalse(mediaItem.isPlayable());
    assertEquals(0, mediaItem.describeContents());

    // Test writeToParcel
    Parcel p = Parcel.obtain();
    mediaItem.writeToParcel(p, 0);
    p.setDataPosition(0);
    assertEquals(mediaItem.getFlags(), p.readInt());
    assertEquals(
        description.toString(), MediaDescriptionCompat.CREATOR.createFromParcel(p).toString());
    p.recycle();
  }

  @Test
  public void testPlayableMediaItem() {
    MediaDescriptionCompat description =
        new MediaDescriptionCompat.Builder()
            .setDescription(DESCRIPTION)
            .setMediaId(MEDIA_ID)
            .setTitle(TITLE)
            .setSubtitle(SUBTITLE)
            .build();
    MediaItem mediaItem = new MediaItem(description, MediaItem.FLAG_PLAYABLE);

    assertEquals(description.toString(), mediaItem.getDescription().toString());
    assertEquals(MEDIA_ID, mediaItem.getMediaId());
    assertEquals(MediaItem.FLAG_PLAYABLE, mediaItem.getFlags());
    assertFalse(mediaItem.isBrowsable());
    assertTrue(mediaItem.isPlayable());
    assertEquals(0, mediaItem.describeContents());

    // Test writeToParcel
    Parcel p = Parcel.obtain();
    mediaItem.writeToParcel(p, 0);
    p.setDataPosition(0);
    assertEquals(mediaItem.getFlags(), p.readInt());
    assertEquals(
        description.toString(), MediaDescriptionCompat.CREATOR.createFromParcel(p).toString());
    p.recycle();
  }
}
