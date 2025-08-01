/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.decoder.flac;

import static org.junit.Assert.fail;

import androidx.media3.test.utils.ExtractorAsserts;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit test for {@link FlacExtractor}. */
// TODO(internal: b/26110951): Use org.junit.runners.Parameterized (and corresponding methods on
//  ExtractorAsserts) when it's supported by our testing infrastructure.
@RunWith(AndroidJUnit4.class)
public class FlacExtractorTest {

  @Before
  public void setUp() {
    if (!FlacLibrary.isAvailable()) {
      fail("Flac library not available.");
    }
  }

  @Test
  public void sample() throws Exception {
    ExtractorAsserts.assertAllBehaviors(
        FlacExtractor::new,
        /* file= */ "media/flac/bear.flac",
        /* dumpFilesPrefix= */ "extractordumps/flac/bear_raw");
  }

  @Test
  public void sample32bit() throws Exception {
    ExtractorAsserts.assertAllBehaviors(
        FlacExtractor::new,
        /* file= */ "media/flac/bear_32bit.flac",
        /* dumpFilesPrefix= */ "extractordumps/flac/bear_32bit_raw");
  }

  @Test
  public void sampleWithId3HeaderAndId3Enabled() throws Exception {
    ExtractorAsserts.assertAllBehaviors(
        FlacExtractor::new,
        /* file= */ "media/flac/bear_with_id3.flac",
        /* dumpFilesPrefix= */ "extractordumps/flac/bear_with_id3_enabled_raw");
  }

  @Test
  public void sampleWithId3HeaderAndId3Disabled() throws Exception {
    ExtractorAsserts.assertAllBehaviors(
        () -> new FlacExtractor(FlacExtractor.FLAG_DISABLE_ID3_METADATA),
        /* file= */ "media/flac/bear_with_id3.flac",
        /* dumpFilesPrefix= */ "extractordumps/flac/bear_with_id3_disabled_raw");
  }

  @Test
  public void sampleUnseekable() throws Exception {
    ExtractorAsserts.assertAllBehaviors(
        FlacExtractor::new,
        /* file= */ "media/flac/bear_no_seek_table_no_num_samples.flac",
        /* dumpFilesPrefix= */ "extractordumps/flac/bear_no_seek_table_no_num_samples_raw");
  }

  @Test
  public void sampleWithVorbisComments() throws Exception {
    ExtractorAsserts.assertAllBehaviors(
        FlacExtractor::new,
        /* file= */ "media/flac/bear_with_vorbis_comments.flac",
        /* dumpFilesPrefix= */ "extractordumps/flac/bear_with_vorbis_comments_raw");
  }

  @Test
  public void sampleWithPicture() throws Exception {
    ExtractorAsserts.assertAllBehaviors(
        FlacExtractor::new,
        /* file= */ "media/flac/bear_with_picture.flac",
        /* dumpFilesPrefix= */ "extractordumps/flac/bear_with_picture_raw");
  }

  @Test
  public void oneMetadataBlock() throws Exception {
    ExtractorAsserts.assertAllBehaviors(
        FlacExtractor::new,
        /* file= */ "media/flac/bear_one_metadata_block.flac",
        /* dumpFilesPrefix= */ "extractordumps/flac/bear_one_metadata_block_raw");
  }

  @Test
  public void noMinMaxFrameSize() throws Exception {
    ExtractorAsserts.assertAllBehaviors(
        FlacExtractor::new,
        /* file= */ "media/flac/bear_no_min_max_frame_size.flac",
        /* dumpFilesPrefix= */ "extractordumps/flac/bear_no_min_max_frame_size_raw");
  }

  @Test
  public void noNumSamples() throws Exception {
    ExtractorAsserts.assertAllBehaviors(
        FlacExtractor::new,
        /* file= */ "media/flac/bear_no_num_samples.flac",
        /* dumpFilesPrefix= */ "extractordumps/flac/bear_no_num_samples_raw");
  }

  @Test
  public void uncommonSampleRate() throws Exception {
    ExtractorAsserts.assertAllBehaviors(
        FlacExtractor::new,
        /* file= */ "media/flac/bear_uncommon_sample_rate.flac",
        /* dumpFilesPrefix= */ "extractordumps/flac/bear_uncommon_sample_rate_raw");
  }
}
