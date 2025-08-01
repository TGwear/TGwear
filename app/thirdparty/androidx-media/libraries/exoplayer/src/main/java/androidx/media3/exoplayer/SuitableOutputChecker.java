/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer;

import static androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP;

import android.content.Context;
import android.os.Looper;
import androidx.annotation.RestrictTo;
import androidx.media3.common.util.Clock;
import androidx.media3.common.util.UnstableApi;

/** Provides methods to check the suitability of selected media outputs. */
@RestrictTo(LIBRARY_GROUP)
@UnstableApi
public interface SuitableOutputChecker {

  /** Callback to notify changes in the suitability of the selected media output. */
  interface Callback {

    /**
     * Called when suitability of the selected output has changed.
     *
     * @param isSelectedOutputSuitableForPlayback true when selected output is suitable for
     *     playback.
     */
    void onSelectedOutputSuitabilityChanged(boolean isSelectedOutputSuitableForPlayback);
  }

  /**
   * Enables the current instance to receive updates on the selected media outputs and sets the
   * {@link Callback} to notify the updates on the suitability of the selected output.
   *
   * <p>When the caller no longer requires updates on suitable outputs, they must call {@link
   * #disable()}.
   *
   * @param callback To receive notifications of changes in suitable media output changes.
   * @param context A {@link Context}.
   * @param callbackLooper The {@link Looper} to call {@link Callback} methods on.
   * @param backgroundLooper The {@link Looper} to run background operations on.
   * @param clock The {@link Clock}.
   */
  void enable(
      Callback callback,
      Context context,
      Looper callbackLooper,
      Looper backgroundLooper,
      Clock clock);

  /**
   * Disables the current instance to receive updates on the selected media outputs and clears the
   * {@link Callback}.
   *
   * @throws IllegalStateException if this instance is not enabled to receive the updates on
   *     suitable media outputs.
   */
  void disable();

  /**
   * Returns whether any audio output is suitable for the media playback.
   *
   * @throws IllegalStateException if this instance is not enabled to receive the updates on
   *     suitable media outputs.
   */
  boolean isSelectedOutputSuitableForPlayback();
}
