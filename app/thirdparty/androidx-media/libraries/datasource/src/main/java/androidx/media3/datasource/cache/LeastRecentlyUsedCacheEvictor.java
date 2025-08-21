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
import java.util.TreeSet;

/** Evicts least recently used cache files first. */
@UnstableApi
public final class LeastRecentlyUsedCacheEvictor implements CacheEvictor {

  private final long maxBytes;
  private final TreeSet<CacheSpan> leastRecentlyUsed;

  private long currentSize;

  public LeastRecentlyUsedCacheEvictor(long maxBytes) {
    this.maxBytes = maxBytes;
    this.leastRecentlyUsed = new TreeSet<>(LeastRecentlyUsedCacheEvictor::compare);
  }

  @Override
  public boolean requiresCacheSpanTouches() {
    return true;
  }

  @Override
  public void onCacheInitialized() {
    // Do nothing.
  }

  @Override
  public void onStartFile(Cache cache, String key, long position, long length) {
    if (length != C.LENGTH_UNSET) {
      evictCache(cache, length);
    }
  }

  @Override
  public void onSpanAdded(Cache cache, CacheSpan span) {
    leastRecentlyUsed.add(span);
    currentSize += span.length;
    evictCache(cache, 0);
  }

  @Override
  public void onSpanRemoved(Cache cache, CacheSpan span) {
    leastRecentlyUsed.remove(span);
    currentSize -= span.length;
  }

  @Override
  public void onSpanTouched(Cache cache, CacheSpan oldSpan, CacheSpan newSpan) {
    onSpanRemoved(cache, oldSpan);
    onSpanAdded(cache, newSpan);
  }

  private void evictCache(Cache cache, long requiredSpace) {
    while (currentSize + requiredSpace > maxBytes && !leastRecentlyUsed.isEmpty()) {
      cache.removeSpan(leastRecentlyUsed.first());
    }
  }

  private static int compare(CacheSpan lhs, CacheSpan rhs) {
    long lastTouchTimestampDelta = lhs.lastTouchTimestamp - rhs.lastTouchTimestamp;
    if (lastTouchTimestampDelta == 0) {
      // Use the standard compareTo method as a tie-break.
      return lhs.compareTo(rhs);
    }
    return lhs.lastTouchTimestamp < rhs.lastTouchTimestamp ? -1 : 1;
  }
}
