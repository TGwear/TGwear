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
import androidx.media3.common.C.DataType;
import androidx.media3.common.C.SelectionReason;
import androidx.media3.common.C.TrackType;
import androidx.media3.common.Format;
import androidx.media3.common.util.UnstableApi;

/** Descriptor for data being loaded or selected by a {@link MediaSource}. */
@UnstableApi
public final class MediaLoadData {

  /** The {@link DataType data type}. */
  public final @DataType int dataType;

  /**
   * One of the {@link TrackType track types}, which is a media track type if the data corresponds
   * to media of a specific type, or {@link C#TRACK_TYPE_UNKNOWN} otherwise.
   */
  public final @TrackType int trackType;

  /**
   * The format of the track to which the data belongs. Null if the data does not belong to a
   * specific track.
   */
  @Nullable public final Format trackFormat;

  /**
   * One of the {@link SelectionReason selection reasons} if the data belongs to a track. {@link
   * C#SELECTION_REASON_UNKNOWN} otherwise.
   */
  public final @C.SelectionReason int trackSelectionReason;

  /**
   * Optional data associated with the selection of the track to which the data belongs. Null if the
   * data does not belong to a track.
   */
  @Nullable public final Object trackSelectionData;

  /**
   * The start time of the media in the {@link MediaPeriod}, or {@link C#TIME_UNSET} if the data
   * does not belong to a specific {@link MediaPeriod}.
   */
  public final long mediaStartTimeMs;

  /**
   * The end time of the media in the {@link MediaPeriod}, or {@link C#TIME_UNSET} if the data does
   * not belong to a specific {@link MediaPeriod} or the end time is unknown.
   */
  public final long mediaEndTimeMs;

  /** Creates an instance with the given {@link #dataType}. */
  public MediaLoadData(@DataType int dataType) {
    this(
        dataType,
        /* trackType= */ C.TRACK_TYPE_UNKNOWN,
        /* trackFormat= */ null,
        /* trackSelectionReason= */ C.SELECTION_REASON_UNKNOWN,
        /* trackSelectionData= */ null,
        /* mediaStartTimeMs= */ C.TIME_UNSET,
        /* mediaEndTimeMs= */ C.TIME_UNSET);
  }

  /**
   * Creates media load data.
   *
   * @param dataType See {@link #dataType}.
   * @param trackType See {@link #trackType}.
   * @param trackFormat See {@link #trackFormat}.
   * @param trackSelectionReason See {@link #trackSelectionReason}.
   * @param trackSelectionData See {@link #trackSelectionData}.
   * @param mediaStartTimeMs See {@link #mediaStartTimeMs}.
   * @param mediaEndTimeMs See {@link #mediaEndTimeMs}.
   */
  public MediaLoadData(
      @DataType int dataType,
      @TrackType int trackType,
      @Nullable Format trackFormat,
      @SelectionReason int trackSelectionReason,
      @Nullable Object trackSelectionData,
      long mediaStartTimeMs,
      long mediaEndTimeMs) {
    this.dataType = dataType;
    this.trackType = trackType;
    this.trackFormat = trackFormat;
    this.trackSelectionReason = trackSelectionReason;
    this.trackSelectionData = trackSelectionData;
    this.mediaStartTimeMs = mediaStartTimeMs;
    this.mediaEndTimeMs = mediaEndTimeMs;
  }
}
