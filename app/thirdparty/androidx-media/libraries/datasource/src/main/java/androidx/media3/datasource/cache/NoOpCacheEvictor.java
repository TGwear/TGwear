/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.datasource.cache;

import androidx.media3.common.util.UnstableApi;

/**
 * Evictor that doesn't ever evict cache files.
 *
 * <p>Warning: Using this evictor might have unforeseeable consequences if cache size is not managed
 * elsewhere.
 */
@UnstableApi
public final class NoOpCacheEvictor implements CacheEvictor {

  @Override
  public boolean requiresCacheSpanTouches() {
    return false;
  }

  @Override
  public void onCacheInitialized() {
    // Do nothing.
  }

  @Override
  public void onStartFile(Cache cache, String key, long position, long length) {
    // Do nothing.
  }

  @Override
  public void onSpanAdded(Cache cache, CacheSpan span) {
    // Do nothing.
  }

  @Override
  public void onSpanRemoved(Cache cache, CacheSpan span) {
    // Do nothing.
  }

  @Override
  public void onSpanTouched(Cache cache, CacheSpan oldSpan, CacheSpan newSpan) {
    // Do nothing.
  }
}
