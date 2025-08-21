/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer;

import androidx.media3.common.PlaybackParameters;
import androidx.media3.common.util.Clock;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.common.util.Util;

/**
 * A {@link MediaClock} whose position advances with real time based on the playback parameters when
 * started.
 */
@UnstableApi
public final class StandaloneMediaClock implements MediaClock {

  private final Clock clock;

  private boolean started;
  private long baseUs;
  private long baseElapsedMs;
  private PlaybackParameters playbackParameters;

  /**
   * Creates a new standalone media clock using the given {@link Clock} implementation.
   *
   * @param clock A {@link Clock}.
   */
  public StandaloneMediaClock(Clock clock) {
    this.clock = clock;
    playbackParameters = PlaybackParameters.DEFAULT;
  }

  /** Starts the clock. Does nothing if the clock is already started. */
  public void start() {
    if (!started) {
      baseElapsedMs = clock.elapsedRealtime();
      started = true;
    }
  }

  /** Stops the clock. Does nothing if the clock is already stopped. */
  public void stop() {
    if (started) {
      resetPosition(getPositionUs());
      started = false;
    }
  }

  /**
   * Resets the clock's position.
   *
   * @param positionUs The position to set in microseconds.
   */
  public void resetPosition(long positionUs) {
    baseUs = positionUs;
    if (started) {
      baseElapsedMs = clock.elapsedRealtime();
    }
  }

  @Override
  public long getPositionUs() {
    long positionUs = baseUs;
    if (started) {
      long elapsedSinceBaseMs = clock.elapsedRealtime() - baseElapsedMs;
      if (playbackParameters.speed == 1f) {
        positionUs += Util.msToUs(elapsedSinceBaseMs);
      } else {
        // Add the media time in microseconds that will elapse in elapsedSinceBaseMs milliseconds of
        // wallclock time
        positionUs += playbackParameters.getMediaTimeUsForPlayoutTimeMs(elapsedSinceBaseMs);
      }
    }
    return positionUs;
  }

  @Override
  public void setPlaybackParameters(PlaybackParameters playbackParameters) {
    // Store the current position as the new base, in case the playback speed has changed.
    if (started) {
      resetPosition(getPositionUs());
    }
    this.playbackParameters = playbackParameters;
  }

  @Override
  public PlaybackParameters getPlaybackParameters() {
    return playbackParameters;
  }
}
