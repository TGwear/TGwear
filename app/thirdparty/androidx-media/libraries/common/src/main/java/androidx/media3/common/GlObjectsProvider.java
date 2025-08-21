/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package androidx.media3.common;

import android.opengl.EGL14;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import androidx.annotation.IntRange;
import androidx.media3.common.util.GlUtil.GlException;
import androidx.media3.common.util.UnstableApi;

// TODO(b/271433904): Expand this class to cover more methods in GlUtil.
/** Provider to customize the creation and maintenance of GL objects. */
@UnstableApi
public interface GlObjectsProvider {

  /**
   * Creates a new {@link EGLContext} for the specified {@link EGLDisplay}.
   *
   * @param eglDisplay The {@link EGLDisplay} to create an {@link EGLContext} for.
   * @param openGlVersion The version of OpenGL ES to configure. Accepts either {@code 2}, for
   *     OpenGL ES 2.0, or {@code 3}, for OpenGL ES 3.0.
   * @param configAttributes The attributes to configure EGL with.
   * @throws GlException If an error occurs during creation.
   */
  EGLContext createEglContext(
      EGLDisplay eglDisplay, @IntRange(from = 2, to = 3) int openGlVersion, int[] configAttributes)
      throws GlException;

  /**
   * Creates a new {@link EGLSurface} wrapping the specified {@code surface}.
   *
   * @param eglDisplay The {@link EGLDisplay} to attach the surface to.
   * @param surface The surface to wrap; must be a surface, surface texture or surface holder.
   * @param colorTransfer The {@linkplain C.ColorTransfer color transfer characteristics} to which
   *     the {@code surface} is configured.
   * @param isEncoderInputSurface Whether the {@code surface} is the input surface of an encoder.
   * @throws GlException If an error occurs during creation.
   */
  EGLSurface createEglSurface(
      EGLDisplay eglDisplay,
      Object surface,
      @C.ColorTransfer int colorTransfer,
      boolean isEncoderInputSurface)
      throws GlException;

  /**
   * Creates and focuses a placeholder {@link EGLSurface}.
   *
   * @param eglContext The {@link EGLContext} to make current.
   * @param eglDisplay The {@link EGLDisplay} to attach the surface to.
   * @return A placeholder {@link EGLSurface} that has been focused to allow rendering to take
   *     place, or {@link EGL14#EGL_NO_SURFACE} if the current context supports rendering without a
   *     surface.
   * @throws GlException If an error occurs during creation.
   */
  EGLSurface createFocusedPlaceholderEglSurface(EGLContext eglContext, EGLDisplay eglDisplay)
      throws GlException;

  /**
   * Returns a {@link GlTextureInfo} containing the identifiers of the newly created buffers.
   *
   * @param texId The identifier of the texture to attach to the buffers.
   * @param width The width of the texture in pixels.
   * @param height The height of the texture in pixels.
   * @throws GlException If an error occurs during creation.
   */
  GlTextureInfo createBuffersForTexture(int texId, int width, int height) throws GlException;

  /**
   * Releases the created objects.
   *
   * @param eglDisplay The {@link EGLDisplay} to release the objects for.
   */
  void release(EGLDisplay eglDisplay) throws GlException;
}
