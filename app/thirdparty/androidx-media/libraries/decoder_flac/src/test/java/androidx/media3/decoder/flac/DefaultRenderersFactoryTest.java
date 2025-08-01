/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.decoder.flac;

import androidx.media3.common.C;
import androidx.media3.test.utils.DefaultRenderersFactoryAsserts;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit test for {@link DefaultRenderersFactoryTest} with {@link LibflacAudioRenderer}. */
@RunWith(AndroidJUnit4.class)
public final class DefaultRenderersFactoryTest {

  @Test
  public void createRenderers_instantiatesFlacRenderer() {
    DefaultRenderersFactoryAsserts.assertExtensionRendererCreated(
        LibflacAudioRenderer.class, C.TRACK_TYPE_AUDIO);
  }
}
