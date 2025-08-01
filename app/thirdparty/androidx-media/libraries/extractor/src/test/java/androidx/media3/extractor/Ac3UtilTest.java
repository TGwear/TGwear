/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor;

import static com.google.common.truth.Truth.assertThat;

import androidx.media3.common.util.Util;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit tests for {@link Ac3Util}. */
@RunWith(AndroidJUnit4.class)
public final class Ac3UtilTest {

  private static final int TRUEHD_SYNCFRAME_SAMPLE_COUNT = 40;
  private static final byte[] TRUEHD_SYNCFRAME_HEADER =
      Util.getBytesFromHexString("C07504D8F8726FBA0097C00FB7520000");
  private static final byte[] TRUEHD_NON_SYNCFRAME_HEADER =
      Util.getBytesFromHexString("A025048860224E6F6DEDB6D5B6DBAFE6");

  @Test
  public void parseTrueHdSyncframeAudioSampleCount_nonSyncframe() {
    assertThat(Ac3Util.parseTrueHdSyncframeAudioSampleCount(TRUEHD_NON_SYNCFRAME_HEADER))
        .isEqualTo(0);
  }

  @Test
  public void parseTrueHdSyncframeAudioSampleCount_syncframe() {
    assertThat(Ac3Util.parseTrueHdSyncframeAudioSampleCount(TRUEHD_SYNCFRAME_HEADER))
        .isEqualTo(TRUEHD_SYNCFRAME_SAMPLE_COUNT);
  }
}
