/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.rtsp;

/** Represents an RTSP DESCRIBE response. */
/* package */ final class RtspDescribeResponse {
  /** The response's headers. */
  public final RtspHeaders headers;

  /** The response's status code. */
  public final int status;

  /** The {@link SessionDescription} (see RFC2327) in the DESCRIBE response. */
  public final SessionDescription sessionDescription;

  /**
   * Creates a new instance.
   *
   * @param headers The response's headers.
   * @param status The response's status code.
   * @param sessionDescription The {@link SessionDescription} in the DESCRIBE response.
   */
  public RtspDescribeResponse(
      RtspHeaders headers, int status, SessionDescription sessionDescription) {
    this.headers = headers;
    this.status = status;
    this.sessionDescription = sessionDescription;
  }
}
