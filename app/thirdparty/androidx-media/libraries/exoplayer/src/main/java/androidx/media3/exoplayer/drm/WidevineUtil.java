/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.drm;

import android.util.Pair;
import androidx.annotation.Nullable;
import androidx.media3.common.C;
import androidx.media3.common.util.UnstableApi;
import java.util.Map;

/** Utility methods for Widevine. */
@UnstableApi
public final class WidevineUtil {

  /** Widevine specific key status field name for the remaining license duration, in seconds. */
  public static final String PROPERTY_LICENSE_DURATION_REMAINING = "LicenseDurationRemaining";

  /** Widevine specific key status field name for the remaining playback duration, in seconds. */
  public static final String PROPERTY_PLAYBACK_DURATION_REMAINING = "PlaybackDurationRemaining";

  private WidevineUtil() {}

  /**
   * Returns license and playback durations remaining in seconds.
   *
   * @param drmSession The drm session to query.
   * @return A {@link Pair} consisting of the remaining license and playback durations in seconds,
   *     or null if called before the session has been opened or after it's been released.
   */
  @Nullable
  public static Pair<Long, Long> getLicenseDurationRemainingSec(DrmSession drmSession) {
    Map<String, String> keyStatus = drmSession.queryKeyStatus();
    if (keyStatus == null) {
      return null;
    }
    return new Pair<>(
        getDurationRemainingSec(keyStatus, PROPERTY_LICENSE_DURATION_REMAINING),
        getDurationRemainingSec(keyStatus, PROPERTY_PLAYBACK_DURATION_REMAINING));
  }

  private static long getDurationRemainingSec(Map<String, String> keyStatus, String property) {
    if (keyStatus != null) {
      try {
        String value = keyStatus.get(property);
        if (value != null) {
          return Long.parseLong(value);
        }
      } catch (NumberFormatException e) {
        // do nothing.
      }
    }
    return C.TIME_UNSET;
  }
}
