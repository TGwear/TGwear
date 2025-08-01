/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.cast;

import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;
import com.google.android.gms.cast.MediaQueueItem;

/** Converts between {@link MediaItem} and the Cast SDK's {@link MediaQueueItem}. */
@UnstableApi
public interface MediaItemConverter {

  /**
   * Converts a {@link MediaItem} to a {@link MediaQueueItem}.
   *
   * @param mediaItem The {@link MediaItem}.
   * @return An equivalent {@link MediaQueueItem}.
   */
  MediaQueueItem toMediaQueueItem(MediaItem mediaItem);

  /**
   * Converts a {@link MediaQueueItem} to a {@link MediaItem}.
   *
   * @param mediaQueueItem The {@link MediaQueueItem}.
   * @return The equivalent {@link MediaItem}.
   */
  MediaItem toMediaItem(MediaQueueItem mediaQueueItem);
}
