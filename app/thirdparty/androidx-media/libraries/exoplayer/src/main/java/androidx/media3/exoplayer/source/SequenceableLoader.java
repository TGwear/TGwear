/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.source;

import androidx.media3.common.C;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.LoadingInfo;

// TODO: Clarify the requirements for implementing this interface [Internal ref: b/36250203].
/** A loader that can proceed in approximate synchronization with other loaders. */
@UnstableApi
public interface SequenceableLoader {

  /** A callback to be notified of {@link SequenceableLoader} events. */
  interface Callback<T extends SequenceableLoader> {

    /**
     * Called by the loader to indicate that it wishes for its {@link #continueLoading(LoadingInfo)}
     * method to be called when it can continue to load data. Called on the playback thread.
     */
    void onContinueLoadingRequested(T source);
  }

  /**
   * Returns an estimate of the position up to which data is buffered.
   *
   * @return An estimate of the absolute position in microseconds up to which data is buffered, or
   *     {@link C#TIME_END_OF_SOURCE} if the data is fully buffered.
   */
  long getBufferedPositionUs();

  /** Returns the next load time, or {@link C#TIME_END_OF_SOURCE} if loading has finished. */
  long getNextLoadPositionUs();

  /**
   * Attempts to continue loading.
   *
   * @param loadingInfo The {@link LoadingInfo} when attempting to continue loading.
   * @return True if progress was made, meaning that {@link #getNextLoadPositionUs()} will return a
   *     different value than prior to the call. False otherwise.
   */
  boolean continueLoading(LoadingInfo loadingInfo);

  /** Returns whether the loader is currently loading. */
  boolean isLoading();

  /**
   * Re-evaluates the buffer given the playback position.
   *
   * <p>Re-evaluation may discard buffered media or cancel ongoing loads so that media can be
   * re-buffered in a different quality.
   *
   * @param positionUs The current playback position in microseconds. If playback of this period has
   *     not yet started, the value will be the starting position in this period minus the duration
   *     of any media in previous periods still to be played.
   */
  void reevaluateBuffer(long positionUs);
}
