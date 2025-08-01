/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer;

import androidx.media3.common.util.UnstableApi;

/** A list of {@link RendererCapabilities}. */
@UnstableApi
public interface RendererCapabilitiesList {

  /** A factory for {@link RendererCapabilitiesList} instances. */
  interface Factory {

    /** Creates a {@link RendererCapabilitiesList} instance. */
    RendererCapabilitiesList createRendererCapabilitiesList();
  }

  /** Returns an array of {@link RendererCapabilities}. */
  RendererCapabilities[] getRendererCapabilities();

  /** Returns the number of {@link RendererCapabilities}. */
  int size();

  /** Releases any resources associated with this {@link RendererCapabilitiesList}. */
  void release();
}
