/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.source.preload;

import androidx.annotation.Nullable;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;
import java.util.Objects;

/** Thrown when a non-recoverable preload failure occurs. */
@UnstableApi
public final class PreloadException extends Exception {

  /** The {@link MediaItem} that this instance is associated with. */
  public final MediaItem mediaItem;

  /**
   * Creates an instance.
   *
   * @param mediaItem The {@link MediaItem} that this instance is associated with.
   * @param message See {@link #getMessage()}.
   * @param cause See {@link #getCause()}.
   */
  public PreloadException(
      MediaItem mediaItem, @Nullable String message, @Nullable Throwable cause) {
    super(message, cause);
    this.mediaItem = mediaItem;
  }

  /**
   * Returns whether the error data associated to this exception equals the error data associated to
   * {@code other}.
   *
   * <p>Note that this method does not compare the exceptions' stack traces.
   */
  public boolean errorInfoEquals(@Nullable PreloadException other) {
    if (this == other) {
      return true;
    }
    if (other == null || getClass() != other.getClass()) {
      return false;
    }

    @Nullable Throwable thisCause = getCause();
    @Nullable Throwable thatCause = other.getCause();
    if (thisCause != null && thatCause != null) {
      if (!Objects.equals(thisCause.getMessage(), thatCause.getMessage())) {
        return false;
      }
      if (!Objects.equals(thisCause.getClass(), thatCause.getClass())) {
        return false;
      }
    } else if (thisCause != null || thatCause != null) {
      return false;
    }
    return Objects.equals(mediaItem, other.mediaItem)
        && Objects.equals(getMessage(), other.getMessage());
  }
}
