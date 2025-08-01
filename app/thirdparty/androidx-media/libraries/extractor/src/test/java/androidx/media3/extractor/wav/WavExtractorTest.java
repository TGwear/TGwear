/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.wav;

import androidx.media3.test.utils.ExtractorAsserts;
import androidx.media3.test.utils.ExtractorAsserts.AssertionConfig;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.ParameterizedRobolectricTestRunner;

/** Unit test for {@link WavExtractor}. */
@RunWith(ParameterizedRobolectricTestRunner.class)
public final class WavExtractorTest {

  @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
  public static ImmutableList<ExtractorAsserts.SimulationConfig> params() {
    return ExtractorAsserts.configs();
  }

  @ParameterizedRobolectricTestRunner.Parameter(0)
  public ExtractorAsserts.SimulationConfig simulationConfig;

  @Test
  public void sample() throws Exception {
    ExtractorAsserts.assertBehavior(WavExtractor::new, "media/wav/sample.wav", simulationConfig);
  }

  @Test
  public void sample_withTrailingBytes_extractsSameData() throws Exception {
    ExtractorAsserts.assertBehavior(
        WavExtractor::new,
        "media/wav/sample_with_trailing_bytes.wav",
        new AssertionConfig.Builder().setDumpFilesPrefix("extractordumps/wav/sample.wav").build(),
        simulationConfig);
  }

  @Test
  public void sample_withOddMetadataChunkSize_extractsSameData() throws Exception {
    ExtractorAsserts.assertBehavior(
        WavExtractor::new,
        "media/wav/sample_with_odd_metadata_chunk_size.wav",
        new AssertionConfig.Builder().setDumpFilesPrefix("extractordumps/wav/sample.wav").build(),
        simulationConfig);
  }

  @Test
  public void sample_imaAdpcm() throws Exception {
    ExtractorAsserts.assertBehavior(
        WavExtractor::new, "media/wav/sample_ima_adpcm.wav", simulationConfig);
  }

  @Test
  public void sample_rf64() throws Exception {
    ExtractorAsserts.assertBehavior(
        WavExtractor::new, "media/wav/sample_rf64.wav", simulationConfig);
  }
}
