/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.transformer;

import static androidx.media3.common.util.Assertions.checkNotNull;
import static androidx.media3.transformer.AndroidTestUtil.MP4_ASSET;
import static androidx.media3.transformer.AndroidTestUtil.assumeFormatsSupported;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assume.assumeTrue;

import android.content.Context;
import android.net.Uri;
import androidx.media3.common.Effect;
import androidx.media3.common.MediaItem;
import androidx.media3.common.audio.ChannelMixingAudioProcessor;
import androidx.media3.common.audio.ChannelMixingMatrix;
import androidx.media3.effect.RgbFilter;
import androidx.test.core.app.ApplicationProvider;
import com.google.common.collect.ImmutableList;
import java.io.File;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

/** End-to-end instrumentation test for {@link Transformer} with {@link InAppMp4Muxer}. */
@RunWith(Parameterized.class)
public class TransformerWithInAppMp4MuxerEndToEndAndroidTest {
  private static final String MP4_FILE_ASSET_DIRECTORY = "asset:///media/mp4/";
  private static final String H264_MP4 = "sample_no_bframes.mp4";
  private static final String H265_MP4 = "h265_with_metadata_track.mp4";

  @Parameters(name = "{0}")
  public static ImmutableList<String> mediaFiles() {
    return ImmutableList.of(H264_MP4, H265_MP4);
  }

  @Parameter public @MonotonicNonNull String inputFile;

  private final Context context = ApplicationProvider.getApplicationContext();

  @Test
  public void videoEditing_completesSuccessfully() throws Exception {
    String testId = "videoEditing_completesSuccessfully_" + inputFile;
    // Use MP4_ASSET_FORMAT for H265_MP4_ASSET_URI_STRING test skipping as well, because emulators
    // signal a lack of support for H265_MP4's actual format, but pass this test when using
    // MP4_ASSET_FORMAT for skipping.
    assumeFormatsSupported(
        context,
        testId,
        /* inputFormat= */ MP4_ASSET.videoFormat,
        /* outputFormat= */ MP4_ASSET.videoFormat);
    Transformer transformer =
        new Transformer.Builder(context).setMuxerFactory(new InAppMp4Muxer.Factory()).build();
    ImmutableList<Effect> videoEffects = ImmutableList.of(RgbFilter.createGrayscaleFilter());
    MediaItem mediaItem = MediaItem.fromUri(Uri.parse(MP4_FILE_ASSET_DIRECTORY + inputFile));
    EditedMediaItem editedMediaItem =
        new EditedMediaItem.Builder(mediaItem)
            .setEffects(new Effects(/* audioProcessors= */ ImmutableList.of(), videoEffects))
            .build();

    ExportTestResult result =
        new TransformerAndroidTestRunner.Builder(context, transformer)
            .build()
            .run(testId, editedMediaItem);

    assertThat(new File(result.filePath).length()).isGreaterThan(0);
  }

  @Test
  public void audioEditing_completesSuccessfully() throws Exception {
    // The test does not need not to be parameterised because it only needs to run for a single
    // audio format (AAC).
    assumeTrue(checkNotNull(inputFile).equals(H264_MP4));
    String testId = "audioEditing_completesSuccessfully";
    Transformer transformer =
        new Transformer.Builder(context).setMuxerFactory(new InAppMp4Muxer.Factory()).build();
    ChannelMixingAudioProcessor channelMixingAudioProcessor = new ChannelMixingAudioProcessor();
    channelMixingAudioProcessor.putChannelMixingMatrix(
        ChannelMixingMatrix.create(/* inputChannelCount= */ 1, /* outputChannelCount= */ 2));
    MediaItem mediaItem = MediaItem.fromUri(Uri.parse(MP4_FILE_ASSET_DIRECTORY + H264_MP4));
    EditedMediaItem editedMediaItem =
        new EditedMediaItem.Builder(mediaItem)
            .setEffects(
                new Effects(
                    ImmutableList.of(channelMixingAudioProcessor),
                    /* videoEffects= */ ImmutableList.of()))
            .build();

    ExportTestResult result =
        new TransformerAndroidTestRunner.Builder(context, transformer)
            .build()
            .run(testId, editedMediaItem);

    assertThat(new File(result.filePath).length()).isGreaterThan(0);
  }
}
