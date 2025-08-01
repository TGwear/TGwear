/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.datasource.cache;

import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.media3.common.C;
import androidx.media3.common.util.UnstableApi;

/** Interface for an immutable snapshot of keyed metadata. */
@UnstableApi
public interface ContentMetadata {

  /**
   * Prefix for custom metadata keys. Applications can use keys starting with this prefix without
   * any risk of their keys colliding with ones defined by the ExoPlayer library.
   */
  @SuppressWarnings("unused")
  String KEY_CUSTOM_PREFIX = "custom_";

  /** Key for redirected uri (type: String). */
  String KEY_REDIRECTED_URI = "exo_redir";

  /** Key for content length in bytes (type: long). */
  String KEY_CONTENT_LENGTH = "exo_len";

  /**
   * Returns a metadata value.
   *
   * @param key Key of the metadata to be returned.
   * @param defaultValue Value to return if the metadata doesn't exist.
   * @return The metadata value.
   */
  @Nullable
  byte[] get(String key, @Nullable byte[] defaultValue);

  /**
   * Returns a metadata value.
   *
   * @param key Key of the metadata to be returned.
   * @param defaultValue Value to return if the metadata doesn't exist.
   * @return The metadata value.
   */
  @Nullable
  String get(String key, @Nullable String defaultValue);

  /**
   * Returns a metadata value.
   *
   * @param key Key of the metadata to be returned.
   * @param defaultValue Value to return if the metadata doesn't exist.
   * @return The metadata value.
   */
  long get(String key, long defaultValue);

  /** Returns whether the metadata is available. */
  boolean contains(String key);

  /**
   * Returns the value stored under {@link #KEY_CONTENT_LENGTH}, or {@link C#LENGTH_UNSET} if not
   * set.
   */
  static long getContentLength(ContentMetadata contentMetadata) {
    return contentMetadata.get(KEY_CONTENT_LENGTH, C.LENGTH_UNSET);
  }

  /**
   * Returns the value stored under {@link #KEY_REDIRECTED_URI} as a {@link Uri}, or {code null} if
   * not set.
   */
  @Nullable
  static Uri getRedirectedUri(ContentMetadata contentMetadata) {
    @Nullable String redirectedUri = contentMetadata.get(KEY_REDIRECTED_URI, (String) null);
    return redirectedUri == null ? null : Uri.parse(redirectedUri);
  }
}
