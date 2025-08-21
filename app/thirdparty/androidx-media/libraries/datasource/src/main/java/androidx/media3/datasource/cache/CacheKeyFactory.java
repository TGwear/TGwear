/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.datasource.cache;

import androidx.media3.common.util.UnstableApi;
import androidx.media3.datasource.DataSpec;

/** Factory for cache keys. */
@UnstableApi
public interface CacheKeyFactory {

  /** Default {@link CacheKeyFactory}. */
  CacheKeyFactory DEFAULT =
      (dataSpec) -> dataSpec.key != null ? dataSpec.key : dataSpec.uri.toString();

  /**
   * Returns the cache key of the resource containing the data defined by a {@link DataSpec}.
   *
   * <p>Note that since the returned cache key corresponds to the whole resource, implementations
   * must not return different cache keys for {@link DataSpec DataSpecs} that define different
   * ranges of the same resource. As a result, implementations should not use fields such as {@link
   * DataSpec#position} and {@link DataSpec#length}.
   *
   * @param dataSpec The {@link DataSpec}.
   * @return The cache key of the resource.
   */
  String buildCacheKey(DataSpec dataSpec);
}
