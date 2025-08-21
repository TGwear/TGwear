/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer;

import androidx.media3.common.C;
import androidx.media3.common.MediaItem.LiveConfiguration;
import androidx.media3.common.util.UnstableApi;

/**
 * Controls the playback speed while playing live content in order to maintain a steady target live
 * offset.
 */
@UnstableApi
public interface LivePlaybackSpeedControl {

  /**
   * Sets the live configuration defined by the media.
   *
   * @param liveConfiguration The {@link LiveConfiguration} as defined by the media.
   */
  void setLiveConfiguration(LiveConfiguration liveConfiguration);

  /**
   * Sets the target live offset in microseconds that overrides the live offset {@link
   * #setLiveConfiguration configured} by the media. Passing {@code C.TIME_UNSET} deletes a previous
   * override.
   *
   * <p>If no target live offset is configured by {@link #setLiveConfiguration}, this override has
   * no effect.
   */
  void setTargetLiveOffsetOverrideUs(long liveOffsetUs);

  /**
   * Notifies the live playback speed control that a rebuffer occurred.
   *
   * <p>A rebuffer is defined to be caused by buffer depletion rather than a user action. Hence this
   * method is not called during initial buffering or when buffering as a result of a seek
   * operation.
   */
  void notifyRebuffer();

  /**
   * Returns the adjusted playback speed in order get closer towards the {@link
   * #getTargetLiveOffsetUs() target live offset}.
   *
   * @param liveOffsetUs The current live offset, in microseconds.
   * @param bufferedDurationUs The duration of media that's currently buffered, in microseconds.
   * @return The adjusted factor by which playback should be sped up.
   */
  float getAdjustedPlaybackSpeed(long liveOffsetUs, long bufferedDurationUs);

  /**
   * Returns the current target live offset, in microseconds, or {@link C#TIME_UNSET} if no target
   * live offset is defined for the current media.
   */
  long getTargetLiveOffsetUs();
}
