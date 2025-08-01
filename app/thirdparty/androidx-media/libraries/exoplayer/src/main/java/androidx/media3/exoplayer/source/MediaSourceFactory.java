/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.source;

import androidx.annotation.Nullable;
import androidx.media3.common.C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.drm.DrmSessionManagerProvider;
import androidx.media3.exoplayer.upstream.LoadErrorHandlingPolicy;

/**
 * @deprecated Use {@link MediaSource.Factory}.
 */
@UnstableApi
@Deprecated
public interface MediaSourceFactory extends MediaSource.Factory {

  /**
   * An instance that throws {@link UnsupportedOperationException} from {@link #createMediaSource}
   * and {@link #getSupportedTypes()}.
   */
  @SuppressWarnings("deprecation") // Creating instance of deprecated type
  @UnstableApi
  MediaSourceFactory UNSUPPORTED =
      new MediaSourceFactory() {
        @Override
        public MediaSourceFactory setDrmSessionManagerProvider(
            @Nullable DrmSessionManagerProvider drmSessionManagerProvider) {
          return this;
        }

        @Override
        public MediaSourceFactory setLoadErrorHandlingPolicy(
            @Nullable LoadErrorHandlingPolicy loadErrorHandlingPolicy) {
          return this;
        }

        @Override
        public @C.ContentType int[] getSupportedTypes() {
          throw new UnsupportedOperationException();
        }

        @Override
        public MediaSource createMediaSource(MediaItem mediaItem) {
          throw new UnsupportedOperationException();
        }
      };
}
