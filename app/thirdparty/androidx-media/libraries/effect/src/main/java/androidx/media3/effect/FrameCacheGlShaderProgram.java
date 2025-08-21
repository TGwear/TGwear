/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.effect;

import android.content.Context;
import android.opengl.GLES20;
import androidx.annotation.CallSuper;
import androidx.media3.common.GlTextureInfo;
import androidx.media3.common.VideoFrameProcessingException;
import androidx.media3.common.util.GlProgram;
import androidx.media3.common.util.GlUtil;
import androidx.media3.common.util.Size;
import java.io.IOException;

/**
 * Manages a pool of {@linkplain GlTextureInfo textures}, and caches the input frame.
 *
 * <p>Implements {@link FrameCache}.
 */
/* package */ class FrameCacheGlShaderProgram extends BaseGlShaderProgram {
  private static final String VERTEX_SHADER_TRANSFORMATION_ES2_PATH =
      "shaders/vertex_shader_transformation_es2.glsl";
  private static final String FRAGMENT_SHADER_TRANSFORMATION_ES2_PATH =
      "shaders/fragment_shader_transformation_es2.glsl";

  private final GlProgram copyProgram;

  /** Creates a new instance. */
  public FrameCacheGlShaderProgram(Context context, int capacity, boolean useHdr)
      throws VideoFrameProcessingException {
    super(/* useHighPrecisionColorComponents= */ useHdr, capacity);

    try {
      this.copyProgram =
          new GlProgram(
              context,
              VERTEX_SHADER_TRANSFORMATION_ES2_PATH,
              FRAGMENT_SHADER_TRANSFORMATION_ES2_PATH);
    } catch (IOException | GlUtil.GlException e) {
      throw VideoFrameProcessingException.from(e);
    }

    float[] identityMatrix = GlUtil.create4x4IdentityMatrix();
    copyProgram.setFloatsUniform("uTexTransformationMatrix", identityMatrix);
    copyProgram.setFloatsUniform("uTransformationMatrix", identityMatrix);
    copyProgram.setFloatsUniform("uRgbMatrix", identityMatrix);
    copyProgram.setBufferAttribute(
        "aFramePosition",
        GlUtil.getNormalizedCoordinateBounds(),
        GlUtil.HOMOGENEOUS_COORDINATE_VECTOR_SIZE);
  }

  @Override
  public Size configure(int inputWidth, int inputHeight) {
    return new Size(inputWidth, inputHeight);
  }

  @Override
  public void drawFrame(int inputTexId, long presentationTimeUs)
      throws VideoFrameProcessingException {
    try {
      copyProgram.use();
      copyProgram.setSamplerTexIdUniform("uTexSampler", inputTexId, /* texUnitIndex= */ 0);
      copyProgram.bindAttributesAndUniforms();
      GLES20.glDrawArrays(
          GLES20.GL_TRIANGLE_STRIP,
          /* first= */ 0,
          /* count= */ GlUtil.HOMOGENEOUS_COORDINATE_VECTOR_SIZE);
    } catch (GlUtil.GlException e) {
      throw VideoFrameProcessingException.from(e);
    }
  }

  @Override
  @CallSuper
  public void release() throws VideoFrameProcessingException {
    super.release();
    try {
      copyProgram.delete();
    } catch (GlUtil.GlException e) {
      throw new VideoFrameProcessingException(e);
    }
  }
}
