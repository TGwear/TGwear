/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.dash.manifest;

import androidx.media3.common.C;
import androidx.media3.common.util.UnstableApi;

/** Represents a service description element. */
@UnstableApi
public final class ServiceDescriptionElement {

  /** The target live offset in milliseconds, or {@link C#TIME_UNSET} if undefined. */
  public final long targetOffsetMs;

  /** The minimum live offset in milliseconds, or {@link C#TIME_UNSET} if undefined. */
  public final long minOffsetMs;

  /** The maximum live offset in milliseconds, or {@link C#TIME_UNSET} if undefined. */
  public final long maxOffsetMs;

  /**
   * The minimum factor by which playback can be sped up for live speed adjustment, or {@link
   * C#RATE_UNSET} if undefined.
   */
  public final float minPlaybackSpeed;

  /**
   * The maximum factor by which playback can be sped up for live speed adjustment, or {@link
   * C#RATE_UNSET} if undefined.
   */
  public final float maxPlaybackSpeed;

  /**
   * Creates a service description element.
   *
   * @param targetOffsetMs The target live offset in milliseconds, or {@link C#TIME_UNSET} if
   *     undefined.
   * @param minOffsetMs The minimum live offset in milliseconds, or {@link C#TIME_UNSET} if
   *     undefined.
   * @param maxOffsetMs The maximum live offset in milliseconds, or {@link C#TIME_UNSET} if
   *     undefined.
   * @param minPlaybackSpeed The minimum factor by which playback can be sped up for live speed
   *     adjustment, or {@link C#RATE_UNSET} if undefined.
   * @param maxPlaybackSpeed The maximum factor by which playback can be sped up for live speed
   *     adjustment, or {@link C#RATE_UNSET} if undefined.
   */
  public ServiceDescriptionElement(
      long targetOffsetMs,
      long minOffsetMs,
      long maxOffsetMs,
      float minPlaybackSpeed,
      float maxPlaybackSpeed) {
    this.targetOffsetMs = targetOffsetMs;
    this.minOffsetMs = minOffsetMs;
    this.maxOffsetMs = maxOffsetMs;
    this.minPlaybackSpeed = minPlaybackSpeed;
    this.maxPlaybackSpeed = maxPlaybackSpeed;
  }
}
