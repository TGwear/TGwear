/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.hls;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.google.common.truth.Truth.assertThat;

import android.content.Context;
import androidx.media3.common.C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MimeTypes;
import androidx.media3.exoplayer.analytics.PlayerId;
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.test.utils.FakeDataSource;
import androidx.media3.test.utils.robolectric.RobolectricUtil;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit test for creating HLS media sources with the {@link DefaultMediaSourceFactory}. */
@RunWith(AndroidJUnit4.class)
public class DefaultMediaSourceFactoryTest {

  private static final String URI_MEDIA = "http://exoplayer.dev/video";

  @Test
  public void createMediaSource_withMimeType_hlsSource() {
    DefaultMediaSourceFactory defaultMediaSourceFactory =
        new DefaultMediaSourceFactory((Context) ApplicationProvider.getApplicationContext());
    MediaItem mediaItem =
        new MediaItem.Builder().setUri(URI_MEDIA).setMimeType(MimeTypes.APPLICATION_M3U8).build();

    MediaSource mediaSource = defaultMediaSourceFactory.createMediaSource(mediaItem);

    assertThat(mediaSource).isInstanceOf(HlsMediaSource.class);
  }

  @Test
  public void createMediaSource_withMimeTypeLowerCaseLetters_hlsSource() {
    DefaultMediaSourceFactory defaultMediaSourceFactory =
        new DefaultMediaSourceFactory((Context) ApplicationProvider.getApplicationContext());
    MediaItem mediaItem =
        new MediaItem.Builder().setUri(URI_MEDIA).setMimeType("application/x-mpegurl").build();

    MediaSource mediaSource = defaultMediaSourceFactory.createMediaSource(mediaItem);

    assertThat(mediaSource).isInstanceOf(HlsMediaSource.class);
  }

  @Test
  public void createMediaSource_withTag_tagInSource() {
    Object tag = new Object();
    DefaultMediaSourceFactory defaultMediaSourceFactory =
        new DefaultMediaSourceFactory((Context) ApplicationProvider.getApplicationContext());
    MediaItem mediaItem =
        new MediaItem.Builder()
            .setUri(URI_MEDIA)
            .setMimeType(MimeTypes.APPLICATION_M3U8)
            .setTag(tag)
            .build();

    MediaSource mediaSource = defaultMediaSourceFactory.createMediaSource(mediaItem);

    assertThat(mediaSource.getMediaItem().localConfiguration.tag).isEqualTo(tag);
  }

  @Test
  public void createMediaSource_withPath_hlsSource() {
    DefaultMediaSourceFactory defaultMediaSourceFactory =
        new DefaultMediaSourceFactory((Context) ApplicationProvider.getApplicationContext());
    MediaItem mediaItem = new MediaItem.Builder().setUri(URI_MEDIA + "/file.m3u8").build();

    MediaSource mediaSource = defaultMediaSourceFactory.createMediaSource(mediaItem);

    assertThat(mediaSource).isInstanceOf(HlsMediaSource.class);
  }

  @Test
  public void getSupportedTypes_hlsModule_containsTypeHls() {
    int[] supportedTypes =
        new DefaultMediaSourceFactory((Context) ApplicationProvider.getApplicationContext())
            .getSupportedTypes();

    assertThat(supportedTypes).asList().containsExactly(C.CONTENT_TYPE_OTHER, C.CONTENT_TYPE_HLS);
  }

  @Test
  public void createMediaSource_withSetDataSourceFactory_usesDataSourceFactory() throws Exception {
    FakeDataSource fakeDataSource = new FakeDataSource();
    DefaultMediaSourceFactory defaultMediaSourceFactory =
        new DefaultMediaSourceFactory((Context) ApplicationProvider.getApplicationContext())
            .setDataSourceFactory(() -> fakeDataSource);

    prepareHlsUrlAndWaitForPrepareError(defaultMediaSourceFactory);

    assertThat(fakeDataSource.getAndClearOpenedDataSpecs()).asList().isNotEmpty();
  }

  @Test
  public void
      createMediaSource_usingDefaultDataSourceFactoryAndSetDataSourceFactory_usesUpdatesDataSourceFactory()
          throws Exception {
    FakeDataSource fakeDataSource = new FakeDataSource();
    DefaultMediaSourceFactory defaultMediaSourceFactory =
        new DefaultMediaSourceFactory((Context) ApplicationProvider.getApplicationContext());

    // Use default DataSource.Factory first.
    prepareHlsUrlAndWaitForPrepareError(defaultMediaSourceFactory);
    defaultMediaSourceFactory.setDataSourceFactory(() -> fakeDataSource);
    prepareHlsUrlAndWaitForPrepareError(defaultMediaSourceFactory);

    assertThat(fakeDataSource.getAndClearOpenedDataSpecs()).asList().isNotEmpty();
  }

  private static void prepareHlsUrlAndWaitForPrepareError(
      DefaultMediaSourceFactory defaultMediaSourceFactory) throws Exception {
    MediaSource mediaSource =
        defaultMediaSourceFactory.createMediaSource(MediaItem.fromUri(URI_MEDIA + "/file.m3u8"));
    getInstrumentation()
        .runOnMainSync(
            () ->
                mediaSource.prepareSource(
                    (source, timeline) -> {}, /* mediaTransferListener= */ null, PlayerId.UNSET));
    // We don't expect this to prepare successfully.
    RobolectricUtil.runMainLooperUntil(
        () -> {
          try {
            mediaSource.maybeThrowSourceInfoRefreshError();
            return false;
          } catch (IOException e) {
            return true;
          }
        });
  }
}
