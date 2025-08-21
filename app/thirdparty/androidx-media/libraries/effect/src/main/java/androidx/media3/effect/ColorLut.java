/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package androidx.media3.effect;

import android.content.Context;
import androidx.media3.common.VideoFrameProcessingException;
import androidx.media3.common.util.GlUtil;
import androidx.media3.common.util.UnstableApi;

/**
 * Specifies color transformations using color lookup tables to apply to each frame in the fragment
 * shader.
 */
@UnstableApi
public interface ColorLut extends GlEffect {

  /**
   * Returns the OpenGL texture ID of the LUT to apply to the pixels of the frame with the given
   * timestamp.
   */
  int getLutTextureId(long presentationTimeUs);

  /** Returns the length N of the 3D N x N x N LUT cube with the given timestamp. */
  int getLength(long presentationTimeUs);

  /** Releases the OpenGL texture of the LUT. */
  void release() throws GlUtil.GlException;

  @Override
  default GlShaderProgram toGlShaderProgram(Context context, boolean useHdr)
      throws VideoFrameProcessingException {
    return new ColorLutShaderProgram(context, /* colorLut= */ this, useHdr);
  }
}
