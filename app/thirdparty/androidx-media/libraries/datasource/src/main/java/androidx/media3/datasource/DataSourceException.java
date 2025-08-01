/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.datasource;

import androidx.annotation.Nullable;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.util.UnstableApi;
import java.io.IOException;

/** Used to specify reason of a DataSource error. */
public class DataSourceException extends IOException {

  /**
   * Returns whether the given {@link IOException} was caused by a {@link DataSourceException} whose
   * {@link #reason} is {@link PlaybackException#ERROR_CODE_IO_READ_POSITION_OUT_OF_RANGE} in its
   * cause stack.
   */
  @UnstableApi
  public static boolean isCausedByPositionOutOfRange(IOException e) {
    @Nullable Throwable cause = e;
    while (cause != null) {
      if (cause instanceof DataSourceException) {
        int reason = ((DataSourceException) cause).reason;
        if (reason == PlaybackException.ERROR_CODE_IO_READ_POSITION_OUT_OF_RANGE) {
          return true;
        }
      }
      cause = cause.getCause();
    }
    return false;
  }

  /**
   * Indicates that the {@link DataSpec#position starting position} of the request was outside the
   * bounds of the data.
   *
   * @deprecated Use {@link PlaybackException#ERROR_CODE_IO_READ_POSITION_OUT_OF_RANGE}.
   */
  @UnstableApi @Deprecated
  public static final int POSITION_OUT_OF_RANGE =
      PlaybackException.ERROR_CODE_IO_READ_POSITION_OUT_OF_RANGE;

  /**
   * The reason of this {@link DataSourceException}, should be one of the {@code ERROR_CODE_IO_*} in
   * {@link PlaybackException.ErrorCode}.
   */
  public final @PlaybackException.ErrorCode int reason;

  /**
   * Constructs a DataSourceException.
   *
   * @param reason Reason of the error, should be one of the {@code ERROR_CODE_IO_*} in {@link
   *     PlaybackException.ErrorCode}.
   */
  @UnstableApi
  public DataSourceException(@PlaybackException.ErrorCode int reason) {
    this.reason = reason;
  }

  /**
   * Constructs a DataSourceException.
   *
   * @param cause The error cause.
   * @param reason Reason of the error, should be one of the {@code ERROR_CODE_IO_*} in {@link
   *     PlaybackException.ErrorCode}.
   */
  @UnstableApi
  public DataSourceException(@Nullable Throwable cause, @PlaybackException.ErrorCode int reason) {
    super(cause);
    this.reason = reason;
  }

  /**
   * Constructs a DataSourceException.
   *
   * @param message The error message.
   * @param reason Reason of the error, should be one of the {@code ERROR_CODE_IO_*} in {@link
   *     PlaybackException.ErrorCode}.
   */
  @UnstableApi
  public DataSourceException(@Nullable String message, @PlaybackException.ErrorCode int reason) {
    super(message);
    this.reason = reason;
  }

  /**
   * Constructs a DataSourceException.
   *
   * @param message The error message.
   * @param cause The error cause.
   * @param reason Reason of the error, should be one of the {@code ERROR_CODE_IO_*} in {@link
   *     PlaybackException.ErrorCode}.
   */
  @UnstableApi
  public DataSourceException(
      @Nullable String message,
      @Nullable Throwable cause,
      @PlaybackException.ErrorCode int reason) {
    super(message, cause);
    this.reason = reason;
  }
}
