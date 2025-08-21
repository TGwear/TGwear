/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package androidx.media3.transformer;

import static androidx.media3.transformer.Composition.HDR_MODE_TONE_MAP_HDR_TO_SDR_USING_OPEN_GL;
import static com.google.common.truth.Truth.assertThat;

import androidx.media3.common.MimeTypes;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit test for {@link TransformationRequest}. */
@RunWith(AndroidJUnit4.class)
public class TransformationRequestTest {

  @Test
  public void buildUponTransformationRequest_createsEqualTransformationRequest() {
    TransformationRequest request = createTestTransformationRequest();
    assertThat(request.buildUpon().build()).isEqualTo(request);
  }

  private static TransformationRequest createTestTransformationRequest() {
    return new TransformationRequest.Builder()
        .setResolution(720)
        .setAudioMimeType(MimeTypes.AUDIO_AAC)
        .setVideoMimeType(MimeTypes.VIDEO_H264)
        .setHdrMode(HDR_MODE_TONE_MAP_HDR_TO_SDR_USING_OPEN_GL)
        .build();
  }
}
