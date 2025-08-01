/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.dash.manifest;

import androidx.annotation.Nullable;
import androidx.media3.common.C;
import androidx.media3.common.util.UnstableApi;
import java.util.Collections;
import java.util.List;

/** Encapsulates media content components over a contiguous period of time. */
@UnstableApi
public class Period {

  /** The period identifier, if one exists. */
  @Nullable public final String id;

  /** The start time of the period in milliseconds, relative to the start of the manifest. */
  public final long startMs;

  /** The adaptation sets belonging to the period. */
  public final List<AdaptationSet> adaptationSets;

  /** The event stream belonging to the period. */
  public final List<EventStream> eventStreams;

  /** The asset identifier for this period, if one exists */
  @Nullable public final Descriptor assetIdentifier;

  /**
   * @param id The period identifier. May be null.
   * @param startMs The start time of the period in milliseconds.
   * @param adaptationSets The adaptation sets belonging to the period.
   */
  public Period(@Nullable String id, long startMs, List<AdaptationSet> adaptationSets) {
    this(id, startMs, adaptationSets, Collections.emptyList(), /* assetIdentifier= */ null);
  }

  /**
   * @param id The period identifier. May be null.
   * @param startMs The start time of the period in milliseconds.
   * @param adaptationSets The adaptation sets belonging to the period.
   * @param eventStreams The {@link EventStream}s belonging to the period.
   */
  public Period(
      @Nullable String id,
      long startMs,
      List<AdaptationSet> adaptationSets,
      List<EventStream> eventStreams) {
    this(id, startMs, adaptationSets, eventStreams, /* assetIdentifier= */ null);
  }

  /**
   * @param id The period identifier. May be null.
   * @param startMs The start time of the period in milliseconds.
   * @param adaptationSets The adaptation sets belonging to the period.
   * @param eventStreams The {@link EventStream}s belonging to the period.
   * @param assetIdentifier The asset identifier for this period
   */
  public Period(
      @Nullable String id,
      long startMs,
      List<AdaptationSet> adaptationSets,
      List<EventStream> eventStreams,
      @Nullable Descriptor assetIdentifier) {
    this.id = id;
    this.startMs = startMs;
    this.adaptationSets = Collections.unmodifiableList(adaptationSets);
    this.eventStreams = Collections.unmodifiableList(eventStreams);
    this.assetIdentifier = assetIdentifier;
  }

  /**
   * Returns the index of the first adaptation set of a given type, or {@link C#INDEX_UNSET} if no
   * adaptation set of the specified type exists.
   *
   * @param type An adaptation set type.
   * @return The index of the first adaptation set of the specified type, or {@link C#INDEX_UNSET}.
   */
  public int getAdaptationSetIndex(int type) {
    int adaptationCount = adaptationSets.size();
    for (int i = 0; i < adaptationCount; i++) {
      if (adaptationSets.get(i).type == type) {
        return i;
      }
    }
    return C.INDEX_UNSET;
  }
}
