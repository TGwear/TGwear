/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

/**
 * This class provides a ClassLoader that is capable of loading WebRTC Java classes regardless of
 * what thread it's called from. Such a ClassLoader is needed for the few cases where the JNI
 * mechanism is unable to automatically determine the appropriate ClassLoader instance.
 */
class WebRtcClassLoader {
  @CalledByNative
  static Object getClassLoader() {
    Object loader = WebRtcClassLoader.class.getClassLoader();
    if (loader == null) {
      throw new RuntimeException("Failed to get WebRTC class loader.");
    }
    return loader;
  }
}
