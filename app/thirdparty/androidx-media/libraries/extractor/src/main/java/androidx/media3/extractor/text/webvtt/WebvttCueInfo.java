/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.text.webvtt;

import androidx.media3.common.text.Cue;
import androidx.media3.common.util.UnstableApi;

/** A representation of a WebVTT cue. */
@UnstableApi
public final class WebvttCueInfo {

  public final Cue cue;
  public final long startTimeUs;
  public final long endTimeUs;

  public WebvttCueInfo(Cue cue, long startTimeUs, long endTimeUs) {
    this.cue = cue;
    this.startTimeUs = startTimeUs;
    this.endTimeUs = endTimeUs;
  }
}
