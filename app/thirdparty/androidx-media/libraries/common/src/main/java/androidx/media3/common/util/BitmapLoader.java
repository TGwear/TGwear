/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common.util;

import android.graphics.Bitmap;
import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.media3.common.MediaMetadata;
import com.google.common.util.concurrent.ListenableFuture;

/** Loads images. */
@UnstableApi
public interface BitmapLoader {

  /** Returns whether the given {@code mimeType} is supported. */
  boolean supportsMimeType(String mimeType);

  /** Decodes an image from compressed binary data. */
  ListenableFuture<Bitmap> decodeBitmap(byte[] data);

  /** Loads an image from {@code uri}. */
  ListenableFuture<Bitmap> loadBitmap(Uri uri);

  /**
   * Loads an image from {@link MediaMetadata}. Returns null if {@code metadata} doesn't contain
   * bitmap information.
   *
   * <p>By default, the method will try to decode an image from {@link MediaMetadata#artworkData} if
   * it is present. Otherwise, the method will try to load an image from {@link
   * MediaMetadata#artworkUri} if it is present. The method will return null if neither {@link
   * MediaMetadata#artworkData} nor {@link MediaMetadata#artworkUri} is present.
   */
  @Nullable
  default ListenableFuture<Bitmap> loadBitmapFromMetadata(MediaMetadata metadata) {
    @Nullable ListenableFuture<Bitmap> future;
    if (metadata.artworkData != null) {
      future = decodeBitmap(metadata.artworkData);
    } else if (metadata.artworkUri != null) {
      future = loadBitmap(metadata.artworkUri);
    } else {
      future = null;
    }
    return future;
  }
}
