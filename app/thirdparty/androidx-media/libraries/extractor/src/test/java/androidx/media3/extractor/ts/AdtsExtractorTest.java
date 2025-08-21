/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.ts;

import androidx.media3.test.utils.ExtractorAsserts;
import androidx.media3.test.utils.ExtractorAsserts.AssertionConfig;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.ParameterizedRobolectricTestRunner;
import org.robolectric.ParameterizedRobolectricTestRunner.Parameter;
import org.robolectric.ParameterizedRobolectricTestRunner.Parameters;

/** Unit test for {@link AdtsExtractor}. */
@RunWith(ParameterizedRobolectricTestRunner.class)
public final class AdtsExtractorTest {

  @Parameters(name = "{0}")
  public static ImmutableList<ExtractorAsserts.SimulationConfig> params() {
    return ExtractorAsserts.configs();
  }

  @Parameter public ExtractorAsserts.SimulationConfig simulationConfig;

  @Test
  public void sample() throws Exception {
    ExtractorAsserts.assertBehavior(AdtsExtractor::new, "media/ts/sample.adts", simulationConfig);
  }

  @Test
  public void sample_with_id3() throws Exception {
    ExtractorAsserts.assertBehavior(
        AdtsExtractor::new, "media/ts/sample_with_id3.adts", simulationConfig);
  }

  @Test
  public void sample_withSeeking() throws Exception {
    ExtractorAsserts.assertBehavior(
        () -> new AdtsExtractor(/* flags= */ AdtsExtractor.FLAG_ENABLE_CONSTANT_BITRATE_SEEKING),
        "media/ts/sample.adts",
        new AssertionConfig.Builder()
            .setDumpFilesPrefix("extractordumps/ts/sample_cbs.adts")
            .build(),
        simulationConfig);
  }

  // https://github.com/google/ExoPlayer/issues/6700
  @Test
  public void sample_withSeekingAndTruncatedFile() throws Exception {
    ExtractorAsserts.assertBehavior(
        () -> new AdtsExtractor(/* flags= */ AdtsExtractor.FLAG_ENABLE_CONSTANT_BITRATE_SEEKING),
        "media/ts/sample_cbs_truncated.adts",
        simulationConfig);
  }
}
