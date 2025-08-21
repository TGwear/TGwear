/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.datasource.cache;

/** Metadata associated with a cache file. */
/* package */ final class CacheFileMetadata {

  public final long length;
  public final long lastTouchTimestamp;

  public CacheFileMetadata(long length, long lastTouchTimestamp) {
    this.length = length;
    this.lastTouchTimestamp = lastTouchTimestamp;
  }
}
