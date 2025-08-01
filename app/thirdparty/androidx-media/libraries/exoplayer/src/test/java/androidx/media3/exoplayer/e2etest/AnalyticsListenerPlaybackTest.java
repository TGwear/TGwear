/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.e2etest;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import android.content.Context;
import android.net.Uri;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.analytics.AnalyticsListener;
import androidx.media3.exoplayer.source.LoadEventInfo;
import androidx.media3.test.utils.FakeClock;
import androidx.media3.test.utils.robolectric.ShadowMediaCodecConfig;
import androidx.media3.test.utils.robolectric.TestPlayerRunHelper;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

/** End-to-end tests of events reported to {@link AnalyticsListener}. */
@RunWith(AndroidJUnit4.class)
public class AnalyticsListenerPlaybackTest {

  @Rule
  public ShadowMediaCodecConfig mediaCodecConfig =
      ShadowMediaCodecConfig.forAllSupportedMimeTypes();

  @Test
  public void loadEventsReportedAsExpected() throws Exception {
    Context applicationContext = ApplicationProvider.getApplicationContext();
    ExoPlayer player =
        new ExoPlayer.Builder(applicationContext)
            .setClock(new FakeClock(/* isAutoAdvancing= */ true))
            .build();
    AnalyticsListener mockAnalyticsListener = mock(AnalyticsListener.class);
    player.addAnalyticsListener(mockAnalyticsListener);
    Uri mediaUri = Uri.parse("asset:///media/mp4/sample.mp4");
    MediaItem mediaItem = new MediaItem.Builder().setUri(mediaUri).build();

    player.setMediaItem(mediaItem);
    player.prepare();
    player.play();
    TestPlayerRunHelper.runUntilPlaybackState(player, Player.STATE_ENDED);
    player.release();

    ArgumentCaptor<LoadEventInfo> loadStartedEventInfoCaptor =
        ArgumentCaptor.forClass(LoadEventInfo.class);
    verify(mockAnalyticsListener, atLeastOnce())
        .onLoadStarted(any(), loadStartedEventInfoCaptor.capture(), any(), anyInt());
    List<Uri> loadStartedUris =
        Lists.transform(loadStartedEventInfoCaptor.getAllValues(), i -> i.uri);
    List<Uri> loadStartedDataSpecUris =
        Lists.transform(loadStartedEventInfoCaptor.getAllValues(), i -> i.dataSpec.uri);
    // Remove duplicates in case the load was split into multiple reads.
    assertThat(ImmutableSet.copyOf(loadStartedUris)).containsExactly(mediaUri);
    // The two sources of URI should match (because there's no redirection).
    assertThat(loadStartedDataSpecUris).containsExactlyElementsIn(loadStartedUris).inOrder();
    ArgumentCaptor<LoadEventInfo> loadCompletedEventInfoCaptor =
        ArgumentCaptor.forClass(LoadEventInfo.class);
    verify(mockAnalyticsListener, atLeastOnce())
        .onLoadCompleted(any(), loadCompletedEventInfoCaptor.capture(), any());
    List<Uri> loadCompletedUris =
        Lists.transform(loadCompletedEventInfoCaptor.getAllValues(), i -> i.uri);
    List<Uri> loadCompletedDataSpecUris =
        Lists.transform(loadCompletedEventInfoCaptor.getAllValues(), i -> i.dataSpec.uri);
    // Every started load should be completed.
    assertThat(loadCompletedUris).containsExactlyElementsIn(loadStartedUris);
    assertThat(loadCompletedDataSpecUris).containsExactlyElementsIn(loadStartedUris);
  }
}
