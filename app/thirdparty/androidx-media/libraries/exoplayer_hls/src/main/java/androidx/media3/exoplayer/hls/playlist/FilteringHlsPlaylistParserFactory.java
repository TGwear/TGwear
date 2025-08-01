/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.hls.playlist;

import androidx.annotation.Nullable;
import androidx.media3.common.StreamKey;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.offline.FilteringManifestParser;
import androidx.media3.exoplayer.upstream.ParsingLoadable;
import java.util.List;

/**
 * A {@link HlsPlaylistParserFactory} that includes only the streams identified by the given stream
 * keys.
 */
@UnstableApi
public final class FilteringHlsPlaylistParserFactory implements HlsPlaylistParserFactory {

  private final HlsPlaylistParserFactory hlsPlaylistParserFactory;
  private final List<StreamKey> streamKeys;

  /**
   * @param hlsPlaylistParserFactory A factory for the parsers of the playlists which will be
   *     filtered.
   * @param streamKeys The stream keys. If null or empty then filtering will not occur.
   */
  public FilteringHlsPlaylistParserFactory(
      HlsPlaylistParserFactory hlsPlaylistParserFactory, List<StreamKey> streamKeys) {
    this.hlsPlaylistParserFactory = hlsPlaylistParserFactory;
    this.streamKeys = streamKeys;
  }

  @Override
  public ParsingLoadable.Parser<HlsPlaylist> createPlaylistParser() {
    return new FilteringManifestParser<>(
        hlsPlaylistParserFactory.createPlaylistParser(), streamKeys);
  }

  @Override
  public ParsingLoadable.Parser<HlsPlaylist> createPlaylistParser(
      HlsMultivariantPlaylist multivariantPlaylist,
      @Nullable HlsMediaPlaylist previousMediaPlaylist) {
    return new FilteringManifestParser<>(
        hlsPlaylistParserFactory.createPlaylistParser(multivariantPlaylist, previousMediaPlaylist),
        streamKeys);
  }
}
