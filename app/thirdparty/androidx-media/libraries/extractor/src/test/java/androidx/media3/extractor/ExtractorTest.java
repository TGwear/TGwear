/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor;

import static com.google.common.truth.Truth.assertThat;

import androidx.media3.common.C;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit test for {@link Extractor}. */
@RunWith(AndroidJUnit4.class)
public final class ExtractorTest {

  @Test
  public void constants() {
    // Check that constant values match those defined by {@link C}.
    assertThat(Extractor.RESULT_END_OF_INPUT).isEqualTo(C.RESULT_END_OF_INPUT);
    // Check that the other constant values don't overlap.
    assertThat(C.RESULT_END_OF_INPUT != Extractor.RESULT_CONTINUE).isTrue();
    assertThat(C.RESULT_END_OF_INPUT != Extractor.RESULT_SEEK).isTrue();
  }
}
