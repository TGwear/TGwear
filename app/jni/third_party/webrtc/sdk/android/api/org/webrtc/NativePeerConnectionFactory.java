/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

/** Factory for creating webrtc::jni::OwnedPeerConnection instances. */
public interface NativePeerConnectionFactory {
  /**
   * Create a new webrtc::jni::OwnedPeerConnection instance and returns a pointer to it.
   * The caller takes ownership of the object.
   */
  long createNativePeerConnection();
}
