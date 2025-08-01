/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.transformer.mh.analysis;

import static androidx.media3.common.util.Assertions.checkNotNull;
import static androidx.media3.transformer.AndroidTestUtil.FORCE_TRANSCODE_VIDEO_EFFECTS;
import static com.google.common.truth.Truth.assertWithMessage;

import android.content.Context;
import android.net.Uri;
import androidx.media3.common.MediaItem;
import androidx.media3.transformer.AndroidTestUtil;
import androidx.media3.transformer.EditedMediaItem;
import androidx.media3.transformer.ExportTestResult;
import androidx.media3.transformer.Transformer;
import androidx.media3.transformer.TransformerAndroidTestRunner;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import java.util.HashSet;
import java.util.Set;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Tests repeated transcoding operations (as a stress test and to help reproduce flakiness). */
@RunWith(AndroidJUnit4.class)
@Ignore(
    "Analysis tests are not used for confirming Transformer is running properly, and not configured"
        + " for this use as they're missing skip checks for unsupported devices.")
public final class RepeatedTranscodeTest {
  private static final int TRANSCODE_COUNT = 10;

  @Test
  public void repeatedTranscode_givesConsistentLengthOutput() throws Exception {
    Context context = ApplicationProvider.getApplicationContext();

    TransformerAndroidTestRunner transformerRunner =
        new TransformerAndroidTestRunner.Builder(
                context,
                new Transformer.Builder(context)
                    .setEncoderFactory(new AndroidTestUtil.ForceEncodeEncoderFactory(context))
                    .build())
            .build();
    MediaItem mediaItem = MediaItem.fromUri(Uri.parse(AndroidTestUtil.MP4_REMOTE_10_SECONDS.uri));
    EditedMediaItem editedMediaItem =
        new EditedMediaItem.Builder(mediaItem).setEffects(FORCE_TRANSCODE_VIDEO_EFFECTS).build();

    Set<Long> differentOutputSizesBytes = new HashSet<>();
    for (int i = 0; i < TRANSCODE_COUNT; i++) {
      // Use a long video in case an error occurs a while after the start of the video.
      ExportTestResult testResult =
          transformerRunner.run(
              /* testId= */ "repeatedTranscode_givesConsistentLengthOutput_" + i, editedMediaItem);
      differentOutputSizesBytes.add(checkNotNull(testResult.exportResult.fileSizeBytes));
    }

    assertWithMessage(
            "Different transcoding output sizes detected. Sizes: " + differentOutputSizesBytes)
        .that(differentOutputSizesBytes.size())
        .isEqualTo(1);
  }

  @Test
  public void repeatedTranscodeNoAudio_givesConsistentLengthOutput() throws Exception {
    Context context = ApplicationProvider.getApplicationContext();
    TransformerAndroidTestRunner transformerRunner =
        new TransformerAndroidTestRunner.Builder(
                context,
                new Transformer.Builder(context)
                    .setEncoderFactory(new AndroidTestUtil.ForceEncodeEncoderFactory(context))
                    .build())
            .build();
    MediaItem mediaItem = MediaItem.fromUri(Uri.parse(AndroidTestUtil.MP4_REMOTE_10_SECONDS.uri));
    EditedMediaItem editedMediaItem =
        new EditedMediaItem.Builder(mediaItem)
            .setRemoveAudio(true)
            .setEffects(FORCE_TRANSCODE_VIDEO_EFFECTS)
            .build();

    Set<Long> differentOutputSizesBytes = new HashSet<>();
    for (int i = 0; i < TRANSCODE_COUNT; i++) {
      // Use a long video in case an error occurs a while after the start of the video.
      ExportTestResult testResult =
          transformerRunner.run(
              /* testId= */ "repeatedTranscodeNoAudio_givesConsistentLengthOutput_" + i,
              editedMediaItem);
      differentOutputSizesBytes.add(checkNotNull(testResult.exportResult.fileSizeBytes));
    }

    assertWithMessage(
            "Different transcoding output sizes detected. Sizes: " + differentOutputSizesBytes)
        .that(differentOutputSizesBytes.size())
        .isEqualTo(1);
  }

  @Test
  public void repeatedTranscodeNoVideo_givesConsistentLengthOutput() throws Exception {
    Context context = ApplicationProvider.getApplicationContext();
    TransformerAndroidTestRunner transformerRunner =
        new TransformerAndroidTestRunner.Builder(
                context,
                new Transformer.Builder(context)
                    .setEncoderFactory(new AndroidTestUtil.ForceEncodeEncoderFactory(context))
                    .build())
            .build();
    MediaItem mediaItem = MediaItem.fromUri(Uri.parse(AndroidTestUtil.MP4_REMOTE_10_SECONDS.uri));
    EditedMediaItem editedMediaItem =
        new EditedMediaItem.Builder(mediaItem).setRemoveVideo(true).build();

    Set<Long> differentOutputSizesBytes = new HashSet<>();
    for (int i = 0; i < TRANSCODE_COUNT; i++) {
      // Use a long video in case an error occurs a while after the start of the video.
      ExportTestResult testResult =
          transformerRunner.run(
              /* testId= */ "repeatedTranscodeNoVideo_givesConsistentLengthOutput_" + i,
              editedMediaItem);
      differentOutputSizesBytes.add(checkNotNull(testResult.exportResult.fileSizeBytes));
    }

    assertWithMessage(
            "Different transcoding output sizes detected. Sizes: " + differentOutputSizesBytes)
        .that(differentOutputSizesBytes.size())
        .isEqualTo(1);
  }
}
