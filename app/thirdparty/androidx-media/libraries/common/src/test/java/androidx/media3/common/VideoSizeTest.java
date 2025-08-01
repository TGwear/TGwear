/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common;

import static com.google.common.truth.Truth.assertThat;

import android.os.Bundle;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit test for {@link VideoSize}. */
@RunWith(AndroidJUnit4.class)
public final class VideoSizeTest {

  @Test
  public void roundTripViaBundle_ofVideoSizeUnknown_yieldsEqualInstance() {
    assertThat(roundTripViaBundle(VideoSize.UNKNOWN)).isEqualTo(VideoSize.UNKNOWN);
  }

  @Test
  public void roundTripViaBundle_ofArbitraryVideoSize_yieldsEqualInstance() {
    VideoSize videoSize =
        new VideoSize(/* width= */ 9, /* height= */ 8, /* pixelWidthHeightRatio= */ 6);
    assertThat(roundTripViaBundle(videoSize)).isEqualTo(videoSize);
  }

  @Test
  public void fromBundle_ofEmptyBundle_yieldsVideoSizeUnknown() {
    assertThat(VideoSize.fromBundle(new Bundle())).isEqualTo(VideoSize.UNKNOWN);
  }

  private static VideoSize roundTripViaBundle(VideoSize videoSize) {
    return VideoSize.fromBundle(videoSize.toBundle());
  }
}
