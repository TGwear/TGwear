/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.transformer;

import static androidx.media3.transformer.AndroidTestUtil.BT601_MP4_ASSET;
import static androidx.media3.transformer.AndroidTestUtil.JPG_ASSET;
import static androidx.media3.transformer.AndroidTestUtil.MP4_ASSET;
import static androidx.media3.transformer.AndroidTestUtil.PNG_ASSET;
import static androidx.media3.transformer.AndroidTestUtil.assumeFormatsSupported;
import static androidx.media3.transformer.AndroidTestUtil.getMuxerFactoryBasedOnApi;
import static com.google.common.truth.Truth.assertThat;

import android.content.Context;
import androidx.media3.common.C;
import androidx.media3.common.MediaItem;
import androidx.media3.effect.Presentation;
import androidx.media3.effect.RgbFilter;
import androidx.media3.transformer.ParameterizedAndroidTestUtil.ItemConfig;
import androidx.media3.transformer.ParameterizedAndroidTestUtil.SdrImageItemConfig;
import androidx.media3.transformer.ParameterizedAndroidTestUtil.VideoItemConfig;
import androidx.test.core.app.ApplicationProvider;
import com.google.common.collect.ImmutableList;
import java.io.File;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

/**
 * Parameterized end-to-end test for exporting a single {@link MediaItem} or {@link
 * EditedMediaItem}.
 */
@RunWith(Parameterized.class)
public final class ParameterizedItemExportAndroidTest {
  private static final SdrImageItemConfig PNG_ITEM =
      new SdrImageItemConfig(
          PNG_ASSET,
          /* frameRate= */ 34,
          /* durationUs= */ C.MICROS_PER_SECOND,
          /* effects= */ Effects.EMPTY);
  private static final SdrImageItemConfig PNG_ITEM_WITH_EFFECTS =
      new SdrImageItemConfig(
          PNG_ASSET,
          /* frameRate= */ 34,
          /* durationUs= */ C.MICROS_PER_SECOND,
          /* effects= */ new Effects(
              /* audioProcessors= */ ImmutableList.of(),
              ImmutableList.of(RgbFilter.createGrayscaleFilter())));
  private static final SdrImageItemConfig JPG_ITEM =
      new SdrImageItemConfig(
          JPG_ASSET,
          /* frameRate= */ 41,
          /* durationUs= */ C.MICROS_PER_SECOND,
          /* effects= */ Effects.EMPTY);
  private static final SdrImageItemConfig JPG_ITEM_WITH_EFFECTS =
      new SdrImageItemConfig(
          JPG_ASSET,
          /* frameRate= */ 41,
          /* durationUs= */ C.MICROS_PER_SECOND,
          /* effects= */ new Effects(
              /* audioProcessors= */ ImmutableList.of(),
              ImmutableList.of(RgbFilter.createGrayscaleFilter())));
  private static final VideoItemConfig BT709_ITEM = new VideoItemConfig(MP4_ASSET, Effects.EMPTY);
  private static final VideoItemConfig BT709_ITEM_WITH_EFFECTS =
      new VideoItemConfig(
          MP4_ASSET,
          new Effects(
              /* audioProcessors= */ ImmutableList.of(),
              ImmutableList.of(
                  Presentation.createForWidthAndHeight(
                      480, 360, Presentation.LAYOUT_SCALE_TO_FIT_WITH_CROP))));
  private static final VideoItemConfig BT601_ITEM =
      new VideoItemConfig(BT601_MP4_ASSET, Effects.EMPTY);
  private static final VideoItemConfig BT601_ITEM_WITH_EFFECTS =
      new VideoItemConfig(
          BT601_MP4_ASSET,
          new Effects(
              /* audioProcessors= */ ImmutableList.of(),
              ImmutableList.of(
                  Presentation.createForWidthAndHeight(
                      400, 300, Presentation.LAYOUT_SCALE_TO_FIT_WITH_CROP))));

  @Parameters(name = "{0}")
  public static ImmutableList<ItemConfig> params() {
    return ImmutableList.of(
        PNG_ITEM,
        PNG_ITEM_WITH_EFFECTS,
        JPG_ITEM,
        JPG_ITEM_WITH_EFFECTS,
        BT709_ITEM,
        BT709_ITEM_WITH_EFFECTS,
        BT601_ITEM,
        BT601_ITEM_WITH_EFFECTS);
  }

  @Rule public final TestName testName = new TestName();

  @Parameter public ItemConfig itemConfig;

  @Test
  public void export_completesWithCorrectFrameCount() throws Exception {
    Context context = ApplicationProvider.getApplicationContext();
    String testId = testName.getMethodName();
    assumeFormatsSupported(context, testId, itemConfig.format, itemConfig.outputFormat);
    Transformer transformer =
        new Transformer.Builder(context)
            .setMuxerFactory(getMuxerFactoryBasedOnApi())
            .setEncoderFactory(
                new DefaultEncoderFactory.Builder(context).setEnableFallback(false).build())
            .build();

    ExportTestResult result =
        new TransformerAndroidTestRunner.Builder(context, transformer)
            .build()
            .run(testId, itemConfig.build());

    assertThat(result.exportResult.videoFrameCount).isEqualTo(itemConfig.frameCount);
    assertThat(new File(result.filePath).length()).isGreaterThan(0);
  }
}
