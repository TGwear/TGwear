/*
 * Copyright (c) 2017-2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

/**
 * Interface for loading native libraries. A custom loader can be passed to
 * PeerConnectionFactory.initialize.
 */
public interface NativeLibraryLoader {
  /**
   * Loads a native library with the given name.
   *
   * @return True on success
   */
  boolean load(String name);
}
