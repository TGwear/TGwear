/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer;

import androidx.media3.common.PlaybackParameters;
import androidx.media3.common.util.UnstableApi;

/** Tracks the progression of media time. */
@UnstableApi
public interface MediaClock {

  /** Returns the current media position in microseconds. */
  long getPositionUs();

  /** Returns whether there is a skipped silence since the last call to this method. */
  default boolean hasSkippedSilenceSinceLastCall() {
    return false;
  }

  /**
   * Attempts to set the playback parameters. The media clock may override the speed if changing the
   * playback parameters is not supported.
   *
   * @param playbackParameters The playback parameters to attempt to set.
   */
  void setPlaybackParameters(PlaybackParameters playbackParameters);

  /** Returns the active playback parameters. */
  PlaybackParameters getPlaybackParameters();
}
