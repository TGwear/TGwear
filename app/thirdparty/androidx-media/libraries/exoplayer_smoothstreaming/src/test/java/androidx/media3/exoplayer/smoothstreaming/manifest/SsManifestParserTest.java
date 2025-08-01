/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.smoothstreaming.manifest;

import static com.google.common.truth.Truth.assertThat;

import android.net.Uri;
import androidx.media3.test.utils.TestUtil;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit tests for {@link SsManifestParser}. */
@RunWith(AndroidJUnit4.class)
public final class SsManifestParserTest {

  private static final String SAMPLE_ISMC_1 = "media/smooth-streaming/sample_ismc_1";
  private static final String SAMPLE_ISMC_2 = "media/smooth-streaming/sample_ismc_2";

  /** Simple test to ensure the sample manifests parse without any exceptions being thrown. */
  @Test
  public void parseSmoothStreamingManifest() throws Exception {
    SsManifestParser parser = new SsManifestParser();
    parser.parse(
        Uri.parse("https://example.com/test.ismc"),
        TestUtil.getInputStream(ApplicationProvider.getApplicationContext(), SAMPLE_ISMC_1));
    parser.parse(
        Uri.parse("https://example.com/test.ismc"),
        TestUtil.getInputStream(ApplicationProvider.getApplicationContext(), SAMPLE_ISMC_2));
  }

  @Test
  public void parse_populatesFormatLabelWithStreamIndexName() throws Exception {
    SsManifestParser parser = new SsManifestParser();
    SsManifest ssManifest =
        parser.parse(
            Uri.parse("https://example.com/test.ismc"),
            TestUtil.getInputStream(ApplicationProvider.getApplicationContext(), SAMPLE_ISMC_1));

    assertThat(ssManifest.streamElements[0].formats[0].label).isEqualTo("video");
    assertThat(ssManifest.streamElements[0].formats[0].labels.get(0).value).isEqualTo("video");
  }
}
