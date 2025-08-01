/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.session;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import android.os.Bundle;
import android.os.Looper;
import androidx.media3.common.Player;
import androidx.media3.common.Timeline;
import androidx.media3.test.utils.FakeTimeline;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/** Tests for {@link PlayerWrapper}. */
@RunWith(AndroidJUnit4.class)
public class PlayerWrapperTest {

  @Rule public final MockitoRule mockito = MockitoJUnit.rule();

  @Mock private Player player;

  private PlayerWrapper playerWrapper;

  @Before
  public void setUp() {
    playerWrapper =
        new PlayerWrapper(
            player,
            /* playIfSuppressed= */ true,
            /* customLayout= */ ImmutableList.of(),
            /* mediaButtonPreferences= */ ImmutableList.of(),
            SessionCommands.EMPTY,
            Player.Commands.EMPTY,
            /* legacyExtras= */ Bundle.EMPTY);
    when(player.isCommandAvailable(anyInt())).thenReturn(true);
    when(player.getApplicationLooper()).thenReturn(Looper.myLooper());
  }

  @Test
  public void
      getCurrentTimelineWithCommandCheck_withoutCommandGetTimelineAndGetCurrentMediaItem_isEmpty() {
    when(player.isCommandAvailable(Player.COMMAND_GET_TIMELINE)).thenReturn(false);
    when(player.isCommandAvailable(Player.COMMAND_GET_CURRENT_MEDIA_ITEM)).thenReturn(false);
    when(player.getCurrentTimeline()).thenReturn(new FakeTimeline(/* windowCount= */ 3));

    Timeline currentTimeline = playerWrapper.getCurrentTimelineWithCommandCheck();

    assertThat(currentTimeline.isEmpty()).isTrue();
  }

  @Test
  public void getCurrentTimelineWithCommandCheck_withoutCommandGetTimelineWhenEmpty_isEmpty() {
    when(player.isCommandAvailable(Player.COMMAND_GET_TIMELINE)).thenReturn(false);
    when(player.isCommandAvailable(Player.COMMAND_GET_CURRENT_MEDIA_ITEM)).thenReturn(true);
    when(player.getCurrentTimeline()).thenReturn(Timeline.EMPTY);

    Timeline currentTimeline = playerWrapper.getCurrentTimelineWithCommandCheck();

    assertThat(currentTimeline.isEmpty()).isTrue();
  }

  @Test
  public void
      getCurrentTimelineWithCommandCheck_withoutCommandGetTimelineWhenMultipleItems_hasSingleItemTimeline() {
    when(player.isCommandAvailable(Player.COMMAND_GET_TIMELINE)).thenReturn(false);
    when(player.isCommandAvailable(Player.COMMAND_GET_CURRENT_MEDIA_ITEM)).thenReturn(true);
    when(player.getCurrentTimeline()).thenReturn(new FakeTimeline(/* windowCount= */ 3));

    Timeline currentTimeline = playerWrapper.getCurrentTimelineWithCommandCheck();

    assertThat(currentTimeline.getWindowCount()).isEqualTo(1);
  }

  @Test
  public void getCurrentTimelineWithCommandCheck_withCommandGetTimeline_returnOriginalTimeline() {
    when(player.isCommandAvailable(Player.COMMAND_GET_TIMELINE)).thenReturn(true);
    when(player.isCommandAvailable(Player.COMMAND_GET_CURRENT_MEDIA_ITEM)).thenReturn(false);
    when(player.getCurrentTimeline()).thenReturn(new FakeTimeline(/* windowCount= */ 3));

    Timeline currentTimeline = playerWrapper.getCurrentTimelineWithCommandCheck();

    assertThat(currentTimeline.getWindowCount()).isEqualTo(3);
  }

  @Test
  public void createSessionPositionInfoForBundling() {
    int testAdGroupIndex = 12;
    int testAdIndexInAdGroup = 99;
    boolean testIsPlayingAd = true;
    long testDurationMs = 5000;
    long testCurrentPositionMs = 223;
    long testBufferedPositionMs = 500;
    int testBufferedPercentage = 10;
    long testTotalBufferedDurationMs = 30;
    long testCurrentLiveOffsetMs = 212;
    long testContentDurationMs = 6000;
    long testContentPositionMs = 333;
    long testContentBufferedPositionMs = 2223;
    int testmediaItemIndex = 7;
    int testPeriodIndex = 8;
    when(player.getCurrentAdGroupIndex()).thenReturn(testAdGroupIndex);
    when(player.getCurrentAdIndexInAdGroup()).thenReturn(testAdIndexInAdGroup);
    when(player.isPlayingAd()).thenReturn(testIsPlayingAd);
    when(player.getDuration()).thenReturn(testDurationMs);
    when(player.getCurrentPosition()).thenReturn(testCurrentPositionMs);
    when(player.getBufferedPosition()).thenReturn(testBufferedPositionMs);
    when(player.getBufferedPercentage()).thenReturn(testBufferedPercentage);
    when(player.getTotalBufferedDuration()).thenReturn(testTotalBufferedDurationMs);
    when(player.getCurrentLiveOffset()).thenReturn(testCurrentLiveOffsetMs);
    when(player.getContentDuration()).thenReturn(testContentDurationMs);
    when(player.getContentPosition()).thenReturn(testContentPositionMs);
    when(player.getContentBufferedPosition()).thenReturn(testContentBufferedPositionMs);
    when(player.getCurrentMediaItemIndex()).thenReturn(testmediaItemIndex);
    when(player.getCurrentPeriodIndex()).thenReturn(testPeriodIndex);

    SessionPositionInfo sessionPositionInfo = playerWrapper.createSessionPositionInfoForBundling();

    assertThat(sessionPositionInfo.positionInfo.positionMs).isEqualTo(testCurrentPositionMs);
    assertThat(sessionPositionInfo.positionInfo.contentPositionMs).isEqualTo(testContentPositionMs);
    assertThat(sessionPositionInfo.positionInfo.adGroupIndex).isEqualTo(testAdGroupIndex);
    assertThat(sessionPositionInfo.positionInfo.adIndexInAdGroup).isEqualTo(testAdIndexInAdGroup);
    assertThat(sessionPositionInfo.positionInfo.mediaItemIndex).isEqualTo(testmediaItemIndex);
    assertThat(sessionPositionInfo.positionInfo.periodIndex).isEqualTo(testPeriodIndex);
    assertThat(sessionPositionInfo.isPlayingAd).isEqualTo(testIsPlayingAd);
    assertThat(sessionPositionInfo.durationMs).isEqualTo(testDurationMs);
    assertThat(sessionPositionInfo.bufferedPositionMs).isEqualTo(testBufferedPositionMs);
    assertThat(sessionPositionInfo.bufferedPercentage).isEqualTo(testBufferedPercentage);
    assertThat(sessionPositionInfo.totalBufferedDurationMs).isEqualTo(testTotalBufferedDurationMs);
    assertThat(sessionPositionInfo.currentLiveOffsetMs).isEqualTo(testCurrentLiveOffsetMs);
    assertThat(sessionPositionInfo.contentDurationMs).isEqualTo(testContentDurationMs);
    assertThat(sessionPositionInfo.contentBufferedPositionMs)
        .isEqualTo(testContentBufferedPositionMs);
  }
}
