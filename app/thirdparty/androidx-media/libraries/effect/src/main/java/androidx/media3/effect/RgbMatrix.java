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
import androidx.media3.common.util.UnstableApi;
import com.google.common.collect.ImmutableList;

/**
 * Specifies a 4x4 RGB color transformation matrix to apply to each frame in the fragment shader.
 */
@UnstableApi
public interface RgbMatrix extends GlEffect {

  /**
   * Returns the 4x4 RGB transformation {@linkplain android.opengl.Matrix matrix} to apply to the
   * color values of each pixel in the frame with the given timestamp.
   *
   * @param presentationTimeUs The timestamp of the frame to apply the matrix on.
   * @param useHdr If {@code true}, colors will be in linear RGB BT.2020. If {@code false}, colors
   *     will be in linear RGB BT.709. Must be consistent with {@code useHdr} in {@link
   *     #toGlShaderProgram(Context, boolean)}.
   * @return The {@code RgbMatrix} to apply to the frame.
   */
  float[] getMatrix(long presentationTimeUs, boolean useHdr);

  @Override
  default BaseGlShaderProgram toGlShaderProgram(Context context, boolean useHdr)
      throws VideoFrameProcessingException {
    return DefaultShaderProgram.create(
        context,
        /* matrixTransformations= */ ImmutableList.of(),
        /* rgbMatrices= */ ImmutableList.of(this),
        useHdr);
  }
}
