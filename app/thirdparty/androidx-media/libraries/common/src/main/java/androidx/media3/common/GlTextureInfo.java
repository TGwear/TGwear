/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common;

import androidx.media3.common.util.GlUtil;
import androidx.media3.common.util.UnstableApi;

/** Contains information describing an OpenGL texture. */
@UnstableApi
public final class GlTextureInfo {
  /** A {@link GlTextureInfo} instance with all fields unset. */
  public static final GlTextureInfo UNSET =
      new GlTextureInfo(
          /* texId= */ C.INDEX_UNSET,
          /* fboId= */ C.INDEX_UNSET,
          /* rboId= */ C.INDEX_UNSET,
          /* width= */ C.LENGTH_UNSET,
          /* height= */ C.LENGTH_UNSET);

  /** The OpenGL texture identifier, or {@link C#INDEX_UNSET} if not specified. */
  public final int texId;

  /**
   * Identifier of a framebuffer object associated with the texture, or {@link C#INDEX_UNSET} if not
   * specified.
   */
  public final int fboId;

  /**
   * Identifier of a renderbuffer object attached with the framebuffer, or {@link C#INDEX_UNSET} if
   * not specified.
   */
  public final int rboId;

  /** The width of the texture, in pixels, or {@link C#LENGTH_UNSET} if not specified. */
  public final int width;

  /** The height of the texture, in pixels, or {@link C#LENGTH_UNSET} if not specified. */
  public final int height;

  /**
   * Creates a new instance.
   *
   * @param texId The OpenGL texture identifier, or {@link C#INDEX_UNSET} if not specified.
   * @param fboId Identifier of a framebuffer object associated with the texture, or {@link
   *     C#INDEX_UNSET} if not specified.
   * @param rboId Identifier of a renderbuffer object associated with the texture, or {@link
   *     C#INDEX_UNSET} if not specified.
   * @param width The width of the texture, in pixels, or {@link C#LENGTH_UNSET} if not specified.
   * @param height The height of the texture, in pixels, or {@link C#LENGTH_UNSET} if not specified.
   */
  public GlTextureInfo(int texId, int fboId, int rboId, int width, int height) {
    this.texId = texId;
    this.fboId = fboId;
    this.rboId = rboId;
    this.width = width;
    this.height = height;
  }

  /** Releases all information associated with this instance. */
  public void release() throws GlUtil.GlException {
    if (texId != C.INDEX_UNSET) {
      GlUtil.deleteTexture(texId);
    }
    if (fboId != C.INDEX_UNSET) {
      GlUtil.deleteFbo(fboId);
    }
    if (rboId != C.INDEX_UNSET) {
      GlUtil.deleteRbo(rboId);
    }
  }
}
