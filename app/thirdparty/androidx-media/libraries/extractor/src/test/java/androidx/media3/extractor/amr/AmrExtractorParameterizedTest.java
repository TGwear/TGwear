/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.amr;

import androidx.media3.test.utils.ExtractorAsserts;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.ParameterizedRobolectricTestRunner;
import org.robolectric.ParameterizedRobolectricTestRunner.Parameter;
import org.robolectric.ParameterizedRobolectricTestRunner.Parameters;

/**
 * Unit tests for {@link AmrExtractor} that use parameterization to test a range of behaviours.
 *
 * <p>For non-parameterized tests see {@link AmrExtractorSeekTest} and {@link
 * AmrExtractorNonParameterizedTest}.
 */
@RunWith(ParameterizedRobolectricTestRunner.class)
public final class AmrExtractorParameterizedTest {

  @Parameters(name = "{0}")
  public static ImmutableList<ExtractorAsserts.SimulationConfig> params() {
    return ExtractorAsserts.configs();
  }

  @Parameter public ExtractorAsserts.SimulationConfig simulationConfig;

  @Test
  public void extractingNarrowBandSamples() throws Exception {
    ExtractorAsserts.assertBehavior(AmrExtractor::new, "media/amr/sample_nb.amr", simulationConfig);
  }

  @Test
  public void extractingWideBandSamples() throws Exception {
    ExtractorAsserts.assertBehavior(AmrExtractor::new, "media/amr/sample_wb.amr", simulationConfig);
  }

  @Test
  public void extractingNarrowBandSamples_withSeeking() throws Exception {
    ExtractorAsserts.assertBehavior(
        () -> new AmrExtractor(AmrExtractor.FLAG_ENABLE_CONSTANT_BITRATE_SEEKING),
        "media/amr/sample_nb.amr",
        new ExtractorAsserts.AssertionConfig.Builder()
            .setDumpFilesPrefix("extractordumps/amr/sample_nb_cbr_seeking_enabled.amr")
            .build(),
        simulationConfig);
  }

  @Test
  public void extractingWideBandSamples_withSeeking() throws Exception {
    ExtractorAsserts.assertBehavior(
        () -> new AmrExtractor(AmrExtractor.FLAG_ENABLE_CONSTANT_BITRATE_SEEKING),
        "media/amr/sample_wb.amr",
        new ExtractorAsserts.AssertionConfig.Builder()
            .setDumpFilesPrefix("extractordumps/amr/sample_wb_cbr_seeking_enabled.amr")
            .build(),
        simulationConfig);
  }

  @Test
  public void extractingNarrowBandSamples_withSeekingAlways() throws Exception {
    ExtractorAsserts.assertBehavior(
        () -> new AmrExtractor(AmrExtractor.FLAG_ENABLE_CONSTANT_BITRATE_SEEKING_ALWAYS),
        "media/amr/sample_nb.amr",
        new ExtractorAsserts.AssertionConfig.Builder()
            .setDumpFilesPrefix("extractordumps/amr/sample_nb_cbr_seeking_always_enabled.amr")
            .build(),
        simulationConfig);
  }

  @Test
  public void extractingWideBandSamples_withSeekingAlways() throws Exception {
    ExtractorAsserts.assertBehavior(
        () -> new AmrExtractor(AmrExtractor.FLAG_ENABLE_CONSTANT_BITRATE_SEEKING_ALWAYS),
        "media/amr/sample_wb.amr",
        new ExtractorAsserts.AssertionConfig.Builder()
            .setDumpFilesPrefix("extractordumps/amr/sample_wb_cbr_seeking_always_enabled.amr")
            .build(),
        simulationConfig);
  }

  @Test
  public void extractingNarrowBandSamples_withIndexSeeking() throws Exception {
    ExtractorAsserts.assertBehavior(
        () -> new AmrExtractor(AmrExtractor.FLAG_ENABLE_INDEX_SEEKING),
        "media/amr/sample_nb_with_silence_frames.amr",
        simulationConfig);
  }

  @Test
  public void extractingWideBandSamples_withIndexSeeking() throws Exception {
    ExtractorAsserts.assertBehavior(
        () -> new AmrExtractor(AmrExtractor.FLAG_ENABLE_INDEX_SEEKING),
        "media/amr/sample_wb_with_silence_frames.amr",
        simulationConfig);
  }
}
