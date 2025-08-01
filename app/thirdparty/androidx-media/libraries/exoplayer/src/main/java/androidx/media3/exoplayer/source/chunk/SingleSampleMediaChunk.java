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
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.text.TextRenderer;
import androidx.media3.extractor.DefaultExtractorInput;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.TrackOutput;
import java.io.IOException;

/**
 * @deprecated The only use for this class is subtitle playback, but it is only compatible with
 *     legacy subtitle decoding, which is not supported by default. Users of this class almost
 *     certainly need to {@linkplain TextRenderer#experimentalSetLegacyDecodingEnabled(boolean)
 *     enable legacy subtitle decoding} on their {@link ExoPlayer} instance in order to avoid
 *     playback errors when trying to play subtitles.
 */
@UnstableApi
@Deprecated
public final class SingleSampleMediaChunk extends BaseMediaChunk {

  private final @C.TrackType int trackType;
  private final Format sampleFormat;

  private long nextLoadPosition;
  private boolean loadCompleted;

  /**
   * @param dataSource The source from which the data should be loaded.
   * @param dataSpec Defines the data to be loaded.
   * @param trackFormat See {@link #trackFormat}.
   * @param trackSelectionReason See {@link #trackSelectionReason}.
   * @param trackSelectionData See {@link #trackSelectionData}.
   * @param startTimeUs The start time of the media contained by the chunk, in microseconds.
   * @param endTimeUs The end time of the media contained by the chunk, in microseconds.
   * @param chunkIndex The index of the chunk, or {@link C#INDEX_UNSET} if it is not known.
   * @param trackType The {@link C.TrackType track type} of the chunk.
   * @param sampleFormat The {@link Format} of the sample in the chunk.
   */
  public SingleSampleMediaChunk(
      DataSource dataSource,
      DataSpec dataSpec,
      Format trackFormat,
      @C.SelectionReason int trackSelectionReason,
      @Nullable Object trackSelectionData,
      long startTimeUs,
      long endTimeUs,
      long chunkIndex,
      @C.TrackType int trackType,
      Format sampleFormat) {
    super(
        dataSource,
        dataSpec,
        trackFormat,
        trackSelectionReason,
        trackSelectionData,
        startTimeUs,
        endTimeUs,
        /* clippedStartTimeUs= */ C.TIME_UNSET,
        /* clippedEndTimeUs= */ C.TIME_UNSET,
        chunkIndex);
    this.trackType = trackType;
    this.sampleFormat = sampleFormat;
  }

  @Override
  public boolean isLoadCompleted() {
    return loadCompleted;
  }

  // Loadable implementation.

  @Override
  public void cancelLoad() {
    // Do nothing.
  }

  @SuppressWarnings("NonAtomicVolatileUpdate")
  @Override
  public void load() throws IOException {
    BaseMediaChunkOutput output = getOutput();
    output.setSampleOffsetUs(0);
    TrackOutput trackOutput = output.track(0, trackType);
    trackOutput.format(sampleFormat);
    try {
      // Create and open the input.
      DataSpec loadDataSpec = dataSpec.subrange(nextLoadPosition);
      long length = dataSource.open(loadDataSpec);
      if (length != C.LENGTH_UNSET) {
        length += nextLoadPosition;
      }
      ExtractorInput extractorInput =
          new DefaultExtractorInput(dataSource, nextLoadPosition, length);
      // Load the sample data.
      int result = 0;
      while (result != C.RESULT_END_OF_INPUT) {
        nextLoadPosition += result;
        result = trackOutput.sampleData(extractorInput, Integer.MAX_VALUE, true);
      }
      int sampleSize = (int) nextLoadPosition;
      trackOutput.sampleMetadata(startTimeUs, C.BUFFER_FLAG_KEY_FRAME, sampleSize, 0, null);
    } finally {
      DataSourceUtil.closeQuietly(dataSource);
    }
    loadCompleted = true;
  }
}
