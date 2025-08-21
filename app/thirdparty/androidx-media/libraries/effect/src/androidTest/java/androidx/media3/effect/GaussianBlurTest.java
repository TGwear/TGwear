/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.effect;

import static androidx.media3.effect.EffectsTestUtil.generateAndProcessFrames;
import static androidx.media3.effect.EffectsTestUtil.getAndAssertOutputBitmaps;
import static com.google.common.truth.Truth.assertThat;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import androidx.media3.common.util.Consumer;
import androidx.media3.test.utils.TextureBitmapReader;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.common.collect.ImmutableList;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;

/** Tests for {@link GaussianBlur}. */
@RunWith(AndroidJUnit4.class)
public class GaussianBlurTest {
  @Rule public final TestName testName = new TestName();

  private static final String ASSET_PATH = "test-generated-goldens/GaussianBlurTest";
  private static final int BLANK_FRAME_WIDTH = 200;
  private static final int BLANK_FRAME_HEIGHT = 100;
  private static final Consumer<SpannableString> TEXT_SPAN_CONSUMER =
      (text) -> {
        text.setSpan(
            new BackgroundColorSpan(Color.BLUE),
            /* start= */ 0,
            text.length(),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(
            new ForegroundColorSpan(Color.WHITE),
            /* start= */ 0,
            text.length(),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(
            new AbsoluteSizeSpan(/* size= */ 100),
            /* start= */ 0,
            text.length(),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(
            new TypefaceSpan(/* family= */ "sans-serif"),
            /* start= */ 0,
            text.length(),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      };

  private String testId;
  private @MonotonicNonNull TextureBitmapReader textureBitmapReader;

  @Before
  public void setUp() {
    textureBitmapReader = new TextureBitmapReader();
    testId = testName.getMethodName();
  }

  // Golden images for these tests were generated on an API 33 emulator. API 26 emulators have a
  // different text rendering implementation that leads to a larger pixel difference.

  @Test
  public void gaussianBlur_blursFrame() throws Exception {
    ImmutableList<Long> frameTimesUs = ImmutableList.of(22_000L);
    ImmutableList<Long> actualPresentationTimesUs =
        generateAndProcessFrames(
            BLANK_FRAME_WIDTH,
            BLANK_FRAME_HEIGHT,
            frameTimesUs,
            new GaussianBlur(/* sigma= */ 5f),
            textureBitmapReader,
            TEXT_SPAN_CONSUMER);

    assertThat(actualPresentationTimesUs).containsExactly(22_000L);
    getAndAssertOutputBitmaps(textureBitmapReader, actualPresentationTimesUs, testId, ASSET_PATH);
  }

  @Test
  public void gaussianBlur_withNegativeCoefficients_blursFrame() throws Exception {
    GaussianFunction gaussianFunction =
        new GaussianFunction(/* sigma= */ 5f, /* numStandardDeviations= */ 2f);
    ImmutableList<Long> frameTimesUs = ImmutableList.of(22_000L);
    ImmutableList<Long> actualPresentationTimesUs =
        generateAndProcessFrames(
            BLANK_FRAME_WIDTH,
            BLANK_FRAME_HEIGHT,
            frameTimesUs,
            new SeparableConvolution() {
              @Override
              public ConvolutionFunction1D getConvolution(long presentationTimeUs) {
                return new ConvolutionFunction1D() {

                  @Override
                  public float domainStart() {
                    return gaussianFunction.domainStart();
                  }

                  @Override
                  public float domainEnd() {
                    return gaussianFunction.domainEnd();
                  }

                  @Override
                  public float value(float samplePosition) {
                    return -gaussianFunction.value(samplePosition);
                  }
                };
              }
            },
            textureBitmapReader,
            TEXT_SPAN_CONSUMER);

    assertThat(actualPresentationTimesUs).containsExactly(22_000L);
    getAndAssertOutputBitmaps(textureBitmapReader, actualPresentationTimesUs, testId, ASSET_PATH);
  }
}
