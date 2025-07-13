/*
 * Copyright (c) 2017-2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

class NativeLibrary {
  private static String TAG = "NativeLibrary";

  static class DefaultLoader implements NativeLibraryLoader {
    @Override
    public boolean load(String name) {
      Logging.d(TAG, "Loading library: " + name);
      System.loadLibrary(name);

      // Not relevant, but kept for API compatibility.
      return true;
    }
  }

  private static Object lock = new Object();
  private static boolean libraryLoaded;

  /**
   * Loads the native library. Clients should call PeerConnectionFactory.initialize. It will call
   * this method for them.
   */
  static void initialize(NativeLibraryLoader loader, String libraryName) {
    synchronized (lock) {
      if (libraryLoaded) {
        Logging.d(TAG, "Native library has already been loaded.");
        return;
      }
      Logging.d(TAG, "Loading native library: " + libraryName);
      libraryLoaded = loader.load(libraryName);
    }
  }

  /** Returns true if the library has been loaded successfully. */
  static boolean isLoaded() {
    synchronized (lock) {
      return libraryLoaded;
    }
  }
}
