/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.transformer;

import androidx.annotation.Nullable;
import androidx.media3.common.Format;
import androidx.media3.common.MediaItem;

/** A listener for {@link MediaItem} changes in the {@linkplain SampleExporter sample exporters}. */
/* package */ interface OnMediaItemChangedListener {

  /**
   * Called when the {@link MediaItem} whose samples are passed to the {@link SampleExporter}
   * changes.
   *
   * @param editedMediaItem The {@link MediaItem} with the transformations to apply to it.
   * @param durationUs The duration of the {@link MediaItem}, in microseconds.
   * @param decodedFormat The {@link Format} decoded from the {@link MediaItem} track, which
   *     represents the samples output from the {@link SampleExporter}. {@code null} if no such
   *     track was decoded.
   * @param isLast Whether the {@link MediaItem} is the last one passed to the {@link
   *     SampleExporter}.
   */
  void onMediaItemChanged(
      EditedMediaItem editedMediaItem,
      long durationUs,
      @Nullable Format decodedFormat,
      boolean isLast);
}
