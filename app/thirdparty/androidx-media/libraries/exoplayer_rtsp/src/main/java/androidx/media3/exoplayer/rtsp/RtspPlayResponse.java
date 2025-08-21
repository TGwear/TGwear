/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.rtsp;

import com.google.common.collect.ImmutableList;
import java.util.List;

/** Represents an RTSP PLAY response. */
/* package */ final class RtspPlayResponse {
  /** The response's status code. */
  public final int status;

  /** The playback start timing, {@link RtspSessionTiming#DEFAULT} if not present. */
  public final RtspSessionTiming sessionTiming;

  /** The list of {@link RtspTrackTiming} representing the {@link RtspHeaders#RTP_INFO} header. */
  public final ImmutableList<RtspTrackTiming> trackTimingList;

  /**
   * Creates a new instance.
   *
   * @param status The response's status code.
   * @param sessionTiming The {@link RtspSessionTiming}, pass {@link RtspSessionTiming#DEFAULT} if
   *     not present.
   * @param trackTimingList The list of {@link RtspTrackTiming} representing the {@link
   *     RtspHeaders#RTP_INFO} header.
   */
  public RtspPlayResponse(
      int status, RtspSessionTiming sessionTiming, List<RtspTrackTiming> trackTimingList) {
    this.status = status;
    this.sessionTiming = sessionTiming;
    this.trackTimingList = ImmutableList.copyOf(trackTimingList);
  }
}
