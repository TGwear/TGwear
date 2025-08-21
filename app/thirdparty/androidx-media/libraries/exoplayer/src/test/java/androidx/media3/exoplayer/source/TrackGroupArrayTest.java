/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.source;

import static com.google.common.truth.Truth.assertThat;

import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.TrackGroup;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit test for {@link TrackGroupArray}. */
@RunWith(AndroidJUnit4.class)
public final class TrackGroupArrayTest {

  @Test
  public void roundTripViaBundle_ofTrackGroupArray_yieldsEqualInstance() {
    Format.Builder formatBuilder = new Format.Builder();
    Format format1 = formatBuilder.setSampleMimeType(MimeTypes.VIDEO_H264).build();
    Format format2 = formatBuilder.setSampleMimeType(MimeTypes.AUDIO_AAC).build();
    Format format3 = formatBuilder.setSampleMimeType(MimeTypes.VIDEO_H264).build();

    TrackGroup trackGroup1 = new TrackGroup(format1, format2);
    TrackGroup trackGroup2 = new TrackGroup(format3);

    TrackGroupArray trackGroupArrayToBundle = new TrackGroupArray(trackGroup1, trackGroup2);

    TrackGroupArray trackGroupArrayFromBundle =
        TrackGroupArray.fromBundle(trackGroupArrayToBundle.toBundle());

    assertThat(trackGroupArrayFromBundle).isEqualTo(trackGroupArrayToBundle);
  }
}
