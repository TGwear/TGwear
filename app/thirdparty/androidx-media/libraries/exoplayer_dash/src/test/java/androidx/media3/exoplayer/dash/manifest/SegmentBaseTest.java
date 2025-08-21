/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.dash.manifest;

import static com.google.common.truth.Truth.assertThat;

import androidx.media3.common.C;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit test for {@link SegmentBase}. */
@RunWith(AndroidJUnit4.class)
public final class SegmentBaseTest {

  @Test
  public void getFirstAvailableSegmentNum_unboundedSegmentTemplate() {
    long periodStartUnixTimeUs = 123_000_000_000_000L;
    SegmentBase.SegmentTemplate segmentTemplate =
        new SegmentBase.SegmentTemplate(
            /* initialization= */ null,
            /* timescale= */ 1000,
            /* presentationTimeOffset= */ 0,
            /* startNumber= */ 42,
            /* endNumber= */ C.INDEX_UNSET,
            /* duration= */ 2000,
            /* segmentTimeline= */ null,
            /* availabilityTimeOffsetUs= */ 500_000,
            /* initializationTemplate= */ null,
            /* mediaTemplate= */ null,
            /* timeShiftBufferDepthUs= */ 6_000_000,
            /* periodStartUnixTimeUs= */ periodStartUnixTimeUs);

    assertThat(
            segmentTemplate.getFirstAvailableSegmentNum(
                /* periodDurationUs= */ C.TIME_UNSET,
                /* nowUnixTimeUs= */ periodStartUnixTimeUs - 10_000_000))
        .isEqualTo(42);
    assertThat(
            segmentTemplate.getFirstAvailableSegmentNum(
                /* periodDurationUs= */ C.TIME_UNSET, /* nowUnixTimeUs= */ periodStartUnixTimeUs))
        .isEqualTo(42);
    assertThat(
            segmentTemplate.getFirstAvailableSegmentNum(
                /* periodDurationUs= */ C.TIME_UNSET,
                /* nowUnixTimeUs= */ periodStartUnixTimeUs + 7_999_999))
        .isEqualTo(42);
    assertThat(
            segmentTemplate.getFirstAvailableSegmentNum(
                /* periodDurationUs= */ C.TIME_UNSET,
                /* nowUnixTimeUs= */ periodStartUnixTimeUs + 8_000_000))
        .isEqualTo(43);
    assertThat(
            segmentTemplate.getFirstAvailableSegmentNum(
                /* periodDurationUs= */ C.TIME_UNSET,
                /* nowUnixTimeUs= */ periodStartUnixTimeUs + 9_999_999))
        .isEqualTo(43);
    assertThat(
            segmentTemplate.getFirstAvailableSegmentNum(
                /* periodDurationUs= */ C.TIME_UNSET,
                /* nowUnixTimeUs= */ periodStartUnixTimeUs + 10_000_000))
        .isEqualTo(44);
  }

  @Test
  public void getAvailableSegmentCount_unboundedSegmentTemplate() {
    long periodStartUnixTimeUs = 123_000_000_000_000L;
    SegmentBase.SegmentTemplate segmentTemplate =
        new SegmentBase.SegmentTemplate(
            /* initialization= */ null,
            /* timescale= */ 1000,
            /* presentationTimeOffset= */ 0,
            /* startNumber= */ 42,
            /* endNumber= */ C.INDEX_UNSET,
            /* duration= */ 2000,
            /* segmentTimeline= */ null,
            /* availabilityTimeOffsetUs= */ 500_000,
            /* initializationTemplate= */ null,
            /* mediaTemplate= */ null,
            /* timeShiftBufferDepthUs= */ 6_000_000,
            /* periodStartUnixTimeUs= */ periodStartUnixTimeUs);

    assertThat(
            segmentTemplate.getAvailableSegmentCount(
                /* periodDurationUs= */ C.TIME_UNSET,
                /* nowUnixTimeUs= */ periodStartUnixTimeUs - 10_000_000))
        .isEqualTo(0);
    assertThat(
            segmentTemplate.getAvailableSegmentCount(
                /* periodDurationUs= */ C.TIME_UNSET, /* nowUnixTimeUs= */ periodStartUnixTimeUs))
        .isEqualTo(0);
    assertThat(
            segmentTemplate.getAvailableSegmentCount(
                /* periodDurationUs= */ C.TIME_UNSET,
                /* nowUnixTimeUs= */ periodStartUnixTimeUs + 1_499_999))
        .isEqualTo(0);
    assertThat(
            segmentTemplate.getAvailableSegmentCount(
                /* periodDurationUs= */ C.TIME_UNSET,
                /* nowUnixTimeUs= */ periodStartUnixTimeUs + 1_500_000))
        .isEqualTo(1);
    assertThat(
            segmentTemplate.getAvailableSegmentCount(
                /* periodDurationUs= */ C.TIME_UNSET,
                /* nowUnixTimeUs= */ periodStartUnixTimeUs + 7_499_999))
        .isEqualTo(3);
    assertThat(
            segmentTemplate.getAvailableSegmentCount(
                /* periodDurationUs= */ C.TIME_UNSET,
                /* nowUnixTimeUs= */ periodStartUnixTimeUs + 7_500_000))
        .isEqualTo(4);
    assertThat(
            segmentTemplate.getAvailableSegmentCount(
                /* periodDurationUs= */ C.TIME_UNSET,
                /* nowUnixTimeUs= */ periodStartUnixTimeUs + 7_999_999))
        .isEqualTo(4);
    assertThat(
            segmentTemplate.getAvailableSegmentCount(
                /* periodDurationUs= */ C.TIME_UNSET,
                /* nowUnixTimeUs= */ periodStartUnixTimeUs + 8_000_000))
        .isEqualTo(3);
  }

  @Test
  public void getNextSegmentShiftTimeUse_unboundedSegmentTemplate() {
    long periodStartUnixTimeUs = 123_000_000_000_000L;
    SegmentBase.SegmentTemplate segmentTemplate =
        new SegmentBase.SegmentTemplate(
            /* initialization= */ null,
            /* timescale= */ 1000,
            /* presentationTimeOffset= */ 0,
            /* startNumber= */ 42,
            /* endNumber= */ C.INDEX_UNSET,
            /* duration= */ 2000,
            /* segmentTimeline= */ null,
            /* availabilityTimeOffsetUs= */ 500_000,
            /* initializationTemplate= */ null,
            /* mediaTemplate= */ null,
            /* timeShiftBufferDepthUs= */ 6_000_000,
            /* periodStartUnixTimeUs= */ periodStartUnixTimeUs);

    assertThat(
            segmentTemplate.getNextSegmentAvailableTimeUs(
                /* periodDurationUs= */ C.TIME_UNSET,
                /* nowUnixTimeUs= */ periodStartUnixTimeUs - 10_000_000))
        .isEqualTo(1_500_000);
    assertThat(
            segmentTemplate.getNextSegmentAvailableTimeUs(
                /* periodDurationUs= */ C.TIME_UNSET, /* nowUnixTimeUs= */ periodStartUnixTimeUs))
        .isEqualTo(1_500_000);
    assertThat(
            segmentTemplate.getNextSegmentAvailableTimeUs(
                /* periodDurationUs= */ C.TIME_UNSET,
                /* nowUnixTimeUs= */ periodStartUnixTimeUs + 1_499_999))
        .isEqualTo(1_500_000);
    assertThat(
            segmentTemplate.getNextSegmentAvailableTimeUs(
                /* periodDurationUs= */ C.TIME_UNSET,
                /* nowUnixTimeUs= */ periodStartUnixTimeUs + 1_500_000))
        .isEqualTo(3_500_000);
    assertThat(
            segmentTemplate.getNextSegmentAvailableTimeUs(
                /* periodDurationUs= */ C.TIME_UNSET,
                /* nowUnixTimeUs= */ periodStartUnixTimeUs + 17_499_999))
        .isEqualTo(17_500_000);
    assertThat(
            segmentTemplate.getNextSegmentAvailableTimeUs(
                /* periodDurationUs= */ C.TIME_UNSET,
                /* nowUnixTimeUs= */ periodStartUnixTimeUs + 17_500_000))
        .isEqualTo(19_500_000);
  }

  /** Regression test for https://github.com/google/ExoPlayer/issues/8804. */
  @Test
  public void getSegmentCount_withSegmentTemplate_avoidsIncorrectRounding() {
    SegmentBase.SegmentTemplate segmentTemplate =
        new SegmentBase.SegmentTemplate(
            /* initialization= */ null,
            /* timescale= */ 90000,
            /* presentationTimeOffset= */ 0,
            /* startNumber= */ 0,
            /* endNumber= */ C.INDEX_UNSET,
            /* duration= */ 179989,
            /* segmentTimeline= */ null,
            /* availabilityTimeOffsetUs= */ C.TIME_UNSET,
            /* initializationTemplate= */ null,
            /* mediaTemplate= */ null,
            /* timeShiftBufferDepthUs= */ C.TIME_UNSET,
            /* periodStartUnixTimeUs= */ C.TIME_UNSET);
    assertThat(segmentTemplate.getSegmentCount(2931820000L)).isEqualTo(1466);
  }

  @Test
  public void getSegmentCount_withSegmentTemplate_avoidsOverflow() {
    SegmentBase.SegmentTemplate segmentTemplate =
        new SegmentBase.SegmentTemplate(
            /* initialization= */ null,
            /* timescale= */ 1000000,
            /* presentationTimeOffset= */ 0,
            /* startNumber= */ 0,
            /* endNumber= */ C.INDEX_UNSET,
            /* duration= */ 179989,
            /* segmentTimeline= */ null,
            /* availabilityTimeOffsetUs= */ C.TIME_UNSET,
            /* initializationTemplate= */ null,
            /* mediaTemplate= */ null,
            /* timeShiftBufferDepthUs= */ C.TIME_UNSET,
            /* periodStartUnixTimeUs= */ C.TIME_UNSET);
    assertThat(segmentTemplate.getSegmentCount(1618875028000000L)).isEqualTo(8994299808L);
  }
}
