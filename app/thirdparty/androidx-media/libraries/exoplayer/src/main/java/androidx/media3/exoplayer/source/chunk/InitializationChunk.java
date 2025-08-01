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
import androidx.media3.common.util.UnstableApi;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DataSourceUtil;
import androidx.media3.datasource.DataSpec;
import androidx.media3.exoplayer.source.chunk.ChunkExtractor.TrackOutputProvider;
import androidx.media3.extractor.ChunkIndex;
import androidx.media3.extractor.DefaultExtractorInput;
import androidx.media3.extractor.Extractor;
import androidx.media3.extractor.ExtractorInput;
import java.io.IOException;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

/**
 * A {@link Chunk} that uses an {@link Extractor} to decode initialization data for single track.
 */
@UnstableApi
public final class InitializationChunk extends Chunk {

  private final ChunkExtractor chunkExtractor;

  private @MonotonicNonNull TrackOutputProvider trackOutputProvider;
  @Nullable private ChunkIndex chunkIndex;
  private long nextLoadPosition;
  private volatile boolean loadCanceled;

  /**
   * @param dataSource The source from which the data should be loaded.
   * @param dataSpec Defines the data to be loaded.
   * @param trackFormat See {@link #trackFormat}.
   * @param trackSelectionReason See {@link #trackSelectionReason}.
   * @param trackSelectionData See {@link #trackSelectionData}.
   * @param chunkExtractor A wrapped extractor to use for parsing the initialization data.
   */
  public InitializationChunk(
      DataSource dataSource,
      DataSpec dataSpec,
      Format trackFormat,
      @C.SelectionReason int trackSelectionReason,
      @Nullable Object trackSelectionData,
      ChunkExtractor chunkExtractor) {
    super(
        dataSource,
        dataSpec,
        C.DATA_TYPE_MEDIA_INITIALIZATION,
        trackFormat,
        trackSelectionReason,
        trackSelectionData,
        C.TIME_UNSET,
        C.TIME_UNSET);
    this.chunkExtractor = chunkExtractor;
  }

  /**
   * Initializes the chunk for loading, setting a {@link TrackOutputProvider} for track outputs to
   * which formats will be written as they are loaded.
   *
   * @param trackOutputProvider The {@link TrackOutputProvider} for track outputs to which formats
   *     will be written as they are loaded.
   */
  public void init(TrackOutputProvider trackOutputProvider) {
    this.trackOutputProvider = trackOutputProvider;
  }

  // Loadable implementation.

  @Override
  public void cancelLoad() {
    loadCanceled = true;
  }

  @SuppressWarnings("NonAtomicVolatileUpdate")
  @Override
  public void load() throws IOException {
    if (nextLoadPosition == 0) {
      chunkExtractor.init(
          trackOutputProvider, /* startTimeUs= */ C.TIME_UNSET, /* endTimeUs= */ C.TIME_UNSET);
    }
    try {
      // Create and open the input.
      DataSpec loadDataSpec = dataSpec.subrange(nextLoadPosition);
      ExtractorInput input =
          new DefaultExtractorInput(
              dataSource, loadDataSpec.position, dataSource.open(loadDataSpec));
      // Load and decode the initialization data.
      try {
        while (!loadCanceled && chunkExtractor.read(input)) {}
      } finally {
        nextLoadPosition = input.getPosition() - dataSpec.position;
        chunkIndex = chunkExtractor.getChunkIndex();
      }
    } finally {
      DataSourceUtil.closeQuietly(dataSource);
    }
  }

  /**
   * Returns the {@link ChunkIndex} obtained from the initialization chunk, or null if a {@link
   * ChunkIndex} has not been obtained.
   */
  @Nullable
  public ChunkIndex getChunkIndex() {
    return chunkIndex;
  }
}
