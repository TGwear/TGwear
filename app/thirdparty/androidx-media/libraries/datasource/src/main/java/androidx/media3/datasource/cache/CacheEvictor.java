/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.datasource.cache;

import androidx.media3.common.C;
import androidx.media3.common.util.UnstableApi;

/**
 * Evicts data from a {@link Cache}. Implementations should call {@link Cache#removeSpan(CacheSpan)}
 * to evict cache entries based on their eviction policies.
 */
@UnstableApi
public interface CacheEvictor extends Cache.Listener {

  /**
   * Returns whether the evictor requires the {@link Cache} to touch {@link CacheSpan CacheSpans}
   * when it accesses them. Implementations that do not use {@link CacheSpan#lastTouchTimestamp}
   * should return {@code false}.
   */
  boolean requiresCacheSpanTouches();

  /** Called when cache has been initialized. */
  void onCacheInitialized();

  /**
   * Called when a writer starts writing to the cache.
   *
   * @param cache The source of the event.
   * @param key The key being written.
   * @param position The starting position of the data being written.
   * @param length The length of the data being written, or {@link C#LENGTH_UNSET} if unknown.
   */
  void onStartFile(Cache cache, String key, long position, long length);
}
