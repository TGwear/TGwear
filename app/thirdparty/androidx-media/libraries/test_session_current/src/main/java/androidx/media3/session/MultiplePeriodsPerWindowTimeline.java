/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.session;

import static androidx.media3.common.util.Assertions.checkArgument;

import androidx.media3.common.C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.Util;
import java.util.List;

/** A {@link PlaylistTimeline} implementation that allows setting multiple periods per window. */
public class MultiplePeriodsPerWindowTimeline extends PlaylistTimeline {

  private final int[] periodSizesPerWindow;
  private final long defaultPeriodDurationMs;

  public MultiplePeriodsPerWindowTimeline(
      List<MediaItem> mediaItems, int[] periodSizesPerWindow, long defaultPeriodDurationMs) {
    super(mediaItems);
    checkArgument(
        mediaItems.size() == periodSizesPerWindow.length,
        "mediaItems size and periodSizesPerWindow length must be the same.");
    for (int i = 0; i < periodSizesPerWindow.length; i++) {
      checkArgument(
          periodSizesPerWindow[i] > 0,
          "periodSizesPerWindow should only contain positive integers.");
    }
    this.periodSizesPerWindow = periodSizesPerWindow;
    checkArgument(
        defaultPeriodDurationMs > 0, "defaultPeriodDurationMs should be a positive integer.");
    this.defaultPeriodDurationMs = defaultPeriodDurationMs;
  }

  @Override
  public Window getWindow(int windowIndex, Window window, long defaultPositionProjectionUs) {
    window = super.getWindow(windowIndex, window, defaultPositionProjectionUs);
    int firstPeriodIndex = getFirstPeriodIndex(windowIndex);
    window.set(
        /* uid= */ 0,
        window.mediaItem,
        /* manifest= */ null,
        /* presentationStartTimeMs= */ 0,
        /* windowStartTimeMs= */ 0,
        /* elapsedRealtimeEpochOffsetMs= */ 0,
        /* isSeekable= */ true,
        /* isDynamic= */ false,
        /* liveConfiguration= */ null,
        /* defaultPositionUs= */ 0,
        /* durationUs= */ Util.msToUs(defaultPeriodDurationMs * periodSizesPerWindow[windowIndex]),
        firstPeriodIndex,
        firstPeriodIndex + periodSizesPerWindow[windowIndex] - 1,
        /* positionInFirstPeriodUs= */ 0);
    return window;
  }

  @Override
  public int getPeriodCount() {
    int periodCounter = 0;
    for (int i = 0; i < periodSizesPerWindow.length; i++) {
      periodCounter += periodSizesPerWindow[i];
    }
    return periodCounter;
  }

  @Override
  public Period getPeriod(int periodIndex, Period period, boolean setIds) {
    checkArgument(periodIndex < getPeriodCount());
    int windowIndex = getWindowIndex(periodIndex);
    period.set(
        /* id= */ null,
        /* uid= */ null,
        windowIndex,
        /* durationUs= */ Util.msToUs(defaultPeriodDurationMs),
        /* positionInWindowUs= */ (periodIndex - getFirstPeriodIndex(windowIndex))
            * Util.msToUs(defaultPeriodDurationMs));
    return period;
  }

  private int getFirstPeriodIndex(int windowIndex) {
    int index = 0;
    for (int i = 0; i < windowIndex; i++) {
      index += periodSizesPerWindow[i];
    }
    return index;
  }

  private int getWindowIndex(int periodIndex) {
    int periodCounter = 0;
    for (int i = 0; i < periodSizesPerWindow.length; i++) {
      if (periodIndex >= periodCounter && periodIndex < periodCounter + periodSizesPerWindow[i]) {
        return i;
      }
      periodCounter += periodSizesPerWindow[i];
    }
    return C.INDEX_UNSET;
  }
}
