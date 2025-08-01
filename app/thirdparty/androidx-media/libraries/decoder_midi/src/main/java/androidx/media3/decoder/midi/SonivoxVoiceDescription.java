/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.decoder.midi;

import android.content.Context;
import com.jsyn.unitgen.UnitVoice;
import com.jsyn.util.VoiceDescription;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

/** Synthesizer voice description, used for obtaining {@link SonivoxSynthVoice} instances. */
/* package */ final class SonivoxVoiceDescription extends VoiceDescription {
  private static final String VOICE_CLASS_NAME = "SonivoxVoiceDescription";
  private static final String[] tags = {"wavetable", "GM2", "ringtone"};

  private static final Object LOCK = new Object();
  private static @MonotonicNonNull SonivoxVoiceDescription instance;

  public static SonivoxVoiceDescription getInstance(Context context) throws MidiDecoderException {
    synchronized (LOCK) {
      if (instance == null) {
        instance = new SonivoxVoiceDescription(SonivoxWaveData.loadWaveTableData(context));
      }
      return instance;
    }
  }

  private final short[] waveTableData;

  private SonivoxVoiceDescription(short[] waveTableData) {
    super(VOICE_CLASS_NAME, SonivoxWaveData.getProgramNames());
    this.waveTableData = waveTableData;
  }

  @Override
  public UnitVoice createUnitVoice() {
    // We must return a new instance every time.
    return new SonivoxSynthVoice(waveTableData);
  }

  @Override
  public String[] getTags(int presetIndex) {
    return tags;
  }

  @Override
  public String getVoiceClassName() {
    return VOICE_CLASS_NAME;
  }
}
