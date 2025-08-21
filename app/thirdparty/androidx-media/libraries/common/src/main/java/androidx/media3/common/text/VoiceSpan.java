/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common.text;

import static androidx.media3.common.util.Assertions.checkNotNull;

import android.os.Bundle;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.common.util.Util;

/**
 * A span representing the speaker of the spanned text.
 *
 * <p>For example a <a href="https://www.w3.org/TR/webvtt1/#webvtt-cue-voice-span">WebVTT voice
 * span</a>.
 */
@UnstableApi
public final class VoiceSpan {

  /** The voice name. */
  public final String name;

  private static final String FIELD_NAME = Util.intToStringMaxRadix(0);

  public VoiceSpan(String name) {
    this.name = name;
  }

  public Bundle toBundle() {
    Bundle bundle = new Bundle();
    bundle.putString(FIELD_NAME, name);
    return bundle;
  }

  public static VoiceSpan fromBundle(Bundle bundle) {
    return new VoiceSpan(checkNotNull(bundle.getString(FIELD_NAME)));
  }
}
