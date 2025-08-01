/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer;

import android.content.Context;
import androidx.media3.common.util.SystemClock;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.analytics.PlayerId;
import androidx.media3.exoplayer.audio.AudioRendererEventListener;
import androidx.media3.exoplayer.video.VideoRendererEventListener;
import java.util.Arrays;

/** The default {@link RendererCapabilitiesList} implementation. */
@UnstableApi
public final class DefaultRendererCapabilitiesList implements RendererCapabilitiesList {

  /** Factory for {@link DefaultRendererCapabilitiesList}. */
  public static final class Factory implements RendererCapabilitiesList.Factory {
    private final RenderersFactory renderersFactory;

    /**
     * Creates an instance.
     *
     * @param context A context to create a {@link DefaultRenderersFactory} that is used as the
     *     default.
     */
    public Factory(Context context) {
      this.renderersFactory = new DefaultRenderersFactory(context);
    }

    /**
     * Creates an instance.
     *
     * @param renderersFactory The {@link RenderersFactory} to create an array of {@linkplain
     *     Renderer renderers} whose {@link RendererCapabilities} are represented by the {@link
     *     DefaultRendererCapabilitiesList}.
     */
    public Factory(RenderersFactory renderersFactory) {
      this.renderersFactory = renderersFactory;
    }

    @Override
    public DefaultRendererCapabilitiesList createRendererCapabilitiesList() {
      Renderer[] renderers =
          renderersFactory.createRenderers(
              Util.createHandlerForCurrentOrMainLooper(),
              new VideoRendererEventListener() {},
              new AudioRendererEventListener() {},
              cueGroup -> {},
              metadata -> {});
      return new DefaultRendererCapabilitiesList(renderers);
    }
  }

  private final Renderer[] renderers;

  private DefaultRendererCapabilitiesList(Renderer[] renderers) {
    this.renderers = Arrays.copyOf(renderers, renderers.length);
    for (int i = 0; i < renderers.length; i++) {
      this.renderers[i].init(i, PlayerId.UNSET, SystemClock.DEFAULT);
    }
  }

  @Override
  public RendererCapabilities[] getRendererCapabilities() {
    RendererCapabilities[] rendererCapabilities = new RendererCapabilities[renderers.length];
    for (int i = 0; i < renderers.length; i++) {
      rendererCapabilities[i] = renderers[i].getCapabilities();
    }
    return rendererCapabilities;
  }

  @Override
  public int size() {
    return renderers.length;
  }

  @Override
  public void release() {
    for (Renderer renderer : renderers) {
      renderer.release();
    }
  }
}
