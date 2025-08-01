/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.ts;

import static com.google.common.truth.Truth.assertThat;

import androidx.media3.extractor.Extractor;
import androidx.media3.extractor.PositionHolder;
import androidx.media3.test.utils.FakeExtractorInput;
import androidx.media3.test.utils.TestUtil;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit test for {@link TsDurationReader}. */
@RunWith(AndroidJUnit4.class)
public final class TsDurationReaderTest {

  private TsDurationReader tsDurationReader;
  private PositionHolder seekPositionHolder;

  @Before
  public void setUp() {
    tsDurationReader = new TsDurationReader(TsExtractor.DEFAULT_TIMESTAMP_SEARCH_BYTES);
    seekPositionHolder = new PositionHolder();
  }

  @Test
  public void isDurationReadPending_returnFalseByDefault() {
    assertThat(tsDurationReader.isDurationReadFinished()).isFalse();
  }

  @Test
  public void readDuration_returnsCorrectDuration() throws IOException, InterruptedException {
    FakeExtractorInput input =
        new FakeExtractorInput.Builder()
            .setData(
                TestUtil.getByteArray(
                    ApplicationProvider.getApplicationContext(), "media/ts/bbb_2500ms.ts"))
            .setSimulateIOErrors(false)
            .setSimulateUnknownLength(false)
            .setSimulatePartialReads(false)
            .build();

    while (!tsDurationReader.isDurationReadFinished()) {
      int result = tsDurationReader.readDuration(input, seekPositionHolder, /* pcrPid= */ 256);
      if (result == Extractor.RESULT_END_OF_INPUT) {
        break;
      }
      if (result == Extractor.RESULT_SEEK) {
        input.setPosition((int) seekPositionHolder.position);
      }
    }
    assertThat(tsDurationReader.getDurationUs() / 1000).isEqualTo(2500);
  }

  @Test
  public void readDuration_midStream_returnsCorrectDuration() throws IOException {
    FakeExtractorInput input =
        new FakeExtractorInput.Builder()
            .setData(
                TestUtil.getByteArray(
                    ApplicationProvider.getApplicationContext(), "media/ts/bbb_2500ms.ts"))
            .setSimulateIOErrors(false)
            .setSimulateUnknownLength(false)
            .setSimulatePartialReads(false)
            .build();

    input.setPosition(1234);
    while (!tsDurationReader.isDurationReadFinished()) {
      int result = tsDurationReader.readDuration(input, seekPositionHolder, /* pcrPid= */ 256);
      if (result == Extractor.RESULT_END_OF_INPUT) {
        break;
      }
      if (result == Extractor.RESULT_SEEK) {
        input.setPosition((int) seekPositionHolder.position);
      }
    }
    assertThat(tsDurationReader.getDurationUs() / 1000).isEqualTo(2500);
  }
}
