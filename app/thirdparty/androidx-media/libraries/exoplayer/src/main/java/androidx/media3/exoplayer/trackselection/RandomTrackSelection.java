/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.trackselection;

import android.os.SystemClock;
import androidx.annotation.Nullable;
import androidx.media3.common.C;
import androidx.media3.common.Timeline;
import androidx.media3.common.TrackGroup;
import androidx.media3.common.util.NullableType;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.source.MediaSource.MediaPeriodId;
import androidx.media3.exoplayer.source.chunk.MediaChunk;
import androidx.media3.exoplayer.source.chunk.MediaChunkIterator;
import androidx.media3.exoplayer.upstream.BandwidthMeter;
import java.util.List;
import java.util.Random;

/** An {@link ExoTrackSelection} whose selected track is updated randomly. */
@UnstableApi
public final class RandomTrackSelection extends BaseTrackSelection {

  /** Factory for {@link RandomTrackSelection} instances. */
  public static final class Factory implements ExoTrackSelection.Factory {

    private final Random random;

    public Factory() {
      random = new Random();
    }

    /**
     * @param seed A seed for the {@link Random} instance used by the factory.
     */
    public Factory(int seed) {
      random = new Random(seed);
    }

    @Override
    public @NullableType ExoTrackSelection[] createTrackSelections(
        @NullableType Definition[] definitions,
        BandwidthMeter bandwidthMeter,
        MediaPeriodId mediaPeriodId,
        Timeline timeline) {
      return TrackSelectionUtil.createTrackSelectionsForDefinitions(
          definitions,
          definition ->
              new RandomTrackSelection(
                  definition.group, definition.tracks, definition.type, random));
    }
  }

  private final Random random;

  private int selectedIndex;

  /**
   * Creates a new instance.
   *
   * @param group The {@link TrackGroup}. Must not be null.
   * @param tracks The indices of the selected tracks within the {@link TrackGroup}. Must not be
   *     null or empty. May be in any order.
   * @param type The {@link Type} of this track selection.
   * @param random A source of random numbers.
   */
  public RandomTrackSelection(TrackGroup group, int[] tracks, @Type int type, Random random) {
    super(group, tracks, type);
    this.random = random;
    selectedIndex = random.nextInt(length);
  }

  @Override
  public void updateSelectedTrack(
      long playbackPositionUs,
      long bufferedDurationUs,
      long availableDurationUs,
      List<? extends MediaChunk> queue,
      MediaChunkIterator[] mediaChunkIterators) {
    // Count the number of allowed formats.
    long nowMs = SystemClock.elapsedRealtime();
    int allowedFormatCount = 0;
    for (int i = 0; i < length; i++) {
      if (!isTrackExcluded(i, nowMs)) {
        allowedFormatCount++;
      }
    }

    selectedIndex = random.nextInt(allowedFormatCount);
    if (allowedFormatCount != length) {
      // Adjust the format index to account for excluded formats.
      allowedFormatCount = 0;
      for (int i = 0; i < length; i++) {
        if (!isTrackExcluded(i, nowMs) && selectedIndex == allowedFormatCount++) {
          selectedIndex = i;
          return;
        }
      }
    }
  }

  @Override
  public int getSelectedIndex() {
    return selectedIndex;
  }

  @Override
  public int getSelectionReason() {
    return C.SELECTION_REASON_ADAPTIVE;
  }

  @Override
  @Nullable
  public Object getSelectionData() {
    return null;
  }
}
