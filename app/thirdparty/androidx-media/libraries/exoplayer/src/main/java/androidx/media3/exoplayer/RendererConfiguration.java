/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer;

import static androidx.media3.exoplayer.audio.AudioSink.OFFLOAD_MODE_DISABLED;

import androidx.annotation.Nullable;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.audio.AudioSink;

/** The configuration of a {@link Renderer}. */
@UnstableApi
public final class RendererConfiguration {

  /** The default configuration. */
  public static final RendererConfiguration DEFAULT =
      new RendererConfiguration(
          /* offloadModePreferred= */ OFFLOAD_MODE_DISABLED, /* tunneling= */ false);

  /** The offload mode preference with which to configure the renderer. */
  public final @AudioSink.OffloadMode int offloadModePreferred;

  /** Whether to enable tunneling. */
  public final boolean tunneling;

  /**
   * Creates an instance with {@code tunneling} and sets {@link #offloadModePreferred} to {@link
   * AudioSink#OFFLOAD_MODE_DISABLED}.
   *
   * @param tunneling Whether to enable tunneling.
   */
  public RendererConfiguration(boolean tunneling) {
    this.offloadModePreferred = OFFLOAD_MODE_DISABLED;
    this.tunneling = tunneling;
  }

  /**
   * Creates an instance.
   *
   * @param offloadModePreferred The offload mode to use.
   * @param tunneling Whether to enable tunneling.
   */
  public RendererConfiguration(@AudioSink.OffloadMode int offloadModePreferred, boolean tunneling) {
    this.offloadModePreferred = offloadModePreferred;
    this.tunneling = tunneling;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    RendererConfiguration other = (RendererConfiguration) obj;
    return offloadModePreferred == other.offloadModePreferred && tunneling == other.tunneling;
  }

  @Override
  public int hashCode() {
    int hashCode = offloadModePreferred << 1;
    hashCode += (tunneling ? 1 : 0);
    return hashCode;
  }
}
