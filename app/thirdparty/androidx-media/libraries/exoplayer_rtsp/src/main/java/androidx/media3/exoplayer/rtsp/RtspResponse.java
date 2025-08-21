/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package androidx.media3.exoplayer.rtsp;

/** Represents an RTSP Response. */
/* package */ final class RtspResponse {

  /** The status code of this response, as defined in RFC 2326 section 11. */
  public final int status;

  /** The headers of this response. */
  public final RtspHeaders headers;

  /** The body of this RTSP message, or empty string if absent. */
  public final String messageBody;

  /**
   * Creates a new instance.
   *
   * @param status The status code of this response, as defined in RFC 2326 section 11.
   * @param headers The headers of this response.
   * @param messageBody The body of this RTSP message, or empty string if absent.
   */
  public RtspResponse(int status, RtspHeaders headers, String messageBody) {
    this.status = status;
    this.headers = headers;
    this.messageBody = messageBody;
  }

  /**
   * Creates a new instance with an empty {@link #messageBody}.
   *
   * @param status The status code of this response, as defined in RFC 2326 section 11.
   * @param headers The headers of this response.
   */
  public RtspResponse(int status, RtspHeaders headers) {
    this(status, headers, /* messageBody= */ "");
  }
}
