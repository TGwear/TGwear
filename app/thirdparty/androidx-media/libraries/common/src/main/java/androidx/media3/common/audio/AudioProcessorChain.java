/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common.audio;

import androidx.media3.common.PlaybackParameters;
import androidx.media3.common.util.UnstableApi;

/**
 * Provides a chain of audio processors, which are used for any user-defined processing and applying
 * playback parameters (if supported). Because applying playback parameters can skip and
 * stretch/compress audio, the sink will query the chain for information on how to transform its
 * output position to map it onto a media position, via {@link #getMediaDuration(long)} and {@link
 * #getSkippedOutputFrameCount()}.
 */
@UnstableApi
public interface AudioProcessorChain {

  /**
   * Returns the fixed chain of audio processors that will process audio. This method is called once
   * during initialization, but audio processors may change state to become active/inactive during
   * playback.
   */
  AudioProcessor[] getAudioProcessors();

  /**
   * Configures audio processors to apply the specified playback parameters immediately, returning
   * the new playback parameters, which may differ from those passed in. Only called when processors
   * have no input pending.
   *
   * @param playbackParameters The playback parameters to try to apply.
   * @return The playback parameters that were actually applied.
   */
  PlaybackParameters applyPlaybackParameters(PlaybackParameters playbackParameters);

  /**
   * Configures audio processors to apply whether to skip silences immediately, returning the new
   * value. Only called when processors have no input pending.
   *
   * @param skipSilenceEnabled Whether silences should be skipped in the audio stream.
   * @return The new value.
   */
  boolean applySkipSilenceEnabled(boolean skipSilenceEnabled);

  /**
   * Returns the media duration corresponding to the specified playout duration, taking speed
   * adjustment due to audio processing into account.
   *
   * <p>The scaling performed by this method will use the actual playback speed achieved by the
   * audio processor chain, on average, since it was last flushed. This may differ very slightly
   * from the target playback speed.
   *
   * @param playoutDuration The playout duration to scale.
   * @return The corresponding media duration, in the same units as {@code duration}.
   */
  long getMediaDuration(long playoutDuration);

  /**
   * Returns the number of output audio frames skipped since the audio processors were last flushed.
   */
  long getSkippedOutputFrameCount();
}
