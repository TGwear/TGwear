/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.audio;

import android.media.AudioDeviceInfo;
import androidx.annotation.RequiresApi;

/** Wrapper class for the platform {@link AudioDeviceInfo}. */
@RequiresApi(23)
/* package */ final class AudioDeviceInfoApi23 {

  /** The platform {@link AudioDeviceInfo}. */
  public final AudioDeviceInfo audioDeviceInfo;

  /**
   * Creates the audio device info wrapper.
   *
   * @param audioDeviceInfo The platform {@link AudioDeviceInfo}.
   */
  public AudioDeviceInfoApi23(AudioDeviceInfo audioDeviceInfo) {
    this.audioDeviceInfo = audioDeviceInfo;
  }
}
