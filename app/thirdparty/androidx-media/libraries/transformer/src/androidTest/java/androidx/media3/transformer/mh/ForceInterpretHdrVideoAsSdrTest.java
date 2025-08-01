/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.transformer.mh;

import static androidx.media3.common.util.Util.SDK_INT;
import static androidx.media3.test.utils.TestUtil.retrieveTrackFormat;
import static androidx.media3.transformer.AndroidTestUtil.MP4_ASSET_1080P_5_SECOND_HLG10;
import static androidx.media3.transformer.AndroidTestUtil.MP4_ASSET_720P_4_SECOND_HDR10;
import static androidx.media3.transformer.AndroidTestUtil.assumeFormatsSupported;
import static androidx.media3.transformer.AndroidTestUtil.recordTestSkipped;
import static com.google.common.truth.Truth.assertThat;

import android.content.Context;
import android.net.Uri;
import androidx.media3.common.C;
import androidx.media3.common.ColorInfo;
import androidx.media3.common.Format;
import androidx.media3.common.MediaItem;
import androidx.media3.transformer.Composition;
import androidx.media3.transformer.EditedMediaItem;
import androidx.media3.transformer.EditedMediaItemSequence;
import androidx.media3.transformer.ExportTestResult;
import androidx.media3.transformer.Transformer;
import androidx.media3.transformer.TransformerAndroidTestRunner;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;

/**
 * {@link Transformer} instrumentation test for {@linkplain
 * Composition#HDR_MODE_EXPERIMENTAL_FORCE_INTERPRET_HDR_AS_SDR forcing HDR contents to be
 * interpreted as SDR}.
 */
@RunWith(AndroidJUnit4.class)
public class ForceInterpretHdrVideoAsSdrTest {
  @Rule public final TestName testName = new TestName();

  private String testId;

  @Before
  public void setUpTestId() {
    testId = testName.getMethodName();
  }

  @Test
  public void forceInterpretHdrVideoAsSdrTest_hdr10File_transformsOrThrows() throws Exception {
    Context context = ApplicationProvider.getApplicationContext();

    if (SDK_INT < 29) {
      // TODO: b/269759013 - Fix failures under API 29 to expand confidence on all API versions.
      recordTestSkipped(
          context, testId, /* reason= */ "Under API 29, this API is considered best-effort.");
      return;
    }

    // Force interpret HDR as SDR signals SDR input to the decoder, even if the actual input is HDR.
    Format decoderInputFormat =
        MP4_ASSET_720P_4_SECOND_HDR10
            .videoFormat
            .buildUpon()
            .setColorInfo(ColorInfo.SDR_BT709_LIMITED)
            .build();
    assumeFormatsSupported(context, testId, decoderInputFormat, /* outputFormat= */ null);

    Transformer transformer = new Transformer.Builder(context).build();
    EditedMediaItem editedMediaItem =
        new EditedMediaItem.Builder(MediaItem.fromUri(Uri.parse(MP4_ASSET_720P_4_SECOND_HDR10.uri)))
            .build();
    Composition composition =
        new Composition.Builder(new EditedMediaItemSequence.Builder(editedMediaItem).build())
            .setHdrMode(Composition.HDR_MODE_EXPERIMENTAL_FORCE_INTERPRET_HDR_AS_SDR)
            .build();
    ExportTestResult exportTestResult =
        new TransformerAndroidTestRunner.Builder(context, transformer)
            .build()
            .run(testId, composition);

    @C.ColorTransfer
    int actualColorTransfer =
        retrieveTrackFormat(context, exportTestResult.filePath, C.TRACK_TYPE_VIDEO)
            .colorInfo
            .colorTransfer;
    assertThat(actualColorTransfer).isEqualTo(C.COLOR_TRANSFER_SDR);
  }

  @Test
  public void forceInterpretHdrVideoAsSdrTest_hlg10File_transformsOrThrows() throws Exception {
    Context context = ApplicationProvider.getApplicationContext();

    if (SDK_INT < 29) {
      // TODO: b/269759013 - Fix failures under API 29 to expand confidence on all API versions.
      recordTestSkipped(
          context, testId, /* reason= */ "Under API 29, this API is considered best-effort.");
      return;
    }

    // Force interpret HDR as SDR signals SDR input to the decoder, even if the actual input is HDR.
    Format decoderInputFormat =
        MP4_ASSET_1080P_5_SECOND_HLG10
            .videoFormat
            .buildUpon()
            .setColorInfo(ColorInfo.SDR_BT709_LIMITED)
            .build();
    assumeFormatsSupported(context, testId, decoderInputFormat, /* outputFormat= */ null);

    Transformer transformer = new Transformer.Builder(context).build();
    EditedMediaItem editedMediaItem =
        new EditedMediaItem.Builder(
                MediaItem.fromUri(Uri.parse(MP4_ASSET_1080P_5_SECOND_HLG10.uri)))
            .build();
    Composition composition =
        new Composition.Builder(new EditedMediaItemSequence.Builder(editedMediaItem).build())
            .setHdrMode(Composition.HDR_MODE_EXPERIMENTAL_FORCE_INTERPRET_HDR_AS_SDR)
            .build();
    ExportTestResult exportTestResult =
        new TransformerAndroidTestRunner.Builder(context, transformer)
            .build()
            .run(testId, composition);

    @C.ColorTransfer
    int actualColorTransfer =
        retrieveTrackFormat(context, exportTestResult.filePath, C.TRACK_TYPE_VIDEO)
            .colorInfo
            .colorTransfer;
    assertThat(actualColorTransfer).isEqualTo(C.COLOR_TRANSFER_SDR);
  }
}
