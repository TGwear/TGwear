/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.source;

import androidx.media3.common.C;
import androidx.media3.common.util.UnstableApi;
import java.util.List;

/** A factory to create composite {@link SequenceableLoader}s. */
@UnstableApi
public interface CompositeSequenceableLoaderFactory {

  /** Returns an empty composite {@link SequenceableLoader}, with no delegate loaders. */
  SequenceableLoader empty();

  /**
   * @deprecated Use {@link #empty()} for an empty composite loader, or {@link #create(List, List)}
   *     for a non-empty one.
   */
  @Deprecated
  SequenceableLoader createCompositeSequenceableLoader(SequenceableLoader... loaders);

  /**
   * Creates a composite {@link SequenceableLoader}.
   *
   * @param loaders The sub-loaders that make up the {@link SequenceableLoader} to be built.
   * @param loaderTrackTypes The track types handled by each entry in {@code loaders}. Must be the
   *     same as {@code loaders}.
   * @return A composite {@link SequenceableLoader} that comprises the given loaders.
   */
  SequenceableLoader create(
      List<? extends SequenceableLoader> loaders,
      List<List<@C.TrackType Integer>> loaderTrackTypes);
}
