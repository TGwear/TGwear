/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.transformer;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import androidx.media3.common.MediaItem;
import androidx.media3.common.audio.AudioProcessor;
import androidx.media3.common.audio.SpeedChangingAudioProcessor;
import androidx.media3.test.utils.TestSpeedProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit test for {@link EditedMediaItem.Builder}. */
@RunWith(AndroidJUnit4.class)
public final class EditedMediaItemBuilderTest {

  @Test
  public void build_removeAudioAndVideo_throws() {
    MediaItem mediaItem = MediaItem.fromUri("uri");

    assertThrows(
        IllegalStateException.class,
        () ->
            new EditedMediaItem.Builder(mediaItem)
                .setRemoveAudio(true)
                .setRemoveVideo(true)
                .build());
  }

  @Test
  public void setFlattenForSlowMotion_forClippedMediaItem_throws() {
    MediaItem.ClippingConfiguration clippingConfiguration =
        new MediaItem.ClippingConfiguration.Builder().setStartPositionMs(1000).build();
    MediaItem mediaItem =
        new MediaItem.Builder()
            .setUri("Uri")
            .setClippingConfiguration(clippingConfiguration)
            .build();

    assertThrows(
        IllegalArgumentException.class,
        () -> new EditedMediaItem.Builder(mediaItem).setFlattenForSlowMotion(true).build());
  }

  @Test
  public void presentationDurationUs_withoutClippingConfiguration() {
    MediaItem mediaItem = MediaItem.fromUri("Uri");

    EditedMediaItem editedMediaItem =
        new EditedMediaItem.Builder(mediaItem).setDurationUs(1_000).build();

    assertThat(editedMediaItem.getPresentationDurationUs()).isEqualTo(1_000);
  }

  @Test
  public void presentationDurationUs_withClippingConfigurationAndEndPosition() {
    MediaItem.ClippingConfiguration clippingConfiguration =
        new MediaItem.ClippingConfiguration.Builder().setEndPositionMs(500).build();
    MediaItem mediaItem =
        new MediaItem.Builder()
            .setUri("Uri")
            .setClippingConfiguration(clippingConfiguration)
            .build();

    EditedMediaItem editedMediaItem =
        new EditedMediaItem.Builder(mediaItem).setDurationUs(1_000_000).build();

    assertThat(editedMediaItem.getPresentationDurationUs()).isEqualTo(500_000);
  }

  @Test
  public void presentationDurationUs_withClippingConfigurationAndStartEndPosition() {
    MediaItem.ClippingConfiguration clippingConfiguration =
        new MediaItem.ClippingConfiguration.Builder()
            // 300_000us
            .setStartPositionMs(300)
            // 500_000us
            .setEndPositionMs(500)
            .build();
    MediaItem mediaItem =
        new MediaItem.Builder()
            .setUri("Uri")
            .setClippingConfiguration(clippingConfiguration)
            .build();

    EditedMediaItem editedMediaItem =
        new EditedMediaItem.Builder(mediaItem).setDurationUs(1_000_000).build();

    assertThat(editedMediaItem.getPresentationDurationUs()).isEqualTo(200_000);
  }

  @Test
  public void presentationDurationUs_withClippingConfigurationAndStartEndPositionInUs() {
    MediaItem.ClippingConfiguration clippingConfiguration =
        new MediaItem.ClippingConfiguration.Builder()
            .setStartPositionUs(300_000)
            .setEndPositionUs(500_000)
            .build();
    MediaItem mediaItem =
        new MediaItem.Builder()
            .setUri("Uri")
            .setClippingConfiguration(clippingConfiguration)
            .build();

    EditedMediaItem editedMediaItem =
        new EditedMediaItem.Builder(mediaItem).setDurationUs(1_000_000).build();

    assertThat(editedMediaItem.getPresentationDurationUs()).isEqualTo(200_000);
  }

  @Test
  public void presentationDurationUs_withClippingConfigurationAndStartPosition() {
    MediaItem.ClippingConfiguration clippingConfiguration =
        new MediaItem.ClippingConfiguration.Builder()
            // 300_000us
            .setStartPositionMs(300)
            .build();
    MediaItem mediaItem =
        new MediaItem.Builder()
            .setUri("Uri")
            .setClippingConfiguration(clippingConfiguration)
            .build();

    EditedMediaItem editedMediaItem =
        new EditedMediaItem.Builder(mediaItem).setDurationUs(1_000_000).build();

    assertThat(editedMediaItem.getPresentationDurationUs()).isEqualTo(700_000);
  }

  @Test
  public void presentationDurationUs_withClippingConfigurationAndDurationAdjustingEffect() {
    MediaItem.ClippingConfiguration clippingConfiguration =
        new MediaItem.ClippingConfiguration.Builder().setStartPositionUs(200_000).build();
    MediaItem mediaItem =
        new MediaItem.Builder()
            .setUri("Uri")
            .setClippingConfiguration(clippingConfiguration)
            .build();
    ImmutableList<AudioProcessor> audioProcessors =
        ImmutableList.of(
            new SpeedChangingAudioProcessor(
                TestSpeedProvider.createWithStartTimes(new long[] {0L}, new float[] {2f})));
    EditedMediaItem editedMediaItem =
        new EditedMediaItem.Builder(mediaItem)
            .setDurationUs(1_000_000)
            .setRemoveVideo(true)
            .setEffects(new Effects(audioProcessors, /* videoEffects= */ ImmutableList.of()))
            .build();

    assertThat(editedMediaItem.getPresentationDurationUs()).isEqualTo(400_000);
  }

  @Test
  public void presentationDurationUs_withDurationAdjustingEffect() {
    MediaItem mediaItem = new MediaItem.Builder().setUri("Uri").build();
    ImmutableList<AudioProcessor> audioProcessors =
        ImmutableList.of(
            new SpeedChangingAudioProcessor(
                TestSpeedProvider.createWithStartTimes(new long[] {0L}, new float[] {2f})));
    EditedMediaItem editedMediaItem =
        new EditedMediaItem.Builder(mediaItem)
            .setDurationUs(1_000_000)
            .setRemoveVideo(true)
            .setEffects(new Effects(audioProcessors, /* videoEffects= */ ImmutableList.of()))
            .build();

    assertThat(editedMediaItem.getPresentationDurationUs()).isEqualTo(500_000);
  }
}
