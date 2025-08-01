/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.source.ads;

import androidx.annotation.VisibleForTesting;
import androidx.media3.common.AdPlaybackState;
import androidx.media3.common.C;
import androidx.media3.common.Timeline;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.source.ForwardingTimeline;

/** A {@link Timeline} for sources that have ads. */
@VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
@UnstableApi
public final class SinglePeriodAdTimeline extends ForwardingTimeline {

  private final AdPlaybackState adPlaybackState;

  /**
   * Creates a new timeline with a single period containing ads.
   *
   * @param contentTimeline The timeline of the content alongside which ads will be played. It must
   *     have one window and one period.
   * @param adPlaybackState The state of the period's ads.
   */
  public SinglePeriodAdTimeline(Timeline contentTimeline, AdPlaybackState adPlaybackState) {
    super(contentTimeline);
    Assertions.checkState(contentTimeline.getPeriodCount() == 1);
    Assertions.checkState(contentTimeline.getWindowCount() == 1);
    this.adPlaybackState = adPlaybackState;
  }

  @Override
  public Period getPeriod(int periodIndex, Period period, boolean setIds) {
    timeline.getPeriod(periodIndex, period, setIds);
    long durationUs =
        period.durationUs == C.TIME_UNSET ? adPlaybackState.contentDurationUs : period.durationUs;
    period.set(
        period.id,
        period.uid,
        period.windowIndex,
        durationUs,
        period.getPositionInWindowUs(),
        adPlaybackState,
        period.isPlaceholder);
    return period;
  }
}
