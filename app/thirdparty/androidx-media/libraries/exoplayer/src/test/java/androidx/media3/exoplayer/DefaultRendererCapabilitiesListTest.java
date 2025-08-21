/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer;

import static androidx.media3.common.util.Assertions.checkNotNull;
import static com.google.common.truth.Truth.assertThat;

import androidx.media3.common.util.SystemClock;
import androidx.media3.test.utils.FakeAudioRenderer;
import androidx.media3.test.utils.FakeRenderer;
import androidx.media3.test.utils.FakeVideoRenderer;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit test for {@link DefaultRendererCapabilitiesList}. */
@RunWith(AndroidJUnit4.class)
public class DefaultRendererCapabilitiesListTest {

  private AtomicReference<List<FakeRenderer>> underlyingRenderersReference;
  private RenderersFactory renderersFactory;

  @Before
  public void setUp() {
    underlyingRenderersReference = new AtomicReference<>();
    renderersFactory =
        (eventHandler,
            videoRendererEventListener,
            audioRendererEventListener,
            textRendererOutput,
            metadataRendererOutput) -> {
          FakeRenderer[] createdRenderers =
              new FakeRenderer[] {
                new FakeVideoRenderer(
                    SystemClock.DEFAULT.createHandler(
                        eventHandler.getLooper(), /* callback= */ null),
                    videoRendererEventListener),
                new FakeAudioRenderer(
                    SystemClock.DEFAULT.createHandler(
                        eventHandler.getLooper(), /* callback= */ null),
                    audioRendererEventListener)
              };
          underlyingRenderersReference.set(ImmutableList.copyOf(createdRenderers));
          return createdRenderers;
        };
  }

  @Test
  public void createRendererCapabilitiesList_underlyingRenderersInitialized() {
    DefaultRendererCapabilitiesList.Factory rendererCapabilitiesFactory =
        new DefaultRendererCapabilitiesList.Factory(renderersFactory);

    rendererCapabilitiesFactory.createRendererCapabilitiesList();

    List<FakeRenderer> underlyingRenderers = checkNotNull(underlyingRenderersReference.get());
    for (FakeRenderer renderer : underlyingRenderers) {
      assertThat(renderer.isInitialized).isTrue();
    }
  }

  @Test
  public void getRendererCapabilities_returnsExpectedRendererCapabilities() {
    DefaultRendererCapabilitiesList.Factory rendererCapabilitiesFactory =
        new DefaultRendererCapabilitiesList.Factory(renderersFactory);
    DefaultRendererCapabilitiesList rendererCapabilitiesList =
        rendererCapabilitiesFactory.createRendererCapabilitiesList();

    RendererCapabilities[] rendererCapabilities =
        rendererCapabilitiesList.getRendererCapabilities();

    List<FakeRenderer> underlyingRenderers = checkNotNull(underlyingRenderersReference.get());
    assertThat(rendererCapabilities).hasLength(underlyingRenderers.size());
    for (int i = 0; i < rendererCapabilities.length; i++) {
      assertThat(rendererCapabilities[i].getTrackType())
          .isEqualTo(underlyingRenderers.get(i).getTrackType());
    }
  }

  @Test
  public void release_underlyingRenderersReleased() {
    DefaultRendererCapabilitiesList.Factory rendererCapabilitiesFactory =
        new DefaultRendererCapabilitiesList.Factory(renderersFactory);
    DefaultRendererCapabilitiesList rendererCapabilitiesList =
        rendererCapabilitiesFactory.createRendererCapabilitiesList();

    rendererCapabilitiesList.release();

    List<FakeRenderer> underlyingRenderers = checkNotNull(underlyingRenderersReference.get());
    for (FakeRenderer renderer : underlyingRenderers) {
      assertThat(renderer.isReleased).isTrue();
    }
  }
}
