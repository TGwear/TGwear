/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * This class is only used from jni_helper.cc to give some Java functionality that were not possible
 * to generate in other ways due to bugs.webrtc.org/8606 and bugs.webrtc.org/8632.
 */
class JniHelper {
  // TODO(bugs.webrtc.org/8632): Remove.
  @CalledByNative
  static byte[] getStringBytes(String s) {
    try {
      return s.getBytes("ISO-8859-1");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("ISO-8859-1 is unsupported");
    }
  }

  // TODO(bugs.webrtc.org/8632): Remove.
  @CalledByNative
  static Object getStringClass() {
    return String.class;
  }

  // TODO(bugs.webrtc.org/8606): Remove.
  @CalledByNative
  static Object getKey(Map.Entry entry) {
    return entry.getKey();
  }

  // TODO(bugs.webrtc.org/8606): Remove.
  @CalledByNative
  static Object getValue(Map.Entry entry) {
    return entry.getValue();
  }
}
