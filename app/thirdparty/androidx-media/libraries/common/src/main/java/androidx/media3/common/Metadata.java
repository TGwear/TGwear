/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common;

import androidx.annotation.Nullable;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.common.util.Util;
import com.google.common.primitives.Longs;
import java.util.Arrays;
import java.util.List;

/** A collection of metadata entries. */
@UnstableApi
public final class Metadata {

  /** A metadata entry. */
  public interface Entry {

    /**
     * Returns the {@link Format} that can be used to decode the wrapped metadata in {@link
     * #getWrappedMetadataBytes()}, or null if this Entry doesn't contain wrapped metadata.
     */
    @Nullable
    default Format getWrappedMetadataFormat() {
      return null;
    }

    /**
     * Returns the bytes of the wrapped metadata in this Entry, or null if it doesn't contain
     * wrapped metadata.
     */
    @Nullable
    default byte[] getWrappedMetadataBytes() {
      return null;
    }

    /**
     * Updates the {@link MediaMetadata.Builder} with the type-specific values stored in this {@code
     * Entry}.
     *
     * @param builder The builder to be updated.
     */
    default void populateMediaMetadata(MediaMetadata.Builder builder) {}
  }

  private final Entry[] entries;

  /**
   * The presentation time of the metadata, in microseconds.
   *
   * <p>This time is an offset from the start of the current {@link Timeline.Period}.
   *
   * <p>This time is {@link C#TIME_UNSET} when not known or undefined.
   */
  public final long presentationTimeUs;

  /**
   * @param entries The metadata entries.
   */
  public Metadata(Entry... entries) {
    this(/* presentationTimeUs= */ C.TIME_UNSET, entries);
  }

  /**
   * @param presentationTimeUs The presentation time for the metadata entries.
   * @param entries The metadata entries.
   */
  public Metadata(long presentationTimeUs, Entry... entries) {
    this.presentationTimeUs = presentationTimeUs;
    this.entries = entries;
  }

  /**
   * @param entries The metadata entries.
   */
  public Metadata(List<? extends Entry> entries) {
    this(entries.toArray(new Entry[0]));
  }

  /**
   * @param presentationTimeUs The presentation time for the metadata entries.
   * @param entries The metadata entries.
   */
  public Metadata(long presentationTimeUs, List<? extends Entry> entries) {
    this(presentationTimeUs, entries.toArray(new Entry[0]));
  }

  /** Returns the number of metadata entries. */
  public int length() {
    return entries.length;
  }

  /**
   * Returns the entry at the specified index.
   *
   * @param index The index of the entry.
   * @return The entry at the specified index.
   */
  public Metadata.Entry get(int index) {
    return entries[index];
  }

  /**
   * Returns a copy of this metadata with the entries of the specified metadata appended. Returns
   * this instance if {@code other} is null.
   *
   * @param other The metadata that holds the entries to append. If null, this methods returns this
   *     instance.
   * @return The metadata instance with the appended entries.
   */
  public Metadata copyWithAppendedEntriesFrom(@Nullable Metadata other) {
    if (other == null) {
      return this;
    }
    return copyWithAppendedEntries(other.entries);
  }

  /**
   * Returns a copy of this metadata with the specified entries appended.
   *
   * @param entriesToAppend The entries to append.
   * @return The metadata instance with the appended entries.
   */
  public Metadata copyWithAppendedEntries(Entry... entriesToAppend) {
    if (entriesToAppend.length == 0) {
      return this;
    }
    return new Metadata(
        presentationTimeUs, Util.nullSafeArrayConcatenation(entries, entriesToAppend));
  }

  /**
   * Returns a copy of this metadata with the specified presentation time.
   *
   * @param presentationTimeUs The new presentation time, in microseconds.
   * @return The metadata instance with the new presentation time.
   */
  public Metadata copyWithPresentationTimeUs(long presentationTimeUs) {
    if (this.presentationTimeUs == presentationTimeUs) {
      return this;
    }
    return new Metadata(presentationTimeUs, entries);
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Metadata other = (Metadata) obj;
    return Arrays.equals(entries, other.entries) && presentationTimeUs == other.presentationTimeUs;
  }

  @Override
  public int hashCode() {
    int result = Arrays.hashCode(entries);
    result = 31 * result + Longs.hashCode(presentationTimeUs);
    return result;
  }

  @Override
  public String toString() {
    return "entries="
        + Arrays.toString(entries)
        + (presentationTimeUs == C.TIME_UNSET ? "" : ", presentationTimeUs=" + presentationTimeUs);
  }
}
