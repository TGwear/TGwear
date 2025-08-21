/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.effect;

/**
 * Interface for a {@link GlShaderProgram} that samples from an external texture.
 *
 * <p>Use {@link #setTextureTransformMatrix(float[])} to provide the texture's transformation
 * matrix.
 */
/* package */ interface ExternalShaderProgram extends GlShaderProgram {

  /**
   * Sets the texture transform matrix for converting an external surface texture's coordinates to
   * sampling locations.
   *
   * @param textureTransformMatrix The external surface texture's {@linkplain
   *     android.graphics.SurfaceTexture#getTransformMatrix(float[]) transform matrix}.
   */
  void setTextureTransformMatrix(float[] textureTransformMatrix);
}
