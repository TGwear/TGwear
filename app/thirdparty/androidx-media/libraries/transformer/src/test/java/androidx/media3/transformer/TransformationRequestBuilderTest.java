/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package androidx.media3.transformer;

import static org.junit.Assert.assertThrows;

import androidx.media3.common.MimeTypes;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit test for {@link TransformationRequest.Builder}. */
@RunWith(AndroidJUnit4.class)
public class TransformationRequestBuilderTest {

  @Test
  public void setAudioMimeType_withVideoMimeType_throws() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new TransformationRequest.Builder().setAudioMimeType(MimeTypes.VIDEO_H264));
  }

  @Test
  public void setVideoMimeType_withAudioMimeType_throws() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new TransformationRequest.Builder().setVideoMimeType(MimeTypes.AUDIO_AAC));
  }
}
