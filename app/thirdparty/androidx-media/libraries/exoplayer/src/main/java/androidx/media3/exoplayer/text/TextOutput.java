/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.text;

import androidx.media3.common.text.Cue;
import androidx.media3.common.text.CueGroup;
import androidx.media3.common.util.UnstableApi;
import java.util.List;

/** Receives text output. */
@UnstableApi
public interface TextOutput {

  /**
   * Called when there is a change in the {@link Cue Cues}.
   *
   * <p>Both {@link #onCues(List)} and {@link #onCues(CueGroup)} are called when there is a change
   * in the cues. You should only implement one or the other.
   *
   * @deprecated Use {@link #onCues(CueGroup)} instead.
   */
  @Deprecated
  default void onCues(List<Cue> cues) {}

  /**
   * Called when there is a change in the {@link CueGroup}.
   *
   * <p>Both {@link #onCues(List)} and {@link #onCues(CueGroup)} are called when there is a change
   * in the cues. You should only implement one or the other.
   */
  void onCues(CueGroup cueGroup);
}
