/*
 * Copyright (c) 2015-2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc.audio;

import android.media.AudioManager;
import androidx.annotation.Nullable;
import java.util.Timer;
import java.util.TimerTask;
import org.webrtc.Logging;

// TODO(magjed): Do we really need to spawn a new thread just to log volume? Can we re-use the
// AudioTrackThread instead?
/**
 * Private utility class that periodically checks and logs the volume level of the audio stream that
 * is currently controlled by the volume control. A timer triggers logs once every 30 seconds and
 * the timer's associated thread is named "WebRtcVolumeLevelLoggerThread".
 */
class VolumeLogger {
  private static final String TAG = "VolumeLogger";
  private static final String THREAD_NAME = "WebRtcVolumeLevelLoggerThread";
  private static final int TIMER_PERIOD_IN_SECONDS = 30;

  private final AudioManager audioManager;
  private @Nullable Timer timer;

  public VolumeLogger(AudioManager audioManager) {
    this.audioManager = audioManager;
  }

  public void start() {
    Logging.d(TAG, "start" + WebRtcAudioUtils.getThreadInfo());
    if (timer != null) {
      return;
    }
    Logging.d(TAG, "audio mode is: " + WebRtcAudioUtils.modeToString(audioManager.getMode()));

    timer = new Timer(THREAD_NAME);
    timer.schedule(new LogVolumeTask(audioManager.getStreamMaxVolume(AudioManager.STREAM_RING),
                       audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL)),
        0, TIMER_PERIOD_IN_SECONDS * 1000);
  }

  private class LogVolumeTask extends TimerTask {
    private final int maxRingVolume;
    private final int maxVoiceCallVolume;

    LogVolumeTask(int maxRingVolume, int maxVoiceCallVolume) {
      this.maxRingVolume = maxRingVolume;
      this.maxVoiceCallVolume = maxVoiceCallVolume;
    }

    @Override
    public void run() {
      final int mode = audioManager.getMode();
      if (mode == AudioManager.MODE_RINGTONE) {
        Logging.d(TAG,
            "STREAM_RING stream volume: " + audioManager.getStreamVolume(AudioManager.STREAM_RING)
                + " (max=" + maxRingVolume + ")");
      } else if (mode == AudioManager.MODE_IN_COMMUNICATION) {
        Logging.d(TAG,
            "VOICE_CALL stream volume: "
                + audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL)
                + " (max=" + maxVoiceCallVolume + ")");
      }
    }
  }

  public void stop() {
    Logging.d(TAG, "stop" + WebRtcAudioUtils.getThreadInfo());
    if (timer != null) {
      timer.cancel();
      timer = null;
    }
  }
}
