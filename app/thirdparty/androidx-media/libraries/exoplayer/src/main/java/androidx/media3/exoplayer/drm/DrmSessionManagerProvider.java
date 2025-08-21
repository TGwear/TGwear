/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.drm;

import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;

/**
 * A provider to obtain a {@link DrmSessionManager} suitable for playing the content described by a
 * {@link MediaItem}.
 */
@UnstableApi
public interface DrmSessionManagerProvider {

  /**
   * Returns a {@link DrmSessionManager} for the given media item.
   *
   * <p>The caller is responsible for {@link DrmSessionManager#prepare() preparing} the {@link
   * DrmSessionManager} before use, and subsequently {@link DrmSessionManager#release() releasing}
   * it.
   */
  DrmSessionManager get(MediaItem mediaItem);
}
