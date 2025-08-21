/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.ts;

import androidx.media3.test.utils.ExtractorAsserts;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.ParameterizedRobolectricTestRunner;
import org.robolectric.ParameterizedRobolectricTestRunner.Parameter;
import org.robolectric.ParameterizedRobolectricTestRunner.Parameters;

/** Unit test for {@link PsExtractor}. */
@RunWith(ParameterizedRobolectricTestRunner.class)
public final class PsExtractorTest {

  @Parameters(name = "{0}")
  public static ImmutableList<ExtractorAsserts.SimulationConfig> params() {
    return ExtractorAsserts.configs();
  }

  @Parameter public ExtractorAsserts.SimulationConfig simulationConfig;

  @Test
  public void sampleWithH262AndMpegAudio() throws Exception {
    ExtractorAsserts.assertBehavior(
        PsExtractor::new, "media/ts/sample_h262_mpeg_audio.ps", simulationConfig);
  }

  @Test
  public void sampleWithAc3() throws Exception {
    ExtractorAsserts.assertBehavior(PsExtractor::new, "media/ts/sample_ac3.ps", simulationConfig);
  }
}
