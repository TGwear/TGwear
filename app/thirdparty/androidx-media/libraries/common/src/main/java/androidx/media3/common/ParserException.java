/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common;

import androidx.annotation.Nullable;
import androidx.media3.common.C.DataType;
import androidx.media3.common.util.UnstableApi;
import java.io.IOException;

/** Thrown when an error occurs parsing media data and metadata. */
@UnstableApi
public class ParserException extends IOException {

  /**
   * Creates a new instance for which {@link #contentIsMalformed} is true and {@link #dataType} is
   * {@link C#DATA_TYPE_UNKNOWN}.
   *
   * @param message See {@link #getMessage()}.
   * @param cause See {@link #getCause()}.
   * @return The created instance.
   */
  public static ParserException createForMalformedDataOfUnknownType(
      @Nullable String message, @Nullable Throwable cause) {
    return new ParserException(message, cause, /* contentIsMalformed= */ true, C.DATA_TYPE_UNKNOWN);
  }

  /**
   * Creates a new instance for which {@link #contentIsMalformed} is true and {@link #dataType} is
   * {@link C#DATA_TYPE_MEDIA}.
   *
   * @param message See {@link #getMessage()}.
   * @param cause See {@link #getCause()}.
   * @return The created instance.
   */
  public static ParserException createForMalformedContainer(
      @Nullable String message, @Nullable Throwable cause) {
    return new ParserException(message, cause, /* contentIsMalformed= */ true, C.DATA_TYPE_MEDIA);
  }

  /**
   * Creates a new instance for which {@link #contentIsMalformed} is true and {@link #dataType} is
   * {@link C#DATA_TYPE_MANIFEST}.
   *
   * @param message See {@link #getMessage()}.
   * @param cause See {@link #getCause()}.
   * @return The created instance.
   */
  public static ParserException createForMalformedManifest(
      @Nullable String message, @Nullable Throwable cause) {
    return new ParserException(
        message, cause, /* contentIsMalformed= */ true, C.DATA_TYPE_MANIFEST);
  }

  /**
   * Creates a new instance for which {@link #contentIsMalformed} is false and {@link #dataType} is
   * {@link C#DATA_TYPE_MANIFEST}.
   *
   * @param message See {@link #getMessage()}.
   * @param cause See {@link #getCause()}.
   * @return The created instance.
   */
  public static ParserException createForManifestWithUnsupportedFeature(
      @Nullable String message, @Nullable Throwable cause) {
    return new ParserException(
        message, cause, /* contentIsMalformed= */ false, C.DATA_TYPE_MANIFEST);
  }

  /**
   * Creates a new instance for which {@link #contentIsMalformed} is false and {@link #dataType} is
   * {@link C#DATA_TYPE_MEDIA}.
   *
   * @param message See {@link #getMessage()}.
   * @return The created instance.
   */
  public static ParserException createForUnsupportedContainerFeature(@Nullable String message) {
    return new ParserException(
        message, /* cause= */ null, /* contentIsMalformed= */ false, C.DATA_TYPE_MEDIA);
  }

  /**
   * Whether the parsing error was caused by a bitstream not following the expected format. May be
   * false when a parser encounters a legal condition which it does not support.
   */
  public final boolean contentIsMalformed;

  /** The {@link DataType data type} of the parsed bitstream. */
  public final int dataType;

  protected ParserException(
      @Nullable String message,
      @Nullable Throwable cause,
      boolean contentIsMalformed,
      @DataType int dataType) {
    super(message, cause);
    this.contentIsMalformed = contentIsMalformed;
    this.dataType = dataType;
  }

  @Override
  public String getMessage() {
    String superMessage = super.getMessage();
    return (superMessage != null ? superMessage + " " : "")
        + "{contentIsMalformed="
        + contentIsMalformed
        + ", dataType="
        + dataType
        + "}";
  }
}
