/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.video;

import android.media.MediaCodec;
import android.view.Surface;
import androidx.annotation.Nullable;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.mediacodec.MediaCodecDecoderException;
import androidx.media3.exoplayer.mediacodec.MediaCodecInfo;

/** Thrown when a failure occurs in a {@link MediaCodec} video decoder. */
@UnstableApi
public class MediaCodecVideoDecoderException extends MediaCodecDecoderException {

  /** The {@link System#identityHashCode(Object)} of the surface when the exception occurred. */
  public final int surfaceIdentityHashCode;

  /** Whether the surface was valid when the exception occurred. */
  public final boolean isSurfaceValid;

  public MediaCodecVideoDecoderException(
      Throwable cause, @Nullable MediaCodecInfo codecInfo, @Nullable Surface surface) {
    super(cause, codecInfo);
    surfaceIdentityHashCode = System.identityHashCode(surface);
    isSurfaceValid = surface == null || surface.isValid();
  }
}
