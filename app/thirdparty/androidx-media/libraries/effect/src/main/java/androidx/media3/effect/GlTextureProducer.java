/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package androidx.media3.effect;

import android.opengl.GLES30;
import androidx.media3.common.GlTextureInfo;
import androidx.media3.common.VideoFrameProcessingException;
import androidx.media3.common.util.GlUtil;
import androidx.media3.common.util.UnstableApi;

/** A component that outputs {@linkplain GlTextureInfo OpenGL textures}. */
@UnstableApi
public interface GlTextureProducer {

  /** Listener for texture output. */
  interface Listener {
    /**
     * Called when a texture has been rendered to.
     *
     * @param textureProducer The {@link GlTextureProducer} that has rendered the texture.
     * @param outputTexture The texture that has been rendered.
     * @param presentationTimeUs The presentation time of the texture.
     * @param syncObject A GL sync object that has been inserted into the GL command stream after
     *     the last write of the {@code outputTexture}. Value is 0 if and only if the {@link
     *     GLES30#glFenceSync} failed.
     */
    void onTextureRendered(
        GlTextureProducer textureProducer,
        GlTextureInfo outputTexture,
        long presentationTimeUs,
        long syncObject)
        throws VideoFrameProcessingException, GlUtil.GlException;
  }

  /** Releases the output texture at the given {@code presentationTimeUs}. */
  void releaseOutputTexture(long presentationTimeUs);
}
