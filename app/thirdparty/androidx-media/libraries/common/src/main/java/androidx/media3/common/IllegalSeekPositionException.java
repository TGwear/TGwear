/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common;

import androidx.media3.common.util.UnstableApi;

/**
 * Thrown when an attempt is made to seek to a position that does not exist in the player's {@link
 * Timeline}.
 */
@UnstableApi
public final class IllegalSeekPositionException extends IllegalStateException {

  /** The {@link Timeline} in which the seek was attempted. */
  public final Timeline timeline;

  /** The index of the window being seeked to. */
  public final int windowIndex;

  /** The seek position in the specified window. */
  public final long positionMs;

  /**
   * @param timeline The {@link Timeline} in which the seek was attempted.
   * @param windowIndex The index of the window being seeked to.
   * @param positionMs The seek position in the specified window.
   */
  public IllegalSeekPositionException(Timeline timeline, int windowIndex, long positionMs) {
    this.timeline = timeline;
    this.windowIndex = windowIndex;
    this.positionMs = positionMs;
  }
}
