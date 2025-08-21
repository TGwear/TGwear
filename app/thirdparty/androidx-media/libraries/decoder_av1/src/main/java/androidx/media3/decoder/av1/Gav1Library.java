/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.decoder.av1;

import androidx.media3.common.MediaLibraryInfo;
import androidx.media3.common.util.LibraryLoader;
import androidx.media3.common.util.UnstableApi;

/** Configures and queries the underlying native library. */
@UnstableApi
public final class Gav1Library {

  static {
    MediaLibraryInfo.registerModule("media3.decoder.av1");
  }

  private static final LibraryLoader LOADER =
      new LibraryLoader("gav1JNI") {
        @Override
        protected void loadLibrary(String name) {
          System.loadLibrary(name);
        }
      };

  private Gav1Library() {}

  /** Returns whether the underlying library is available, loading it if necessary. */
  public static boolean isAvailable() {
    return LOADER.isAvailable();
  }
}
