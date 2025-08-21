/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.metadata.icy;

import static com.google.common.truth.Truth.assertThat;

import androidx.media3.common.MediaMetadata;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Test for {@link IcyHeaders}. */
@RunWith(AndroidJUnit4.class)
public final class IcyHeadersTest {
  @Test
  public void populateMediaMetadata() {
    IcyHeaders headers =
        new IcyHeaders(
            /* bitrate= */ 1234,
            /* genre= */ "pop",
            /* name= */ "radio station",
            /* url= */ "url",
            /* isPublic= */ true,
            /* metadataInterval= */ 5678);
    MediaMetadata.Builder builder = new MediaMetadata.Builder();

    headers.populateMediaMetadata(builder);
    MediaMetadata mediaMetadata = builder.build();

    assertThat(mediaMetadata.station.toString()).isEqualTo("radio station");
    assertThat(mediaMetadata.genre.toString()).isEqualTo("pop");
  }
}
