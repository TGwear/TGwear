/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

/**
 * The FrameEncryptor interface allows Java API users to provide a pointer to
 * their native implementation of the FrameEncryptorInterface.
 * FrameEncyptors are extremely performance sensitive as they must process all
 * outgoing video and audio frames. Due to this reason they should always be
 * backed by a native implementation.
 * @note Not ready for production use.
 */
public interface FrameEncryptor {
  /**
   * @return A FrameEncryptorInterface pointer.
   */
  long getNativeFrameEncryptor();
}
