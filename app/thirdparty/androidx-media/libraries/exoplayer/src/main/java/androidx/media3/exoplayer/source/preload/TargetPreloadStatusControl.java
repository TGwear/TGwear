/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.source.preload;

import androidx.annotation.Nullable;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.source.MediaSource;

/** Controls the target preload status. */
@UnstableApi
public interface TargetPreloadStatusControl<T> {

  /**
   * Returns the target preload status for a source with the given {@code rankingData}. May be null
   * if a {@link MediaSource} with the given {@code rankingData} should not be preloaded.
   */
  @Nullable
  PreloadStatus getTargetPreloadStatus(T rankingData);

  /** Defines the status of the preloading for a {@link MediaSource}. */
  interface PreloadStatus {

    /** The stage of the preloading. */
    int getStage();

    /** The associated value of the preloading stage. */
    long getValue();
  }
}
