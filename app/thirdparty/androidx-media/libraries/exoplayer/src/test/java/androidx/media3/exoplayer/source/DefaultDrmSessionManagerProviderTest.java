/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.source;

import static com.google.common.truth.Truth.assertThat;

import android.net.Uri;
import androidx.media3.common.C;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.drm.DefaultDrmSessionManagerProvider;
import androidx.media3.exoplayer.drm.DrmSessionManager;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit tests for {@link DefaultDrmSessionManagerProvider}. */
@RunWith(AndroidJUnit4.class)
public class DefaultDrmSessionManagerProviderTest {

  @Test
  public void create_noDrmProperties_createsNoopManager() {
    DrmSessionManager drmSessionManager =
        new DefaultDrmSessionManagerProvider().get(MediaItem.fromUri(Uri.EMPTY));

    assertThat(drmSessionManager).isEqualTo(DrmSessionManager.DRM_UNSUPPORTED);
  }

  @Test
  public void create_createsManager() {
    MediaItem mediaItem =
        new MediaItem.Builder()
            .setUri(Uri.EMPTY)
            .setDrmConfiguration(
                new MediaItem.DrmConfiguration.Builder(C.WIDEVINE_UUID)
                    .setLicenseUri(Uri.EMPTY)
                    .build())
            .build();

    DrmSessionManager drmSessionManager = new DefaultDrmSessionManagerProvider().get(mediaItem);

    assertThat(drmSessionManager).isNotEqualTo(DrmSessionManager.DRM_UNSUPPORTED);
  }

  @Test
  public void create_reusesCachedInstanceWherePossible() {
    MediaItem mediaItem1 =
        new MediaItem.Builder()
            .setUri("https://example.test/content-1")
            .setDrmConfiguration(new MediaItem.DrmConfiguration.Builder(C.WIDEVINE_UUID).build())
            .build();
    // Same DRM info as item1, but different URL to check it doesn't prevent re-using a manager.
    MediaItem mediaItem2 =
        new MediaItem.Builder()
            .setUri("https://example.test/content-2")
            .setDrmConfiguration(new MediaItem.DrmConfiguration.Builder(C.WIDEVINE_UUID).build())
            .build();
    // Different DRM info to 1 and 2, needs a different manager instance.
    MediaItem mediaItem3 =
        new MediaItem.Builder()
            .setUri("https://example.test/content-3")
            .setDrmConfiguration(
                new MediaItem.DrmConfiguration.Builder(C.WIDEVINE_UUID)
                    .setLicenseUri("https://example.test/license")
                    .build())
            .build();

    DefaultDrmSessionManagerProvider provider = new DefaultDrmSessionManagerProvider();
    DrmSessionManager drmSessionManager1 = provider.get(mediaItem1);
    DrmSessionManager drmSessionManager2 = provider.get(mediaItem2);
    DrmSessionManager drmSessionManager3 = provider.get(mediaItem3);

    // Get a manager for the first item again - expect it to be a different instance to last time
    // since we only cache one.
    DrmSessionManager drmSessionManager4 = provider.get(mediaItem1);

    assertThat(drmSessionManager1).isSameInstanceAs(drmSessionManager2);
    assertThat(drmSessionManager1).isNotSameInstanceAs(drmSessionManager3);
    assertThat(drmSessionManager1).isNotSameInstanceAs(drmSessionManager4);
  }
}
