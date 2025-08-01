/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.test.utils;

import static androidx.media3.test.utils.FakeTimeline.TimelineWindowDefinition.DEFAULT_WINDOW_DURATION_US;
import static androidx.media3.test.utils.FakeTimeline.TimelineWindowDefinition.DEFAULT_WINDOW_OFFSET_IN_FIRST_PERIOD_US;
import static com.google.common.truth.Truth.assertThat;

import androidx.media3.common.AdPlaybackState;
import androidx.media3.common.Timeline;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Tests for {@link FakeTimeline}. */
@RunWith(AndroidJUnit4.class)
public class FakeTimelineTest {

  @Test
  public void createMultiPeriodAdTimeline_firstPeriodIsAd() {
    Timeline.Window window = new Timeline.Window();
    Timeline.Period period = new Timeline.Period();
    Object windowId = new Object();
    int numberOfPlayedAds = 2;
    FakeTimeline timeline =
        FakeTimeline.createMultiPeriodAdTimeline(
            windowId,
            numberOfPlayedAds,
            /* isAdPeriodFlags...= */ true,
            false,
            true,
            true,
            true,
            false,
            true,
            true);

    assertThat(timeline.getWindowCount()).isEqualTo(1);
    assertThat(timeline.getPeriodCount()).isEqualTo(8);
    // Assert content periods and window duration.
    Timeline.Period contentPeriod1 = timeline.getPeriod(/* periodIndex= */ 1, period);
    Timeline.Period contentPeriod5 = timeline.getPeriod(/* periodIndex= */ 5, period);
    assertThat(contentPeriod1.durationUs).isEqualTo(DEFAULT_WINDOW_DURATION_US / 8);
    assertThat(contentPeriod5.durationUs).isEqualTo(DEFAULT_WINDOW_DURATION_US / 8);
    assertThat(contentPeriod1.getAdGroupCount()).isEqualTo(0);
    assertThat(contentPeriod5.getAdGroupCount()).isEqualTo(0);
    timeline.getWindow(/* windowIndex= */ 0, window);
    assertThat(window.uid).isEqualTo(windowId);
    assertThat(window.durationUs).isEqualTo(DEFAULT_WINDOW_DURATION_US);
    assertThat(window.positionInFirstPeriodUs).isEqualTo(DEFAULT_WINDOW_OFFSET_IN_FIRST_PERIOD_US);
    // Assert ad periods.
    int[] adIndices = {0, 2, 3, 4, 6};
    int adCounter = 0;
    for (int periodIndex : adIndices) {
      Timeline.Period adPeriod = timeline.getPeriod(periodIndex, period);
      assertThat(adPeriod.isServerSideInsertedAdGroup(0)).isTrue();
      assertThat(adPeriod.getAdGroupCount()).isEqualTo(1);
      if (adPeriod.getAdGroupCount() > 0) {
        if (adCounter < numberOfPlayedAds) {
          assertThat(adPeriod.getAdState(/* adGroupIndex= */ 0, /* adIndexInAdGroup= */ 0))
              .isEqualTo(AdPlaybackState.AD_STATE_PLAYED);
        } else {
          assertThat(adPeriod.getAdState(/* adGroupIndex= */ 0, /* adIndexInAdGroup= */ 0))
              .isEqualTo(AdPlaybackState.AD_STATE_UNAVAILABLE);
        }
        adCounter++;
      }
      long expectedDurationUs =
          (DEFAULT_WINDOW_DURATION_US / 8)
              + (periodIndex == 0 ? DEFAULT_WINDOW_OFFSET_IN_FIRST_PERIOD_US : 0);
      assertThat(adPeriod.durationUs).isEqualTo(expectedDurationUs);
      assertThat(adPeriod.getAdDurationUs(/* adGroupIndex= */ 0, /* adIndexInAdGroup= */ 0))
          .isEqualTo(expectedDurationUs);
    }
  }

  @Test
  public void createMultiPeriodAdTimeline_firstPeriodIsContent_correctWindowDurationUs() {
    Timeline.Window window = new Timeline.Window();
    FakeTimeline timeline =
        FakeTimeline.createMultiPeriodAdTimeline(
            /* windowId= */ new Object(),
            /* numberOfPlayedAds= */ 0,
            /* isAdPeriodFlags...= */ false,
            true,
            true,
            false);

    timeline.getWindow(/* windowIndex= */ 0, window);
    // Assert content periods and window duration.
    assertThat(window.durationUs).isEqualTo(DEFAULT_WINDOW_DURATION_US);
    assertThat(window.positionInFirstPeriodUs).isEqualTo(DEFAULT_WINDOW_OFFSET_IN_FIRST_PERIOD_US);
  }
}
