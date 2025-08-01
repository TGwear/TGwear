/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.mp4;

import static com.google.common.truth.Truth.assertThat;

import androidx.media3.extractor.SniffFailure;
import androidx.media3.extractor.text.SubtitleParser;
import androidx.media3.test.utils.FakeExtractorInput;
import androidx.media3.test.utils.TestUtil;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Non-parameterized tests for {@link FragmentedMp4Extractor}. */
@RunWith(AndroidJUnit4.class)
public final class FragmentedMp4ExtractorNonParameterizedTest {

  @Test
  public void sniff_reportsUnsupportedBrandsFailure() throws Exception {
    FragmentedMp4Extractor extractor =
        new FragmentedMp4Extractor(SubtitleParser.Factory.UNSUPPORTED);
    FakeExtractorInput input = createInputForSample("sample_fragmented_unsupported_brands.mp4");

    boolean sniffResult = extractor.sniff(input);
    ImmutableList<SniffFailure> sniffFailures = extractor.getSniffFailureDetails();

    assertThat(sniffResult).isFalse();
    SniffFailure sniffFailure = Iterables.getOnlyElement(sniffFailures);
    assertThat(sniffFailure).isInstanceOf(UnsupportedBrandsSniffFailure.class);
    UnsupportedBrandsSniffFailure unsupportedBrandsSniffFailure =
        (UnsupportedBrandsSniffFailure) sniffFailure;
    assertThat(unsupportedBrandsSniffFailure.majorBrand).isEqualTo(1767992930);
    assertThat(unsupportedBrandsSniffFailure.compatibleBrands.asList())
        .containsExactly(1919903851, 1835102819, 1953459817, 1801548922)
        .inOrder();
  }

  @Test
  public void sniff_reportsWrongFragmentationFailure() throws Exception {
    FragmentedMp4Extractor extractor =
        new FragmentedMp4Extractor(SubtitleParser.Factory.UNSUPPORTED);
    FakeExtractorInput input = createInputForSample("sample.mp4");

    boolean sniffResult = extractor.sniff(input);
    ImmutableList<SniffFailure> sniffFailures = extractor.getSniffFailureDetails();

    assertThat(sniffResult).isFalse();
    SniffFailure sniffFailure = Iterables.getOnlyElement(sniffFailures);
    assertThat(sniffFailure).isInstanceOf(IncorrectFragmentationSniffFailure.class);
    IncorrectFragmentationSniffFailure incorrectFragmentationSniffFailure =
        (IncorrectFragmentationSniffFailure) sniffFailure;
    assertThat(incorrectFragmentationSniffFailure.fileIsFragmented).isFalse();
  }

  private static FakeExtractorInput createInputForSample(String sample) throws IOException {
    return new FakeExtractorInput.Builder()
        .setData(
            TestUtil.getByteArray(
                ApplicationProvider.getApplicationContext(), "media/mp4/" + sample))
        .build();
  }
}
