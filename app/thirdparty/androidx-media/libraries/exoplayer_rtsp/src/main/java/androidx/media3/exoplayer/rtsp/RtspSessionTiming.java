/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.rtsp;

import static androidx.media3.common.util.Util.castNonNull;
import static androidx.media3.exoplayer.rtsp.RtspMessageUtil.checkManifestExpression;

import androidx.annotation.Nullable;
import androidx.media3.common.C;
import androidx.media3.common.ParserException;
import androidx.media3.common.util.Util;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represent the timing (RTSP Normal Playback Time format) of an RTSP session.
 *
 * <p>Currently only NPT is supported. See RFC2326 Section 3.6 for detail of NPT.
 */
/* package */ final class RtspSessionTiming {
  /** The default session timing starting from 0.000 and indefinite length, effectively live. */
  public static final RtspSessionTiming DEFAULT =
      new RtspSessionTiming(/* startTimeMs= */ 0, /* stopTimeMs= */ C.TIME_UNSET);

  // We only support npt=xxx-[xxx], but not npt=-xxx. See RFC2326 Section 3.6.
  // Supports both npt= and npt: identifier.
  private static final Pattern NPT_RANGE_PATTERN =
      Pattern.compile("npt[:=]([.\\d]+|now)\\s?-\\s?([.\\d]+)?");
  private static final String START_TIMING_NTP_FORMAT = "npt=%.3f-";

  private static final long LIVE_START_TIME = 0;

  /** Parses an SDP range attribute (RFC2326 Section 3.6). */
  public static RtspSessionTiming parseTiming(String sdpRangeAttribute) throws ParserException {
    long startTimeMs;
    long stopTimeMs;
    Matcher matcher = NPT_RANGE_PATTERN.matcher(sdpRangeAttribute);
    checkManifestExpression(matcher.matches(), /* message= */ sdpRangeAttribute);

    @Nullable String startTimeString = matcher.group(1);
    checkManifestExpression(startTimeString != null, /* message= */ sdpRangeAttribute);
    if (castNonNull(startTimeString).equals("now")) {
      startTimeMs = LIVE_START_TIME;
    } else {
      startTimeMs = (long) (Float.parseFloat(startTimeString) * C.MILLIS_PER_SECOND);
    }

    @Nullable String stopTimeString = matcher.group(2);
    if (stopTimeString != null) {
      try {
        stopTimeMs = (long) (Float.parseFloat(stopTimeString) * C.MILLIS_PER_SECOND);
      } catch (NumberFormatException e) {
        throw ParserException.createForMalformedManifest(stopTimeString, e);
      }
      checkManifestExpression(stopTimeMs >= startTimeMs, /* message= */ sdpRangeAttribute);
    } else {
      stopTimeMs = C.TIME_UNSET;
    }

    return new RtspSessionTiming(startTimeMs, stopTimeMs);
  }

  /** Gets a Range RTSP header for an RTSP PLAY request. */
  public static String getOffsetStartTimeTiming(long offsetStartTimeMs) {
    double offsetStartTimeSec = (double) offsetStartTimeMs / C.MILLIS_PER_SECOND;
    return Util.formatInvariant(START_TIMING_NTP_FORMAT, offsetStartTimeSec);
  }

  /**
   * The start time of this session, in milliseconds. When playing a live session, the start time is
   * always zero.
   */
  public final long startTimeMs;

  /**
   * The stop time of the session, in milliseconds, or {@link C#TIME_UNSET} when the stop time is
   * not set, for example when playing a live session.
   */
  public final long stopTimeMs;

  private RtspSessionTiming(long startTimeMs, long stopTimeMs) {
    this.startTimeMs = startTimeMs;
    this.stopTimeMs = stopTimeMs;
  }

  /** Tests whether the timing is live. */
  public boolean isLive() {
    return stopTimeMs == C.TIME_UNSET;
  }

  /** Gets the session duration in milliseconds. */
  public long getDurationMs() {
    return stopTimeMs - startTimeMs;
  }
}
