/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.hls;

import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.hls.playlist.HlsMediaPlaylist;
import androidx.media3.exoplayer.hls.playlist.HlsMultivariantPlaylist;

/** Holds a multivariant playlist along with a snapshot of one of its media playlists. */
@UnstableApi
public final class HlsManifest {

  /** The multivariant playlist of an HLS stream. */
  public final HlsMultivariantPlaylist multivariantPlaylist;

  /** A snapshot of a media playlist referred to by {@link #multivariantPlaylist}. */
  public final HlsMediaPlaylist mediaPlaylist;

  /**
   * @param multivariantPlaylist The multivariant playlist.
   * @param mediaPlaylist The media playlist.
   */
  /* package */ HlsManifest(
      HlsMultivariantPlaylist multivariantPlaylist, HlsMediaPlaylist mediaPlaylist) {
    this.multivariantPlaylist = multivariantPlaylist;
    this.mediaPlaylist = mediaPlaylist;
  }
}
