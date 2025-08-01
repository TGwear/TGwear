/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.e2etest;

import static com.google.common.truth.Truth.assertThat;
import static org.robolectric.annotation.GraphicsMode.Mode.NATIVE;

import android.content.Context;
import androidx.media3.common.C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.common.util.Clock;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.test.utils.CapturingRenderersFactory;
import androidx.media3.test.utils.DumpFileAsserts;
import androidx.media3.test.utils.FakeClock;
import androidx.media3.test.utils.robolectric.PlaybackOutput;
import androidx.media3.test.utils.robolectric.TestPlayerRunHelper;
import androidx.test.core.app.ApplicationProvider;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.ParameterizedRobolectricTestRunner;
import org.robolectric.ParameterizedRobolectricTestRunner.Parameter;
import org.robolectric.ParameterizedRobolectricTestRunner.Parameters;
import org.robolectric.annotation.GraphicsMode;

/** Parameterized end-to-end tests using image samples. */
@RunWith(ParameterizedRobolectricTestRunner.class)
@GraphicsMode(value = NATIVE)
public class ParameterizedImagePlaybackTest {
  @Parameter public Set<String> inputFiles;

  @Parameters(name = "{0}")
  public static List<Set<String>> mediaSamples() {
    // Robolectric's ShadowNativeBitmapFactory doesn't support decoding HEIF format, so we don't
    // test that here.
    // TODO b/300457060 - Find out why jpegs cause flaky failures in this test and then add jpegs to
    //  this list if possible.
    return new ArrayList<>(
        Collections2.filter(
            Sets.powerSet(
                ImmutableSet.of(
                    "png/media3test.png",
                    "bmp/non-motion-photo-shortened-cropped.bmp",
                    "png/non-motion-photo-shortened.png",
                    "webp/ic_launcher_round.webp",
                    "avif/white-1x1.avif")),
            /* predicate= */ input -> !input.isEmpty()));
  }

  @Test
  public void test() throws Exception {
    Context applicationContext = ApplicationProvider.getApplicationContext();
    CapturingRenderersFactory renderersFactory = new CapturingRenderersFactory(applicationContext);
    Clock clock = new FakeClock(/* isAutoAdvancing= */ true);
    ExoPlayer player =
        new ExoPlayer.Builder(applicationContext, renderersFactory).setClock(clock).build();
    PlaybackOutput playbackOutput = PlaybackOutput.register(player, renderersFactory);
    List<String> sortedInputFiles = new ArrayList<>(inputFiles);
    Collections.sort(sortedInputFiles);
    List<MediaItem> mediaItems = new ArrayList<>(inputFiles.size());
    long totalDurationMs = 0;
    long currentDurationMs = 3 * C.MILLIS_PER_SECOND;
    for (String inputFile : sortedInputFiles) {
      mediaItems.add(
          new MediaItem.Builder()
              .setUri("asset:///media/" + inputFile)
              .setImageDurationMs(currentDurationMs)
              .build());
      totalDurationMs += currentDurationMs;
      if (currentDurationMs < 5 * C.MILLIS_PER_SECOND) {
        currentDurationMs += C.MILLIS_PER_SECOND;
      }
    }
    player.setMediaItems(mediaItems);
    player.prepare();
    TestPlayerRunHelper.runUntilPlaybackState(player, Player.STATE_READY);
    long playerStartedMs = clock.elapsedRealtime();
    player.play();
    TestPlayerRunHelper.runUntilPlaybackState(player, Player.STATE_ENDED);
    long playbackDurationMs = clock.elapsedRealtime() - playerStartedMs;
    player.release();
    assertThat(playbackDurationMs).isAtLeast(totalDurationMs);
    DumpFileAsserts.assertOutput(
        applicationContext,
        playbackOutput,
        "playbackdumps/image/" + generateName(sortedInputFiles) + ".dump");
  }

  private static String generateName(List<String> sortedInputFiles) {
    StringBuilder name = new StringBuilder();
    for (String inputFile : sortedInputFiles) {
      name.append(inputFile, inputFile.lastIndexOf("/") + 1, inputFile.length()).append("+");
    }
    name.setLength(name.length() - 1);
    return name.toString();
  }
}
