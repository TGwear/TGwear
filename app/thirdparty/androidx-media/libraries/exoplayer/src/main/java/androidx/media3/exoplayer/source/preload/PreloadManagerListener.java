/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.source.preload;

import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;

/**
 * Listener for events in a preload manager.
 *
 * <p>All methods have no-op default implementations to allow selective overrides.
 */
@UnstableApi
public interface PreloadManagerListener {

  /** Called when the given {@link MediaItem} has completed preloading. */
  default void onCompleted(MediaItem mediaItem) {}

  /** Called when an {@linkplain PreloadException error} occurs. */
  default void onError(PreloadException exception) {}
}
