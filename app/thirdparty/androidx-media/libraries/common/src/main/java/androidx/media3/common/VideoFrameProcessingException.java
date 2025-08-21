/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common;

import androidx.media3.common.util.UnstableApi;

/**
 * Thrown when an exception occurs while preparing an {@link Effect}, or applying an {@link Effect}
 * to video frames.
 */
@UnstableApi
public final class VideoFrameProcessingException extends Exception {

  /**
   * Wraps the given exception in a {@code VideoFrameProcessingException} if it is not already a
   * {@code VideoFrameProcessingException} and returns the exception otherwise.
   */
  public static VideoFrameProcessingException from(Exception exception) {
    return from(exception, /* presentationTimeUs= */ C.TIME_UNSET);
  }

  /**
   * Wraps the given exception in a {@code VideoFrameProcessingException} with the given timestamp
   * if it is not already a {@code VideoFrameProcessingException} and returns the exception
   * otherwise.
   */
  public static VideoFrameProcessingException from(Exception exception, long presentationTimeUs) {
    if (exception instanceof VideoFrameProcessingException) {
      return (VideoFrameProcessingException) exception;
    } else {
      return new VideoFrameProcessingException(exception, presentationTimeUs);
    }
  }

  /**
   * The microsecond timestamp of the frame being processed while the exception occurred or {@link
   * C#TIME_UNSET} if unknown.
   */
  public final long presentationTimeUs;

  /**
   * Creates an instance.
   *
   * @param message The detail message for this exception.
   */
  public VideoFrameProcessingException(String message) {
    this(message, /* presentationTimeUs= */ C.TIME_UNSET);
  }

  /**
   * Creates an instance.
   *
   * @param message The detail message for this exception.
   * @param presentationTimeUs The timestamp of the frame for which the exception occurred.
   */
  public VideoFrameProcessingException(String message, long presentationTimeUs) {
    super(message);
    this.presentationTimeUs = presentationTimeUs;
  }

  /**
   * Creates an instance.
   *
   * @param message The detail message for this exception.
   * @param cause The cause of this exception.
   */
  public VideoFrameProcessingException(String message, Throwable cause) {
    this(message, cause, /* presentationTimeUs= */ C.TIME_UNSET);
  }

  /**
   * Creates an instance.
   *
   * @param message The detail message for this exception.
   * @param cause The cause of this exception.
   * @param presentationTimeUs The timestamp of the frame for which the exception occurred.
   */
  public VideoFrameProcessingException(String message, Throwable cause, long presentationTimeUs) {
    super(message, cause);
    this.presentationTimeUs = presentationTimeUs;
  }

  /**
   * Creates an instance.
   *
   * @param cause The cause of this exception.
   */
  public VideoFrameProcessingException(Throwable cause) {
    this(cause, /* presentationTimeUs= */ C.TIME_UNSET);
  }

  /**
   * Creates an instance.
   *
   * @param cause The cause of this exception.
   * @param presentationTimeUs The timestamp of the frame for which the exception occurred.
   */
  public VideoFrameProcessingException(Throwable cause, long presentationTimeUs) {
    super(cause);
    this.presentationTimeUs = presentationTimeUs;
  }
}
