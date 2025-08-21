/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.ogg;

import static androidx.media3.test.utils.TestUtil.getByteArray;
import static com.google.common.truth.Truth.assertThat;

import androidx.media3.common.C;
import androidx.media3.extractor.Extractor;
import androidx.media3.extractor.PositionHolder;
import androidx.media3.test.utils.ExtractorAsserts;
import androidx.media3.test.utils.FakeExtractorInput;
import androidx.media3.test.utils.FakeExtractorOutput;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link OggExtractor} that test specific behaviours and don't need to be parameterized.
 *
 * <p>For parameterized tests using {@link ExtractorAsserts} see {@link
 * OggExtractorParameterizedTest}.
 */
@RunWith(AndroidJUnit4.class)
public final class OggExtractorNonParameterizedTest {

  @Test
  public void read_afterEndOfInput_doesNotThrowIllegalState() throws Exception {
    byte[] data =
        getByteArray(ApplicationProvider.getApplicationContext(), "media/ogg/bear_flac.ogg");
    FakeExtractorInput input = new FakeExtractorInput.Builder().setData(data).build();
    OggExtractor oggExtractor = new OggExtractor();
    oggExtractor.init(new FakeExtractorOutput());
    // We feed data to the extractor until the end of input is reached.
    while (oggExtractor.read(input, new PositionHolder()) != Extractor.RESULT_END_OF_INPUT) {}
    // We call read again to check that it does not throw an IllegalStateException.
    assertThat(oggExtractor.read(input, new PositionHolder())).isEqualTo(C.RESULT_END_OF_INPUT);
  }

  @Test
  public void sniffVorbis() throws Exception {
    byte[] data =
        getByteArray(ApplicationProvider.getApplicationContext(), "media/ogg/vorbis_header");
    assertSniff(data, /* expectedResult= */ true);
  }

  @Test
  public void sniffFlac() throws Exception {
    byte[] data =
        getByteArray(ApplicationProvider.getApplicationContext(), "media/ogg/flac_header");
    assertSniff(data, /* expectedResult= */ true);
  }

  @Test
  public void sniffFailsOpusFile() throws Exception {
    byte[] data =
        getByteArray(ApplicationProvider.getApplicationContext(), "media/ogg/opus_header");
    assertSniff(data, /* expectedResult= */ false);
  }

  @Test
  public void sniffFailsInvalidOggHeader() throws Exception {
    byte[] data =
        getByteArray(ApplicationProvider.getApplicationContext(), "media/ogg/invalid_ogg_header");
    assertSniff(data, /* expectedResult= */ false);
  }

  @Test
  public void sniffInvalidHeader() throws Exception {
    byte[] data =
        getByteArray(ApplicationProvider.getApplicationContext(), "media/ogg/invalid_header");
    assertSniff(data, /* expectedResult= */ false);
  }

  @Test
  public void sniffFailsEOF() throws Exception {
    byte[] data = getByteArray(ApplicationProvider.getApplicationContext(), "media/ogg/eof_header");
    assertSniff(data, /* expectedResult= */ false);
  }

  private void assertSniff(byte[] data, boolean expectedResult) throws IOException {
    FakeExtractorInput input =
        new FakeExtractorInput.Builder()
            .setData(data)
            .setSimulateIOErrors(true)
            .setSimulateUnknownLength(true)
            .setSimulatePartialReads(true)
            .build();
    ExtractorAsserts.assertSniff(new OggExtractor(), input, expectedResult);
  }
}
