/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.source;

import androidx.media3.common.MediaItem;
import androidx.media3.common.Timeline;
import androidx.media3.common.util.UnstableApi;

/** A {@link Timeline} that overrides the {@link MediaItem}. */
@UnstableApi
public final class TimelineWithUpdatedMediaItem extends ForwardingTimeline {

  private final MediaItem updatedMediaItem;

  /**
   * Creates the timeline.
   *
   * @param timeline The wrapped {@link Timeline}.
   * @param mediaItem The {@link MediaItem} that replaced the original one in {@code timeline}.
   */
  public TimelineWithUpdatedMediaItem(Timeline timeline, MediaItem mediaItem) {
    super(timeline);
    this.updatedMediaItem = mediaItem;
  }

  @SuppressWarnings("deprecation") // Setting deprecated field for backward compatibility.
  @Override
  public Window getWindow(int windowIndex, Window window, long defaultPositionProjectionUs) {
    super.getWindow(windowIndex, window, defaultPositionProjectionUs);
    window.mediaItem = updatedMediaItem;
    window.tag =
        updatedMediaItem.localConfiguration != null
            ? updatedMediaItem.localConfiguration.tag
            : null;
    return window;
  }
}
