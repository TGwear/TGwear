/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package androidx.media3.common;

import static com.google.common.base.Preconditions.checkState;
import static com.google.common.truth.Truth.assertThat;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.common.truth.Expect;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Tests for {@link MediaLibraryInfo}. */
@RunWith(AndroidJUnit4.class)
public class MediaLibraryInfoTest {

  private static final Pattern VERSION_PATTERN =
      Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)(?:-(alpha|beta|rc)(\\d\\d))?");

  @Rule public final Expect expect = Expect.create();

  @Test
  public void versionAndSlashyAreConsistent() {
    assertThat(MediaLibraryInfo.VERSION_SLASHY)
        .isEqualTo("AndroidXMedia3/" + MediaLibraryInfo.VERSION);
  }

  @Test
  public void versionIntIsSelfConsistentAndConsistentWithVersionString() {
    // Use the Truth .matches() call so any failure has a clearer error message, then call
    // Matcher#matches() below so the subsequent group(int) calls work.
    assertThat(MediaLibraryInfo.VERSION).matches(VERSION_PATTERN);
    Matcher matcher = VERSION_PATTERN.matcher(MediaLibraryInfo.VERSION);
    checkState(matcher.matches());

    int major = Integer.parseInt(matcher.group(1));
    int minor = Integer.parseInt(matcher.group(2));
    int bugfix = Integer.parseInt(matcher.group(3));
    String phase = matcher.group(4);

    expect.that(major).isAtLeast(1);

    int expectedVersionInt = 0;
    expectedVersionInt += major * 1_000_000_000;
    expectedVersionInt += minor * 1_000_000;
    expectedVersionInt += bugfix * 1000;

    int phaseInt;
    if (phase != null) {
      expect.that(bugfix).isEqualTo(0);
      switch (phase) {
        case "alpha":
          phaseInt = 0;
          break;
        case "beta":
          phaseInt = 1;
          break;
        case "rc":
          phaseInt = 2;
          break;
        default:
          throw new AssertionError("Unrecognized phase: " + phase);
      }
      int phaseCount = Integer.parseInt(matcher.group(5));
      expect.that(phaseCount).isAtLeast(1);
      expectedVersionInt += phaseCount;
    } else {
      // phase == null, so this is a stable or bugfix release.
      phaseInt = 3;
    }
    expectedVersionInt += phaseInt * 100;
    expect
        .withMessage("VERSION_INT for " + MediaLibraryInfo.VERSION)
        .that(MediaLibraryInfo.VERSION_INT)
        .isEqualTo(expectedVersionInt);
  }
}
