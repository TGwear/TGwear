/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

/**
 * Implementations of this interface can create a native {@code webrtc::AudioDecoderFactory}.
 */
public interface AudioDecoderFactoryFactory {
  /**
   * Returns a pointer to a {@code webrtc::AudioDecoderFactory}. The caller takes ownership.
   */
  long createNativeAudioDecoderFactory();
}
