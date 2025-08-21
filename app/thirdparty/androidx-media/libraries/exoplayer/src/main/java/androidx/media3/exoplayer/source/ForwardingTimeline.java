/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.source;

import androidx.annotation.Nullable;
import androidx.media3.common.Player;
import androidx.media3.common.Timeline;
import androidx.media3.common.util.UnstableApi;

/**
 * An overridable {@link Timeline} implementation forwarding methods to another timeline.
 *
 * <p>The following methods call through to {@code super} instead of the {@code timeline} delegate:
 *
 * <ul>
 *   <li>{@link #getPeriodByUid(Object, Period)}
 *   <li>{@link #equals(Object)}
 *   <li>{@link #hashCode()}
 * </ul>
 */
@UnstableApi
public abstract class ForwardingTimeline extends Timeline {

  protected final Timeline timeline;

  public ForwardingTimeline(Timeline timeline) {
    this.timeline = timeline;
  }

  @Override
  public int getWindowCount() {
    return timeline.getWindowCount();
  }

  @Override
  public int getNextWindowIndex(
      int windowIndex, @Player.RepeatMode int repeatMode, boolean shuffleModeEnabled) {
    return timeline.getNextWindowIndex(windowIndex, repeatMode, shuffleModeEnabled);
  }

  @Override
  public int getPreviousWindowIndex(
      int windowIndex, @Player.RepeatMode int repeatMode, boolean shuffleModeEnabled) {
    return timeline.getPreviousWindowIndex(windowIndex, repeatMode, shuffleModeEnabled);
  }

  @Override
  public int getLastWindowIndex(boolean shuffleModeEnabled) {
    return timeline.getLastWindowIndex(shuffleModeEnabled);
  }

  @Override
  public int getFirstWindowIndex(boolean shuffleModeEnabled) {
    return timeline.getFirstWindowIndex(shuffleModeEnabled);
  }

  @Override
  public Window getWindow(int windowIndex, Window window, long defaultPositionProjectionUs) {
    return timeline.getWindow(windowIndex, window, defaultPositionProjectionUs);
  }

  @Override
  public int getPeriodCount() {
    return timeline.getPeriodCount();
  }

  @Override
  public final Period getPeriodByUid(Object periodUid, Period period) {
    return super.getPeriodByUid(periodUid, period);
  }

  @Override
  public Period getPeriod(int periodIndex, Period period, boolean setIds) {
    return timeline.getPeriod(periodIndex, period, setIds);
  }

  @Override
  public int getIndexOfPeriod(Object uid) {
    return timeline.getIndexOfPeriod(uid);
  }

  @Override
  public Object getUidOfPeriod(int periodIndex) {
    return timeline.getUidOfPeriod(periodIndex);
  }

  @Override
  public final boolean equals(@Nullable Object obj) {
    return super.equals(obj);
  }

  @Override
  public final int hashCode() {
    return super.hashCode();
  }
}
