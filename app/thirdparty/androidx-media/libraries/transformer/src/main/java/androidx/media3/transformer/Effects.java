/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.transformer;

import android.util.Pair;
import androidx.media3.common.Effect;
import androidx.media3.common.MediaItem;
import androidx.media3.common.audio.AudioProcessor;
import androidx.media3.common.audio.SpeedChangingAudioProcessor;
import androidx.media3.common.audio.SpeedProvider;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.effect.SpeedChangeEffect;
import androidx.media3.effect.TimestampAdjustment;
import com.google.common.collect.ImmutableList;
import java.util.List;

/** Effects to apply to a {@link MediaItem} or to a {@link Composition}. */
@UnstableApi
public final class Effects {

  /** An empty {@link Effects} instance. */
  public static final Effects EMPTY =
      new Effects(
          /* audioProcessors= */ ImmutableList.of(), /* videoEffects= */ ImmutableList.of());

  /**
   * The list of {@linkplain AudioProcessor audio processors} to apply to audio buffers. They are
   * applied in the order of the list, and buffers will only be modified by that {@link
   * AudioProcessor} if it {@link AudioProcessor#isActive()} based on the current configuration.
   */
  public final ImmutableList<AudioProcessor> audioProcessors;

  /**
   * The list of {@linkplain Effect video effects} to apply to each frame. They are applied in the
   * order of the list.
   */
  public final ImmutableList<Effect> videoEffects;

  /**
   * Creates an instance.
   *
   * @param audioProcessors The {@link #audioProcessors}.
   * @param videoEffects The {@link #videoEffects}.
   */
  public Effects(List<AudioProcessor> audioProcessors, List<Effect> videoEffects) {
    this.audioProcessors = ImmutableList.copyOf(audioProcessors);
    this.videoEffects = ImmutableList.copyOf(videoEffects);
  }

  /**
   * Creates an interlinked {@linkplain AudioProcessor audio processor} and {@linkplain Effect video
   * effect} that changes the speed to media samples in segments of the input file specified by the
   * given {@link SpeedProvider}.
   *
   * <p>The {@linkplain AudioProcessor audio processor} and {@linkplain Effect video effect} are
   * interlinked to help maintain A/V sync. When using Transformer, if the input file doesn't have
   * audio, or audio is being removed, you may have to {@linkplain
   * Composition.Builder#experimentalSetForceAudioTrack force an audio track} for the interlinked
   * effects to function correctly. Alternatively, you can use {@link SpeedChangeEffect} when input
   * has no audio.
   *
   * @param speedProvider The {@link SpeedProvider} determining the speed for the media at specific
   *     timestamps.
   */
  public static Pair<AudioProcessor, Effect> createExperimentalSpeedChangingEffect(
      SpeedProvider speedProvider) {
    SpeedChangingAudioProcessor speedChangingAudioProcessor =
        new SpeedChangingAudioProcessor(speedProvider);
    Effect audioDrivenVideoEffect =
        new TimestampAdjustment(
            speedChangingAudioProcessor::getSpeedAdjustedTimeAsync, speedProvider);
    return Pair.create(speedChangingAudioProcessor, audioDrivenVideoEffect);
  }
}
