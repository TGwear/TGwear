/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common;

import static com.google.common.truth.Truth.assertThat;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit test for {@link TrackGroup}. */
@RunWith(AndroidJUnit4.class)
public final class TrackGroupTest {

  @Test
  public void roundTripViaBundle_ofTrackGroup_yieldsEqualInstance() {
    Format.Builder formatBuilder = new Format.Builder();
    Format format1 = formatBuilder.setSampleMimeType(MimeTypes.VIDEO_H264).build();
    Format format2 = formatBuilder.setSampleMimeType(MimeTypes.AUDIO_AAC).build();
    String id = "abc";

    TrackGroup trackGroupToBundle = new TrackGroup(id, format1, format2);

    TrackGroup trackGroupFromBundle = TrackGroup.fromBundle(trackGroupToBundle.toBundle());

    assertThat(trackGroupFromBundle).isEqualTo(trackGroupToBundle);
  }
}
