/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.ima;

import static androidx.media3.test.utils.robolectric.TestPlayerRunHelper.advance;
import static com.google.common.truth.Truth.assertThat;

import android.content.Context;
import android.widget.LinearLayout;
import androidx.media3.common.AdPlaybackState;
import androidx.media3.common.C;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.ima.ImaServerSideAdInsertionMediaSource.AdsLoader.State;
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.source.ads.ServerSideAdInsertionUtil;
import androidx.media3.test.utils.TestExoPlayerBuilder;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit tests for {@link ImaServerSideAdInsertionMediaSource}. */
@RunWith(AndroidJUnit4.class)
public class ImaServerSideAdInsertionMediaSourceTest {

  @Test
  public void adsLoaderStateToBundle_marshallAndUnmarshalling_resultIsEqual() {
    AdPlaybackState firstAdPlaybackState =
        ServerSideAdInsertionUtil.addAdGroupToAdPlaybackState(
            new AdPlaybackState("adsId1"),
            /* fromPositionUs= */ 0,
            /* contentResumeOffsetUs= */ 10,
            /* adDurationsUs...= */ 5_000_000,
            10_000_000,
            20_000_000);
    AdPlaybackState secondAdPlaybackState =
        ServerSideAdInsertionUtil.addAdGroupToAdPlaybackState(
                new AdPlaybackState("adsId2"),
                /* fromPositionUs= */ 0,
                /* contentResumeOffsetUs= */ 10,
                /* adDurationsUs...= */ 10_000_000)
            .withPlayedAd(/* adGroupIndex= */ 0, /* adIndexInAdGroup= */ 0);
    AdPlaybackState thirdAdPlaybackState =
        ServerSideAdInsertionUtil.addAdGroupToAdPlaybackState(
            new AdPlaybackState("adsId3"),
            /* fromPositionUs= */ C.TIME_END_OF_SOURCE,
            /* contentResumeOffsetUs= */ 10,
            /* adDurationsUs...= */ 10_000_000);
    thirdAdPlaybackState =
        ServerSideAdInsertionUtil.addAdGroupToAdPlaybackState(
                thirdAdPlaybackState,
                /* fromPositionUs= */ 0,
                /* contentResumeOffsetUs= */ 10,
                /* adDurationsUs...= */ 10_000_000)
            .withRemovedAdGroupCount(1);
    State state =
        new State(
            ImmutableMap.<String, AdPlaybackState>builder()
                .put("adsId1", firstAdPlaybackState)
                .put("adsId2", secondAdPlaybackState)
                .put("adsId3", thirdAdPlaybackState)
                .buildOrThrow());

    assertThat(State.fromBundle(state.toBundle())).isEqualTo(state);
  }

  @Test
  public void clearPlaylist_withAdsSource_handlesCleanupWithoutThrowing() throws Exception {
    Context context = ApplicationProvider.getApplicationContext();
    ExoPlayer player = new TestExoPlayerBuilder(context).build();
    ImaServerSideAdInsertionMediaSource.AdsLoader adsLoader =
        new ImaServerSideAdInsertionMediaSource.AdsLoader.Builder(
                context, /* adViewProvider= */ () -> new LinearLayout(context))
            .build();
    adsLoader.setPlayer(player);
    MediaSource mediaSource =
        new ImaServerSideAdInsertionMediaSource.Factory(
                adsLoader, new DefaultMediaSourceFactory(context))
            .createMediaSource(
                MediaItem.fromUri("ssai://dai.google.com/?assetKey=ABC&format=0&adsId=2"));
    player.setMediaSource(mediaSource);
    player.prepare();
    advance(player).untilPendingCommandsAreFullyHandled();

    // Clearing the playlist will cause internal state of the ads source to be invalid and
    // potentially accessing empty timelines. See b/354026260. The test simply ensures that clearing
    // the playlist will not throw any exceptions.
    player.clearMediaItems();
    advance(player).untilPendingCommandsAreFullyHandled();
    player.release();
  }
}
