/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package androidx.media3.transformer;

import static org.junit.Assert.assertThrows;

import android.content.Context;
import androidx.media3.common.MimeTypes;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit test for {@link Transformer.Builder}. */
@RunWith(AndroidJUnit4.class)
public class TransformerBuilderTest {

  @Test
  public void build_withUnsupportedAudioMimeType_throws() {
    Context context = ApplicationProvider.getApplicationContext();

    assertThrows(
        IllegalStateException.class,
        () -> new Transformer.Builder(context).setAudioMimeType(MimeTypes.AUDIO_UNKNOWN).build());
  }

  @Test
  public void build_withUnsupportedVideoMimeType_throws() {
    Context context = ApplicationProvider.getApplicationContext();

    assertThrows(
        IllegalStateException.class,
        () -> new Transformer.Builder(context).setVideoMimeType(MimeTypes.VIDEO_UNKNOWN).build());
  }
}
