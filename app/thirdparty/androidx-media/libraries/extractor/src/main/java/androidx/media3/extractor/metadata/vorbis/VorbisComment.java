/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.metadata.vorbis;

import androidx.media3.common.util.UnstableApi;

/** A vorbis comment, extracted from a FLAC or Ogg file. */
@SuppressWarnings("deprecation") // Extending deprecated type for backwards compatibility.
@UnstableApi
public final class VorbisComment extends androidx.media3.extractor.metadata.flac.VorbisComment {

  /**
   * @param key The key.
   * @param value The value.
   */
  public VorbisComment(String key, String value) {
    super(key, value);
  }
}
