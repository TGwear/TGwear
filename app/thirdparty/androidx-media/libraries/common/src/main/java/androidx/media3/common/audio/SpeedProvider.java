/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common.audio;

import androidx.media3.common.C;
import androidx.media3.common.util.UnstableApi;

/** A custom interface that determines the speed for media at specific timestamps. */
@UnstableApi
public interface SpeedProvider {

  /**
   * Returns the media speed from the provided timestamp.
   *
   * <p>The media speed will stay the same until {@linkplain #getNextSpeedChangeTimeUs the next
   * specified speed change}.
   *
   * @param timeUs The timestamp of the media.
   * @return The speed that the media should be played at.
   */
  float getSpeed(long timeUs);

  /**
   * Returns the timestamp of the next speed change, if there is any.
   *
   * @param timeUs A timestamp, in microseconds.
   * @return The timestamp of the next speed change, in microseconds, or {@link C#TIME_UNSET} if
   *     there is no next speed change. If {@code timeUs} corresponds to a speed change, the
   *     returned value corresponds to the following speed change.
   */
  long getNextSpeedChangeTimeUs(long timeUs);
}
