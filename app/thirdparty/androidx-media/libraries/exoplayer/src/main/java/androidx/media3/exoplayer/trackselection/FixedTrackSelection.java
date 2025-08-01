/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.trackselection;

import androidx.annotation.Nullable;
import androidx.media3.common.C;
import androidx.media3.common.TrackGroup;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.source.chunk.MediaChunk;
import androidx.media3.exoplayer.source.chunk.MediaChunkIterator;
import java.util.List;

/** A {@link TrackSelection} consisting of a single track. */
@UnstableApi
public final class FixedTrackSelection extends BaseTrackSelection {

  private final @C.SelectionReason int reason;
  @Nullable private final Object data;

  /**
   * @param group The {@link TrackGroup}. Must not be null.
   * @param track The index of the selected track within the {@link TrackGroup}.
   */
  public FixedTrackSelection(TrackGroup group, int track) {
    this(group, /* track= */ track, /* type= */ TrackSelection.TYPE_UNSET);
  }

  /**
   * @param group The {@link TrackGroup}. Must not be null.
   * @param track The index of the selected track within the {@link TrackGroup}.
   * @param type The type that will be returned from {@link TrackSelection#getType()}.
   */
  public FixedTrackSelection(TrackGroup group, int track, @Type int type) {
    this(group, track, type, C.SELECTION_REASON_UNKNOWN, /* data= */ null);
  }

  /**
   * @param group The {@link TrackGroup}. Must not be null.
   * @param track The index of the selected track within the {@link TrackGroup}.
   * @param type The type that will be returned from {@link TrackSelection#getType()}.
   * @param reason A reason for the track selection.
   * @param data Optional data associated with the track selection.
   */
  public FixedTrackSelection(
      TrackGroup group,
      int track,
      @Type int type,
      @C.SelectionReason int reason,
      @Nullable Object data) {
    super(group, /* tracks= */ new int[] {track}, type);
    this.reason = reason;
    this.data = data;
  }

  @Override
  public void updateSelectedTrack(
      long playbackPositionUs,
      long bufferedDurationUs,
      long availableDurationUs,
      List<? extends MediaChunk> queue,
      MediaChunkIterator[] mediaChunkIterators) {
    // Do nothing.
  }

  @Override
  public int getSelectedIndex() {
    return 0;
  }

  @Override
  public @C.SelectionReason int getSelectionReason() {
    return reason;
  }

  @Override
  @Nullable
  public Object getSelectionData() {
    return data;
  }
}
