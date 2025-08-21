/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package androidx.media3.exoplayer.rtsp;

import static java.lang.annotation.ElementType.TYPE_USE;

import android.net.Uri;
import androidx.annotation.IntDef;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Represents an RTSP request. */
/* package */ final class RtspRequest {
  /**
   * RTSP request methods, as defined in RFC2326 Section 10.
   *
   * <p>The possible values are:
   *
   * <ul>
   *   <li>{@link #METHOD_UNSET}
   *   <li>{@link #METHOD_ANNOUNCE}
   *   <li>{@link #METHOD_DESCRIBE}
   *   <li>{@link #METHOD_GET_PARAMETER}
   *   <li>{@link #METHOD_OPTIONS}
   *   <li>{@link #METHOD_PAUSE}
   *   <li>{@link #METHOD_PLAY}
   *   <li>{@link #METHOD_PLAY_NOTIFY}
   *   <li>{@link #METHOD_RECORD}
   *   <li>{@link #METHOD_REDIRECT}
   *   <li>{@link #METHOD_SETUP}
   *   <li>{@link #METHOD_SET_PARAMETER}
   *   <li>{@link #METHOD_TEARDOWN}
   * </ul>
   */
  @Documented
  @Retention(RetentionPolicy.SOURCE)
  @Target(TYPE_USE)
  @IntDef(
      value = {
        METHOD_UNSET,
        METHOD_ANNOUNCE,
        METHOD_DESCRIBE,
        METHOD_GET_PARAMETER,
        METHOD_OPTIONS,
        METHOD_PAUSE,
        METHOD_PLAY,
        METHOD_PLAY_NOTIFY,
        METHOD_RECORD,
        METHOD_REDIRECT,
        METHOD_SETUP,
        METHOD_SET_PARAMETER,
        METHOD_TEARDOWN
      })
  public @interface Method {}

  public static final int METHOD_UNSET = 0;
  public static final int METHOD_ANNOUNCE = 1;
  public static final int METHOD_DESCRIBE = 2;
  public static final int METHOD_GET_PARAMETER = 3;
  public static final int METHOD_OPTIONS = 4;
  public static final int METHOD_PAUSE = 5;
  public static final int METHOD_PLAY = 6;
  public static final int METHOD_PLAY_NOTIFY = 7;
  public static final int METHOD_RECORD = 8;
  public static final int METHOD_REDIRECT = 9;
  public static final int METHOD_SETUP = 10;
  public static final int METHOD_SET_PARAMETER = 11;
  public static final int METHOD_TEARDOWN = 12;

  /** The {@link Uri} to which this request is sent. */
  public final Uri uri;

  /** The request method, as defined in {@link Method}. */
  public final @Method int method;

  /** The headers of this request. */
  public final RtspHeaders headers;

  /** The body of this RTSP message, or empty string if absent. */
  public final String messageBody;

  /**
   * Creates a new instance.
   *
   * @param uri The {@link Uri} to which this request is sent.
   * @param method The request method, as defined in {@link Method}.
   * @param headers The headers of this request.
   * @param messageBody The body of this RTSP message, or empty string if absent.
   */
  public RtspRequest(Uri uri, @Method int method, RtspHeaders headers, String messageBody) {
    this.uri = uri;
    this.method = method;
    this.headers = headers;
    this.messageBody = messageBody;
  }
}
