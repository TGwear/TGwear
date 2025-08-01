/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.rtsp;

/** Represents an RTSP SETUP response. */
/* package */ final class RtspSetupResponse {

  /** The response's status code. */
  public final int status;

  /** The Session header (RFC2326 Section 12.37). */
  public final RtspMessageUtil.RtspSessionHeader sessionHeader;

  /** The Transport header (RFC2326 Section 12.39). */
  public final String transport;

  /**
   * Creates a new instance.
   *
   * @param status The response's status code.
   * @param sessionHeader The {@link RtspMessageUtil.RtspSessionHeader}.
   * @param transport The transport header included in the RTSP SETUP response.
   */
  public RtspSetupResponse(
      int status, RtspMessageUtil.RtspSessionHeader sessionHeader, String transport) {
    this.status = status;
    this.sessionHeader = sessionHeader;
    this.transport = transport;
  }
}
