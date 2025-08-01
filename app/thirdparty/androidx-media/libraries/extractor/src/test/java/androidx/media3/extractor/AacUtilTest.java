/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import androidx.media3.common.ParserException;
import androidx.media3.common.util.Util;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit tests for {@link AacUtil}. */
@RunWith(AndroidJUnit4.class)
public final class AacUtilTest {
  private static final byte[] AAC_48K_2CH_HEADER = Util.getBytesFromHexString("1190");

  private static final byte[] NOT_ENOUGH_ARBITRARY_SAMPLING_FREQ_BITS_HEADER =
      Util.getBytesFromHexString("1790");

  private static final byte[] ARBITRARY_SAMPLING_FREQ_BITS_HEADER =
      Util.getBytesFromHexString("1780000790");

  @Test
  public void parseAudioSpecificConfig_twoCh48kAac_parsedCorrectly() throws Exception {
    AacUtil.Config aac = AacUtil.parseAudioSpecificConfig(AAC_48K_2CH_HEADER);

    assertThat(aac.channelCount).isEqualTo(2);
    assertThat(aac.sampleRateHz).isEqualTo(48000);
    assertThat(aac.codecs).isEqualTo("mp4a.40.2");
  }

  @Test
  public void parseAudioSpecificConfig_arbitrarySamplingFreqHeader_parsedCorrectly()
      throws Exception {
    AacUtil.Config aac = AacUtil.parseAudioSpecificConfig(ARBITRARY_SAMPLING_FREQ_BITS_HEADER);
    assertThat(aac.channelCount).isEqualTo(2);
    assertThat(aac.sampleRateHz).isEqualTo(15);
    assertThat(aac.codecs).isEqualTo("mp4a.40.2");
  }

  @Test
  public void
      parseAudioSpecificConfig_arbitrarySamplingFreqHeaderNotEnoughBits_throwsParserException() {
    // ISO 14496-3 1.6.2.1 allows for setting of arbitrary sampling frequency, but if the extra
    // frequency bits are missing, make sure the code will throw an exception.
    assertThrows(
        ParserException.class,
        () -> AacUtil.parseAudioSpecificConfig(NOT_ENOUGH_ARBITRARY_SAMPLING_FREQ_BITS_HEADER));
  }
}
