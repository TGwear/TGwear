/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.trackselection;

import androidx.annotation.Nullable;
import androidx.media3.common.util.NullableType;
import androidx.media3.common.util.UnstableApi;
import java.util.Arrays;

/** An array of {@link TrackSelection}s. */
@UnstableApi
public final class TrackSelectionArray {

  /** The length of this array. */
  public final int length;

  private final @NullableType TrackSelection[] trackSelections;

  // Lazily initialized hashcode.
  private int hashCode;

  /**
   * @param trackSelections The selections. Must not be null, but may contain null elements.
   */
  public TrackSelectionArray(@NullableType TrackSelection... trackSelections) {
    this.trackSelections = trackSelections;
    this.length = trackSelections.length;
  }

  /**
   * Returns the selection at a given index.
   *
   * @param index The index of the selection.
   * @return The selection.
   */
  @Nullable
  public TrackSelection get(int index) {
    return trackSelections[index];
  }

  /** Returns the selections in a newly allocated array. */
  public @NullableType TrackSelection[] getAll() {
    return trackSelections.clone();
  }

  @Override
  public int hashCode() {
    if (hashCode == 0) {
      int result = 17;
      result = 31 * result + Arrays.hashCode(trackSelections);
      hashCode = result;
    }
    return hashCode;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    TrackSelectionArray other = (TrackSelectionArray) obj;
    return Arrays.equals(trackSelections, other.trackSelections);
  }
}
