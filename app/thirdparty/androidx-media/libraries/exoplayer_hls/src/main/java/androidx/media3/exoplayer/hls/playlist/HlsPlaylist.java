/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.hls.playlist;

import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.offline.FilterableManifest;
import java.util.Collections;
import java.util.List;

/** Represents an HLS playlist. */
@UnstableApi
public abstract class HlsPlaylist implements FilterableManifest<HlsPlaylist> {

  /** The base uri. Used to resolve relative paths. */
  public final String baseUri;

  /** The list of tags in the playlist. */
  public final List<String> tags;

  /**
   * Whether the media is formed of independent segments, as defined by the
   * #EXT-X-INDEPENDENT-SEGMENTS tag.
   */
  public final boolean hasIndependentSegments;

  /**
   * @param baseUri See {@link #baseUri}.
   * @param tags See {@link #tags}.
   * @param hasIndependentSegments See {@link #hasIndependentSegments}.
   */
  protected HlsPlaylist(String baseUri, List<String> tags, boolean hasIndependentSegments) {
    this.baseUri = baseUri;
    this.tags = Collections.unmodifiableList(tags);
    this.hasIndependentSegments = hasIndependentSegments;
  }
}
