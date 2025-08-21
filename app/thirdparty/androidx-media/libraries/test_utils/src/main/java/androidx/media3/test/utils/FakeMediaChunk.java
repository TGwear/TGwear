/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.test.utils;

import android.net.Uri;
import androidx.media3.common.C;
import androidx.media3.common.Format;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DataSpec;
import androidx.media3.datasource.DefaultHttpDataSource;
import androidx.media3.exoplayer.source.chunk.MediaChunk;

/** Fake {@link MediaChunk}. */
@UnstableApi
public final class FakeMediaChunk extends MediaChunk {

  private static final DataSource DATA_SOURCE =
      new DefaultHttpDataSource.Factory().setUserAgent("TEST_AGENT").createDataSource();

  /**
   * Creates a fake media chunk.
   *
   * @param trackFormat The {@link Format}.
   * @param startTimeUs The start time of the media, in microseconds.
   * @param endTimeUs The end time of the media, in microseconds.
   */
  public FakeMediaChunk(Format trackFormat, long startTimeUs, long endTimeUs) {
    this(trackFormat, startTimeUs, endTimeUs, C.SELECTION_REASON_UNKNOWN);
  }

  /**
   * Creates a fake media chunk.
   *
   * @param trackFormat The {@link Format}.
   * @param startTimeUs The start time of the media, in microseconds.
   * @param endTimeUs The end time of the media, in microseconds.
   * @param selectionReason One of the {@link C.SelectionReason selection reasons}.
   */
  public FakeMediaChunk(
      Format trackFormat,
      long startTimeUs,
      long endTimeUs,
      @C.SelectionReason int selectionReason) {
    super(
        DATA_SOURCE,
        new DataSpec(Uri.EMPTY),
        trackFormat,
        selectionReason,
        /* trackSelectionData= */ null,
        startTimeUs,
        endTimeUs,
        /* chunkIndex= */ 0);
  }

  @Override
  public void cancelLoad() {
    // Do nothing.
  }

  @Override
  public void load() {
    // Do nothing.
  }

  @Override
  public boolean isLoadCompleted() {
    return true;
  }
}
