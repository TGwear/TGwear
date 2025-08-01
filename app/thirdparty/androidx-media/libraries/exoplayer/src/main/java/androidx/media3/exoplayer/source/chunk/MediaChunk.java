/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.source.chunk;

import androidx.annotation.Nullable;
import androidx.media3.common.C;
import androidx.media3.common.Format;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DataSpec;

/** An abstract base class for {@link Chunk}s that contain media samples. */
@UnstableApi
public abstract class MediaChunk extends Chunk {

  /** The chunk index, or {@link C#INDEX_UNSET} if it is not known. */
  public final long chunkIndex;

  /**
   * @param dataSource The source from which the data should be loaded.
   * @param dataSpec Defines the data to be loaded.
   * @param trackFormat See {@link #trackFormat}.
   * @param trackSelectionReason See {@link #trackSelectionReason}.
   * @param trackSelectionData See {@link #trackSelectionData}.
   * @param startTimeUs The start time of the media contained by the chunk, in microseconds.
   * @param endTimeUs The end time of the media contained by the chunk, in microseconds.
   * @param chunkIndex The index of the chunk, or {@link C#INDEX_UNSET} if it is not known.
   */
  public MediaChunk(
      DataSource dataSource,
      DataSpec dataSpec,
      Format trackFormat,
      @C.SelectionReason int trackSelectionReason,
      @Nullable Object trackSelectionData,
      long startTimeUs,
      long endTimeUs,
      long chunkIndex) {
    super(
        dataSource,
        dataSpec,
        C.DATA_TYPE_MEDIA,
        trackFormat,
        trackSelectionReason,
        trackSelectionData,
        startTimeUs,
        endTimeUs);
    Assertions.checkNotNull(trackFormat);
    this.chunkIndex = chunkIndex;
  }

  /** Returns the next chunk index or {@link C#INDEX_UNSET} if it is not known. */
  public long getNextChunkIndex() {
    return chunkIndex != C.INDEX_UNSET ? chunkIndex + 1 : C.INDEX_UNSET;
  }

  /** Returns whether the chunk has been fully loaded. */
  public abstract boolean isLoadCompleted();
}
