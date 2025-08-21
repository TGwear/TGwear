/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc.audio;

/**
 * This interface is a thin wrapper on top of a native C++ webrtc::AudioDeviceModule (ADM). The
 * reason for basing it on a native ADM instead of a pure Java interface is that we have two native
 * Android implementations (OpenSLES and AAudio) that does not make sense to wrap through JNI.
 *
 * <p>Note: This class is still under development and may change without notice.
 */
public interface AudioDeviceModule {
  /**
   * Returns a C++ pointer to a webrtc::AudioDeviceModule. Caller does _not_ take ownership and
   * lifetime is handled through the release() call.
   */
  long getNativeAudioDeviceModulePointer();

  /**
   * Release resources for this AudioDeviceModule, including native resources. The object should not
   * be used after this call.
   */
  void release();

  /** Control muting/unmuting the speaker. */
  void setSpeakerMute(boolean mute);

  /** Control muting/unmuting the microphone. */
  void setMicrophoneMute(boolean mute);

  /**
   * Enable or disable built in noise suppressor. Returns true if the enabling was successful,
   * otherwise false is returned.
   */
  default boolean setNoiseSuppressorEnabled(boolean enabled) {
    return false;
  }

  /**
   * Sets the preferred field dimension for the built-in microphone. Returns
   * true if setting was successful, otherwise false is returned.
   * This functionality can be implemented with
   * {@code android.media.MicrophoneDirection.setPreferredMicrophoneFieldDimension}.
   */
  default boolean setPreferredMicrophoneFieldDimension(float dimension) {
    return false;
  }
}
