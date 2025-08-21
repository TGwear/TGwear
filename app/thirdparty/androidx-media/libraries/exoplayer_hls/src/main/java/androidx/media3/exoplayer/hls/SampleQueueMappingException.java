/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.hls;

import androidx.annotation.Nullable;
import androidx.media3.common.TrackGroup;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.source.SampleQueue;
import java.io.IOException;

/** Thrown when it is not possible to map a {@link TrackGroup} to a {@link SampleQueue}. */
@UnstableApi
public final class SampleQueueMappingException extends IOException {

  /**
   * @param mimeType The MIME type of the track group whose mapping failed.
   */
  public SampleQueueMappingException(@Nullable String mimeType) {
    super("Unable to bind a sample queue to TrackGroup with MIME type " + mimeType + ".");
  }
}
