/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.effect;

import androidx.media3.common.OverlaySettings;
import androidx.media3.common.VideoFrameProcessingException;
import androidx.media3.common.util.GlUtil;
import androidx.media3.common.util.Size;
import androidx.media3.common.util.UnstableApi;

/** Creates overlays from OpenGL textures. */
@UnstableApi
public abstract class TextureOverlay {
  private static final float[] IDENTITY_MATRIX = GlUtil.create4x4IdentityMatrix();

  /**
   * Returns the overlay texture identifier displayed at the specified timestamp.
   *
   * @param presentationTimeUs The presentation timestamp of the current frame, in microseconds.
   * @throws VideoFrameProcessingException If an error occurs while processing or drawing the frame.
   */
  public abstract int getTextureId(long presentationTimeUs) throws VideoFrameProcessingException;

  // This method is required to find the size of a texture given a texture identifier using OpenGL
  // ES 2.0. OpenGL ES 3.1 can do this with glGetTexLevelParameteriv().
  /**
   * Returns the pixel width and height of the overlay texture displayed at the specified timestamp.
   *
   * <p>This method must be called after {@link #getTextureId(long)}.
   *
   * @param presentationTimeUs The presentation timestamp of the current frame, in microseconds.
   */
  public abstract Size getTextureSize(long presentationTimeUs);

  /**
   * Returns a 4x4 OpenGL matrix, controlling how the vertices of the overlay are displayed at the
   * specified timestamp.
   *
   * <p>Applied before {@linkplain #getOverlaySettings overlay settings}.
   *
   * @param presentationTimeUs The presentation timestamp of the current frame, in microseconds.
   */
  public float[] getVertexTransformation(long presentationTimeUs) {
    return IDENTITY_MATRIX;
  }

  /**
   * Set up resources for the overlay given the input video’s dimensions.
   *
   * <p>This method will be called before drawing the first frame and before drawing subsequent
   * frames with different input dimensions.
   *
   * @param videoSize The width and height of the input video, in pixels.
   */
  public void configure(Size videoSize) {}

  /**
   * Returns the {@link OverlaySettings} controlling how the overlay is displayed at the specified
   * timestamp.
   *
   * @param presentationTimeUs The presentation timestamp of the current frame, in microseconds.
   */
  public OverlaySettings getOverlaySettings(long presentationTimeUs) {
    return new OverlaySettings() {};
  }

  /**
   * Releases all resources.
   *
   * @throws VideoFrameProcessingException If an error occurs while releasing resources.
   */
  public void release() throws VideoFrameProcessingException {}
}
