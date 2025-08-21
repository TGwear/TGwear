/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.text;

import androidx.media3.common.C;
import androidx.media3.common.text.Cue;
import androidx.media3.common.text.CueGroup;
import androidx.media3.extractor.text.CuesWithTiming;
import com.google.common.collect.ImmutableList;

/**
 * A {@code CuesResolver} maps from time to the subtitle cues that should be shown.
 *
 * <p>It also exposes methods for querying when the next and previous change in subtitles is.
 *
 * <p>Different implementations may provide different resolution algorithms.
 */
/* package */ interface CuesResolver {

  /**
   * Adds {@code cues} to this instance, returning whether this changes the cues displayed at {@code
   * currentPositionUs}.
   */
  boolean addCues(CuesWithTiming cues, long currentPositionUs);

  /**
   * Returns the {@linkplain Cue cues} that should be shown at time {@code timeUs}.
   *
   * @param timeUs The time to query, in microseconds.
   * @return The cues that should be shown, ordered by ascending priority for compatibility with
   *     {@link CueGroup#cues}.
   */
  ImmutableList<Cue> getCuesAtTimeUs(long timeUs);

  /**
   * Discards all cues that won't be shown at or after {@code timeUs}.
   *
   * @param timeUs The time to discard cues before, in microseconds.
   */
  void discardCuesBeforeTimeUs(long timeUs);

  /**
   * Returns the time, in microseconds, of the change in {@linkplain #getCuesAtTimeUs(long) cue
   * output} at or before {@code timeUs}.
   *
   * <p>If there's no change before {@code timeUs}, returns {@link C#TIME_UNSET}.
   */
  long getPreviousCueChangeTimeUs(long timeUs);

  /**
   * Returns the time, in microseconds, of the next change in {@linkplain #getCuesAtTimeUs(long) cue
   * output} after {@code timeUs} (exclusive).
   *
   * <p>If there's no change after {@code timeUs}, returns {@link C#TIME_END_OF_SOURCE}.
   */
  long getNextCueChangeTimeUs(long timeUs);

  /** Clears all cues that have been added to this instance. */
  void clear();
}
