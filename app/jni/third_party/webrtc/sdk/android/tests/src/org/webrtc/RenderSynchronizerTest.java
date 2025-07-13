/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

import static java.lang.Math.ceil;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import androidx.test.runner.AndroidJUnit4;
import java.time.Duration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLooper;
import org.robolectric.shadows.ShadowChoreographer;

@RunWith(AndroidJUnit4.class)
@Config(manifest = Config.NONE)
public class RenderSynchronizerTest {

  private static final Duration FRAME_DELAY = Duration.ofMillis(16);
  private static final float DEFAULT_FRAME_RATE = 30f;

  @Mock private RenderSynchronizer.Listener mockListener;
  private RenderSynchronizer renderSynchronizer;
  private Duration refreshDelay;
  private float frameRate;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    ShadowChoreographer.setPaused(true);
  }

  private void init(float displayRefreshRate, float renderFrameRate) {
    refreshDelay = Duration.ofMillis((long)ceil(1000 / displayRefreshRate));
    frameRate = renderFrameRate;

    ShadowChoreographer.setFrameDelay(refreshDelay);
    renderSynchronizer = new RenderSynchronizer(renderFrameRate);
    renderSynchronizer.registerListener(mockListener);
  }

  @Test
  public void renderWindowOpensOnFirstFrame() {
    init(/* displayRefreshRate= */60f, /* renderFrameRate= */30f);

    advanceOneRefreshCycle();
    verify(mockListener).onRenderWindowOpen();
  }

  @Test
  public void doubleDisplayRefreshRate() {
    init(/* displayRefreshRate= */60f, /* renderFrameRate= */30f);

    InOrder inOrder = inOrder(mockListener);
    for (int i = 0; i < 10; ++i) {
      advanceOneRefreshCycle();
      inOrder.verify(mockListener).onRenderWindowOpen();

      advanceOneRefreshCycle();
      inOrder.verify(mockListener).onRenderWindowClose();
    }
  }

  @Test
  public void trippleRefreshRate() {
    init(/* displayRefreshRate= */90f, /* renderFrameRate= */30f);
    InOrder inOrder = inOrder(mockListener);
    for (int i = 0; i < 10; ++i) {
      advanceOneRefreshCycle();
      inOrder.verify(mockListener).onRenderWindowOpen();

      advanceOneRefreshCycle();
      inOrder.verify(mockListener).onRenderWindowClose();

      advanceOneRefreshCycle();
      // No action expected, window stays closed/
    }
  }

  @Test
  public void equalRefreshRate() {
    init(/* displayRefreshRate= */30f, /* renderFrameRate= */30f);
    for (int i = 0; i < 10; ++i) {
      advanceOneRefreshCycle();
    }
    verify(mockListener, times(10)).onRenderWindowOpen();
    verify(mockListener, times(0)).onRenderWindowClose();
  }

  @Test
  public void halfRefreshRate() {
    init(/* displayRefreshRate= */30f, /* renderFrameRate= */60f);
    for (int i = 0; i < 10; ++i) {
      advanceOneRefreshCycle();
    }
    verify(mockListener, times(10)).onRenderWindowOpen();
    verify(mockListener, times(0)).onRenderWindowClose();
  }

  private void advanceOneRefreshCycle() {
    advanceBy(refreshDelay);
  }

  private void advanceBy(Duration duration) {
    ShadowLooper.idleMainLooper(duration.toMillis(), MILLISECONDS);
  }
}
