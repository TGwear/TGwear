/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.e2etest;

import static org.robolectric.annotation.GraphicsMode.Mode.NATIVE;

import android.content.Context;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.test.utils.CapturingRenderersFactory;
import androidx.media3.test.utils.DumpFileAsserts;
import androidx.media3.test.utils.FakeClock;
import androidx.media3.test.utils.robolectric.PlaybackOutput;
import androidx.media3.test.utils.robolectric.TestPlayerRunHelper;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.GraphicsMode;

/** End-to-end tests using image samples. */
@RunWith(AndroidJUnit4.class)
@GraphicsMode(value = NATIVE)
public class ImagePlaybackTest {

  @Test
  public void playImagePlaylist_withSeek_rendersExpectedImages() throws Exception {
    Context applicationContext = ApplicationProvider.getApplicationContext();
    CapturingRenderersFactory renderersFactory = new CapturingRenderersFactory(applicationContext);
    ExoPlayer player =
        new ExoPlayer.Builder(applicationContext, renderersFactory)
            .setClock(new FakeClock(/* isAutoAdvancing= */ true))
            .build();
    PlaybackOutput playbackOutput = PlaybackOutput.register(player, renderersFactory);
    MediaItem mediaItem1 =
        new MediaItem.Builder()
            .setUri("asset:///media/png/media3test.png")
            .setImageDurationMs(3000L)
            .build();
    MediaItem mediaItem2 =
        new MediaItem.Builder()
            .setUri("asset:///media/png/non-motion-photo-shortened.png")
            .setImageDurationMs(3000L)
            .build();
    player.setMediaItems(ImmutableList.of(mediaItem1, mediaItem2));
    player.prepare();

    TestPlayerRunHelper.playUntilPosition(player, /* mediaItemIndex= */ 0, /* positionMs= */ 1000L);
    player.seekTo(/* mediaItemIndex= */ 0, /* positionMs= */ 2000L);
    TestPlayerRunHelper.runUntilPlaybackState(player, Player.STATE_ENDED);
    player.release();

    DumpFileAsserts.assertOutput(
        applicationContext, playbackOutput, "playbackdumps/image/image_playlist_with_seek.dump");
  }
}
