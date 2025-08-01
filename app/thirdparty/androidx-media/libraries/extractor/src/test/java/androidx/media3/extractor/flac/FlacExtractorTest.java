/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.flac;

import androidx.media3.test.utils.ExtractorAsserts;
import androidx.media3.test.utils.ExtractorAsserts.AssertionConfig;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.ParameterizedRobolectricTestRunner;
import org.robolectric.ParameterizedRobolectricTestRunner.Parameter;
import org.robolectric.ParameterizedRobolectricTestRunner.Parameters;

/** Unit tests for {@link FlacExtractor}. */
@RunWith(ParameterizedRobolectricTestRunner.class)
public class FlacExtractorTest {

  @Parameters(name = "{0}")
  public static ImmutableList<ExtractorAsserts.SimulationConfig> params() {
    return ExtractorAsserts.configs();
  }

  @Parameter public ExtractorAsserts.SimulationConfig simulationConfig;

  @Test
  public void sample() throws Exception {
    ExtractorAsserts.assertBehavior(
        FlacExtractor::new,
        "media/flac/bear.flac",
        new AssertionConfig.Builder().setDumpFilesPrefix("extractordumps/flac/bear_flac").build(),
        simulationConfig);
  }

  @Test
  public void sample32bit() throws Exception {
    ExtractorAsserts.assertBehavior(
        FlacExtractor::new,
        "media/flac/bear_32bit.flac",
        new AssertionConfig.Builder()
            .setDumpFilesPrefix("extractordumps/flac/bear_32bit_flac")
            .build(),
        simulationConfig);
  }

  @Test
  public void sampleWithId3HeaderAndId3Enabled() throws Exception {
    ExtractorAsserts.assertBehavior(
        FlacExtractor::new,
        "media/flac/bear_with_id3.flac",
        new AssertionConfig.Builder()
            .setDumpFilesPrefix("extractordumps/flac/bear_with_id3_enabled_flac")
            .build(),
        simulationConfig);
  }

  @Test
  public void sampleWithId3HeaderAndId3Disabled() throws Exception {
    ExtractorAsserts.assertBehavior(
        () -> new FlacExtractor(FlacExtractor.FLAG_DISABLE_ID3_METADATA),
        "media/flac/bear_with_id3.flac",
        new AssertionConfig.Builder()
            .setDumpFilesPrefix("extractordumps/flac/bear_with_id3_disabled_flac")
            .build(),
        simulationConfig);
  }

  @Test
  public void sampleUnseekable() throws Exception {
    ExtractorAsserts.assertBehavior(
        FlacExtractor::new,
        "media/flac/bear_no_seek_table_no_num_samples.flac",
        new AssertionConfig.Builder()
            .setDumpFilesPrefix("extractordumps/flac/bear_no_seek_table_no_num_samples_flac")
            .build(),
        simulationConfig);
  }

  @Test
  public void sampleWithVorbisComments() throws Exception {
    ExtractorAsserts.assertBehavior(
        FlacExtractor::new,
        "media/flac/bear_with_vorbis_comments.flac",
        new AssertionConfig.Builder()
            .setDumpFilesPrefix("extractordumps/flac/bear_with_vorbis_comments_flac")
            .build(),
        simulationConfig);
  }

  @Test
  public void sampleWithPicture() throws Exception {
    ExtractorAsserts.assertBehavior(
        FlacExtractor::new,
        "media/flac/bear_with_picture.flac",
        new AssertionConfig.Builder()
            .setDumpFilesPrefix("extractordumps/flac/bear_with_picture_flac")
            .build(),
        simulationConfig);
  }

  @Test
  public void oneMetadataBlock() throws Exception {
    ExtractorAsserts.assertBehavior(
        FlacExtractor::new,
        "media/flac/bear_one_metadata_block.flac",
        new AssertionConfig.Builder()
            .setDumpFilesPrefix("extractordumps/flac/bear_one_metadata_block_flac")
            .build(),
        simulationConfig);
  }

  @Test
  public void noMinMaxFrameSize() throws Exception {
    ExtractorAsserts.assertBehavior(
        FlacExtractor::new,
        "media/flac/bear_no_min_max_frame_size.flac",
        new AssertionConfig.Builder()
            .setDumpFilesPrefix("extractordumps/flac/bear_no_min_max_frame_size_flac")
            .build(),
        simulationConfig);
  }

  @Test
  public void noNumSamples() throws Exception {
    ExtractorAsserts.assertBehavior(
        FlacExtractor::new,
        "media/flac/bear_no_num_samples.flac",
        new AssertionConfig.Builder()
            .setDumpFilesPrefix("extractordumps/flac/bear_no_num_samples_flac")
            .build(),
        simulationConfig);
  }

  @Test
  public void uncommonSampleRate() throws Exception {
    ExtractorAsserts.assertBehavior(
        FlacExtractor::new,
        "media/flac/bear_uncommon_sample_rate.flac",
        new AssertionConfig.Builder()
            .setDumpFilesPrefix("extractordumps/flac/bear_uncommon_sample_rate_flac")
            .build(),
        simulationConfig);
  }
}
