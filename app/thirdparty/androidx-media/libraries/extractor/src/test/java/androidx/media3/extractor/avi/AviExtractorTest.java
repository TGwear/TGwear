/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.avi;

import androidx.media3.extractor.text.SubtitleParser;
import androidx.media3.test.utils.ExtractorAsserts;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.ParameterizedRobolectricTestRunner;
import org.robolectric.ParameterizedRobolectricTestRunner.Parameter;
import org.robolectric.ParameterizedRobolectricTestRunner.Parameters;

/** Tests for {@link AviExtractor}. */
@RunWith(ParameterizedRobolectricTestRunner.class)
public final class AviExtractorTest {

  @Parameters(name = "{0}")
  public static ImmutableList<ExtractorAsserts.SimulationConfig> params() {
    return ExtractorAsserts.configs();
  }

  @Parameter public ExtractorAsserts.SimulationConfig simulationConfig;

  @Test
  public void aviSample() throws Exception {
    ExtractorAsserts.assertBehavior(
        () -> new AviExtractor(/* extractorFlags= */ 0, SubtitleParser.Factory.UNSUPPORTED),
        "media/avi/sample.avi",
        simulationConfig);
  }

  @Test
  public void aviSampleWithLargeLength() throws Exception {
    ExtractorAsserts.assertBehavior(
        () -> new AviExtractor(/* extractorFlags= */ 0, SubtitleParser.Factory.UNSUPPORTED),
        "media/avi/sample-with-large-length.avi",
        simulationConfig);
  }
}
