/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.google.common.truth.Truth.assertThat;

import androidx.media3.common.C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.ConditionVariable;
import androidx.media3.exoplayer.analytics.AnalyticsListener;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for the {@link AnalyticsListener#onAudioPositionAdvancing(AnalyticsListener.EventTime,
 * long)} callback.
 */
// Deliberately using System.currentTimeMillis for consistency with onAudioPositionAdvancing.
@SuppressWarnings("NowMillisWithoutTimeSource")
@RunWith(AndroidJUnit4.class)
public class AudioPositionAdvancingTest {

  private static final int TIMEOUT_MS = 10_000;

  // Regression test for b/378871275
  @Test
  public void calledWhenPlaybackResumesNotPaused() throws Exception {
    MediaItem mediaItem = MediaItem.fromUri("asset:///media/mp3/bear-id3.mp3");
    List<Long> playoutStartSystemTimes = Collections.synchronizedList(new ArrayList<>());
    ConditionVariable onAdvancingCalled = new ConditionVariable();
    AnalyticsListener analyticsListener =
        new AnalyticsListener() {
          @Override
          public void onAudioPositionAdvancing(EventTime eventTime, long playoutStartSystemTimeMs) {
            playoutStartSystemTimes.add(playoutStartSystemTimeMs);
            onAdvancingCalled.open();
          }
        };
    AtomicReference<ExoPlayer> player = new AtomicReference<>();
    AtomicLong playbackTriggeredSystemTimeMs = new AtomicLong(C.TIME_UNSET);
    getInstrumentation()
        .runOnMainSync(
            () -> {
              player.set(new ExoPlayer.Builder(getInstrumentation().getContext()).build());
              player.get().addAnalyticsListener(analyticsListener);
              player.get().setMediaItem(mediaItem);
              player.get().prepare();
              playbackTriggeredSystemTimeMs.set(System.currentTimeMillis());
              player.get().play();
            });

    assertThat(onAdvancingCalled.block(TIMEOUT_MS)).isTrue();
    long currentTimeMs = System.currentTimeMillis();
    long playoutStartSystemTimeMs = Iterables.getOnlyElement(playoutStartSystemTimes);
    assertThat(playoutStartSystemTimeMs).isAtLeast(playbackTriggeredSystemTimeMs.get());
    assertThat(playoutStartSystemTimeMs).isAtMost(currentTimeMs);

    onAdvancingCalled.close();
    getInstrumentation().runOnMainSync(() -> player.get().pause());

    // Expect the callback to *not* be called.
    assertThat(onAdvancingCalled.block(50)).isFalse();

    getInstrumentation()
        .runOnMainSync(
            () -> {
              playbackTriggeredSystemTimeMs.set(System.currentTimeMillis());
              player.get().play();
            });
    assertThat(onAdvancingCalled.block(TIMEOUT_MS)).isTrue();
    currentTimeMs = System.currentTimeMillis();
    playoutStartSystemTimeMs = Iterables.getLast(playoutStartSystemTimes);
    assertThat(playoutStartSystemTimeMs).isAtLeast(playbackTriggeredSystemTimeMs.get());
    assertThat(playoutStartSystemTimeMs).isAtMost(currentTimeMs);

    getInstrumentation().runOnMainSync(() -> player.get().release());
  }

  @Test
  public void pauseThenPlayInSameLooperIteration() throws Exception {
    MediaItem mediaItem = MediaItem.fromUri("asset:///media/mp3/bear-id3.mp3");
    List<Long> playoutStartSystemTimes = Collections.synchronizedList(new ArrayList<>());
    ConditionVariable onAdvancingCalled = new ConditionVariable();
    AnalyticsListener analyticsListener =
        new AnalyticsListener() {
          @Override
          public void onAudioPositionAdvancing(EventTime eventTime, long playoutStartSystemTimeMs) {
            playoutStartSystemTimes.add(playoutStartSystemTimeMs);
            onAdvancingCalled.open();
          }
        };
    AtomicReference<ExoPlayer> player = new AtomicReference<>();
    getInstrumentation()
        .runOnMainSync(
            () -> {
              player.set(new ExoPlayer.Builder(getInstrumentation().getContext()).build());
              player.get().addAnalyticsListener(analyticsListener);
              player.get().setMediaItem(mediaItem);
              player.get().prepare();
              player.get().play();
            });

    assertThat(onAdvancingCalled.block(TIMEOUT_MS)).isTrue();
    onAdvancingCalled.close();

    AtomicLong playbackTriggeredSystemTimeMs = new AtomicLong(C.TIME_UNSET);
    getInstrumentation()
        .runOnMainSync(
            () -> {
              player.get().pause();
              playbackTriggeredSystemTimeMs.set(System.currentTimeMillis());
              player.get().play();
            });

    assertThat(onAdvancingCalled.block(TIMEOUT_MS)).isTrue();
    long currentTimeMs = System.currentTimeMillis();
    assertThat(playoutStartSystemTimes).hasSize(2);
    long playoutStartSystemTimeMs = playoutStartSystemTimes.get(1);
    assertThat(playoutStartSystemTimeMs).isAtLeast(playbackTriggeredSystemTimeMs.get());
    assertThat(playoutStartSystemTimeMs).isAtMost(currentTimeMs);

    getInstrumentation().runOnMainSync(() -> player.get().release());
  }
}
