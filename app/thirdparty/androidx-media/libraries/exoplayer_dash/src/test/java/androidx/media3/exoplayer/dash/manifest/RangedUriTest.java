/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.dash.manifest;

import static com.google.common.truth.Truth.assertThat;

import androidx.media3.common.C;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit test for {@link RangedUri}. */
@RunWith(AndroidJUnit4.class)
public class RangedUriTest {

  private static final String BASE_URI = "http://www.test.com/";
  private static final String PARTIAL_URI = "path/file.ext";
  private static final String FULL_URI = BASE_URI + PARTIAL_URI;

  @Test
  public void merge() {
    RangedUri rangeA = new RangedUri(FULL_URI, 0, 10);
    RangedUri rangeB = new RangedUri(FULL_URI, 10, 10);
    RangedUri expected = new RangedUri(FULL_URI, 0, 20);
    assertMerge(rangeA, rangeB, expected, null);
  }

  @Test
  public void mergeUnbounded() {
    RangedUri rangeA = new RangedUri(FULL_URI, 0, 10);
    RangedUri rangeB = new RangedUri(FULL_URI, 10, C.LENGTH_UNSET);
    RangedUri expected = new RangedUri(FULL_URI, 0, C.LENGTH_UNSET);
    assertMerge(rangeA, rangeB, expected, null);
  }

  @Test
  public void nonMerge() {
    // A and B do not overlap, so should not merge
    RangedUri rangeA = new RangedUri(FULL_URI, 0, 10);
    RangedUri rangeB = new RangedUri(FULL_URI, 11, 10);
    assertNonMerge(rangeA, rangeB, null);

    // A and B do not overlap, so should not merge
    rangeA = new RangedUri(FULL_URI, 0, 10);
    rangeB = new RangedUri(FULL_URI, 11, C.LENGTH_UNSET);
    assertNonMerge(rangeA, rangeB, null);

    // A and B are bounded but overlap, so should not merge
    rangeA = new RangedUri(FULL_URI, 0, 11);
    rangeB = new RangedUri(FULL_URI, 10, 10);
    assertNonMerge(rangeA, rangeB, null);

    // A and B overlap due to unboundedness, so should not merge
    rangeA = new RangedUri(FULL_URI, 0, C.LENGTH_UNSET);
    rangeB = new RangedUri(FULL_URI, 10, C.LENGTH_UNSET);
    assertNonMerge(rangeA, rangeB, null);
  }

  @Test
  public void mergeWithBaseUri() {
    RangedUri rangeA = new RangedUri(PARTIAL_URI, 0, 10);
    RangedUri rangeB = new RangedUri(FULL_URI, 10, 10);
    RangedUri expected = new RangedUri(FULL_URI, 0, 20);
    assertMerge(rangeA, rangeB, expected, BASE_URI);
  }

  private void assertMerge(RangedUri rangeA, RangedUri rangeB, RangedUri expected, String baseUrl) {
    RangedUri merged = rangeA.attemptMerge(rangeB, baseUrl);
    assertThat(merged).isEqualTo(expected);
    merged = rangeB.attemptMerge(rangeA, baseUrl);
    assertThat(merged).isEqualTo(expected);
  }

  private void assertNonMerge(RangedUri rangeA, RangedUri rangeB, String baseUrl) {
    RangedUri merged = rangeA.attemptMerge(rangeB, baseUrl);
    assertThat(merged).isNull();
    merged = rangeB.attemptMerge(rangeA, baseUrl);
    assertThat(merged).isNull();
  }
}
