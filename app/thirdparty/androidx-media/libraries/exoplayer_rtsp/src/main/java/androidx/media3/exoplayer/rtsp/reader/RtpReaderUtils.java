/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.rtsp.reader;

import androidx.media3.common.C;
import androidx.media3.common.util.Util;

/** Utility methods for {@link RtpPayloadReader}s. */
/* package */ class RtpReaderUtils {

  /**
   * Converts RTP timestamp and media frequency to sample presentation time, in microseconds
   *
   * @param startTimeOffsetUs The offset of the RTP timebase, in microseconds.
   * @param rtpTimestamp The RTP timestamp to convert.
   * @param firstReceivedRtpTimestamp The first received RTP timestamp.
   * @param mediaFrequency The media frequency.
   * @return The calculated sample presentation time, in microseconds.
   */
  public static long toSampleTimeUs(
      long startTimeOffsetUs,
      long rtpTimestamp,
      long firstReceivedRtpTimestamp,
      int mediaFrequency) {
    return startTimeOffsetUs
        + Util.scaleLargeTimestamp(
            rtpTimestamp - firstReceivedRtpTimestamp,
            /* multiplier= */ C.MICROS_PER_SECOND,
            /* divisor= */ mediaFrequency);
  }

  private RtpReaderUtils() {}
}
