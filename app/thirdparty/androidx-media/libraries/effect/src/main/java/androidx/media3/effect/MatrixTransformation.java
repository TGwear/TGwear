/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.effect;

import android.graphics.Matrix;
import androidx.media3.common.util.UnstableApi;

/**
 * Specifies a 3x3 transformation {@link Matrix} to apply in the vertex shader for each frame.
 *
 * <p>The matrix is applied to points given in normalized device coordinates (-1 to 1 on x and y
 * axes). Transformed pixels that are moved outside of the normal device coordinate range are
 * clipped.
 *
 * <p>Output frame pixels outside of the transformed input frame will be black, with alpha = 0 if
 * applicable.
 */
@UnstableApi
public interface MatrixTransformation extends GlMatrixTransformation {
  /**
   * Returns the 3x3 transformation {@link Matrix} to apply to the frame with the given timestamp.
   */
  Matrix getMatrix(long presentationTimeUs);

  @Override
  default float[] getGlMatrixArray(long presentationTimeUs) {
    return MatrixUtils.getGlMatrixArray(getMatrix(presentationTimeUs));
  }
}
