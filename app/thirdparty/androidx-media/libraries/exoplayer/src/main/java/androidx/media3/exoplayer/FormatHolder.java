/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer;

import androidx.annotation.Nullable;
import androidx.media3.common.Format;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.drm.DrmSession;

/** Holds a {@link Format}. */
@UnstableApi
public final class FormatHolder {

  /** An accompanying context for decrypting samples in the format. */
  @Nullable public DrmSession drmSession;

  /** The held {@link Format}. */
  @Nullable public Format format;

  /** Clears the holder. */
  public void clear() {
    drmSession = null;
    format = null;
  }
}
