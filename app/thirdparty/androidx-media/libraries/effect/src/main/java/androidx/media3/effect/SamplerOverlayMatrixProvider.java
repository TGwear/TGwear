/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.effect;

import android.opengl.Matrix;
import androidx.media3.common.OverlaySettings;
import androidx.media3.common.util.GlUtil;
import androidx.media3.common.util.Size;

/**
 * Provides a matrix based on {@link OverlaySettings} to be applied on a texture sampling
 * coordinate.
 */
/* package */ final class SamplerOverlayMatrixProvider extends OverlayMatrixProvider {
  private final float[] transformationMatrixInv;

  public SamplerOverlayMatrixProvider() {
    super();
    transformationMatrixInv = GlUtil.create4x4IdentityMatrix();
  }

  @Override
  public float[] getTransformationMatrix(Size overlaySize, OverlaySettings overlaySettings) {
    // When sampling from a (for example, texture) sampler, the transformation matrix applied to a
    // sampler's coordinate should be the inverse of the transformation matrix that would otherwise
    // be applied to a vertex.
    Matrix.invertM(
        transformationMatrixInv,
        MATRIX_OFFSET,
        super.getTransformationMatrix(overlaySize, overlaySettings),
        MATRIX_OFFSET);
    return transformationMatrixInv;
  }
}
