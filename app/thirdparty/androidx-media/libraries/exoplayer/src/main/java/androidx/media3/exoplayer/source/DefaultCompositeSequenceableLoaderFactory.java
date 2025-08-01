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
import com.google.common.collect.ImmutableList;
import java.util.List;

/** Default implementation of {@link CompositeSequenceableLoaderFactory}. */
@UnstableApi
public final class DefaultCompositeSequenceableLoaderFactory
    implements CompositeSequenceableLoaderFactory {

  @Override
  public SequenceableLoader empty() {
    return new CompositeSequenceableLoader(ImmutableList.of(), ImmutableList.of());
  }

  @Deprecated
  @Override
  @SuppressWarnings("deprecation") // Calling deprecated constructor
  public SequenceableLoader createCompositeSequenceableLoader(SequenceableLoader... loaders) {
    return new CompositeSequenceableLoader(loaders);
  }

  @Override
  public SequenceableLoader create(
      List<? extends SequenceableLoader> loaders,
      List<List<@C.TrackType Integer>> loaderTrackTypes) {
    return new CompositeSequenceableLoader(loaders, loaderTrackTypes);
  }
}
