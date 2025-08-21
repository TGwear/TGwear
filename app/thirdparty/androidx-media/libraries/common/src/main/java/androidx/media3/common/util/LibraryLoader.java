/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common.util;

import java.util.Arrays;

/** Configurable loader for native libraries. */
@UnstableApi
public abstract class LibraryLoader {

  private static final String TAG = "LibraryLoader";

  private String[] nativeLibraries;
  private boolean loadAttempted;
  private boolean isAvailable;

  /**
   * @param libraries The names of the libraries to load.
   */
  public LibraryLoader(String... libraries) {
    nativeLibraries = libraries;
  }

  /**
   * Overrides the names of the libraries to load. Must be called before any call to {@link
   * #isAvailable()}.
   */
  public synchronized void setLibraries(String... libraries) {
    Assertions.checkState(!loadAttempted, "Cannot set libraries after loading");
    nativeLibraries = libraries;
  }

  /** Returns whether the underlying libraries are available, loading them if necessary. */
  public synchronized boolean isAvailable() {
    if (loadAttempted) {
      return isAvailable;
    }
    loadAttempted = true;
    try {
      for (String lib : nativeLibraries) {
        loadLibrary(lib);
      }
      isAvailable = true;
    } catch (UnsatisfiedLinkError exception) {
      // Log a warning as an attempt to check for the library indicates that the app depends on an
      // extension and generally would expect its native libraries to be available.
      Log.w(TAG, "Failed to load " + Arrays.toString(nativeLibraries));
    }
    return isAvailable;
  }

  /**
   * Should be implemented to call {@code System.loadLibrary(name)}.
   *
   * <p>It's necessary for each subclass to implement this method because {@link
   * System#loadLibrary(String)} uses reflection to obtain the calling class, which is then used to
   * obtain the class loader to use when loading the native library. If this class were to implement
   * the method directly, and if a subclass were to have a different class loader, then loading of
   * the native library would fail.
   *
   * @param name The name of the library to load.
   */
  protected abstract void loadLibrary(String name);
}
