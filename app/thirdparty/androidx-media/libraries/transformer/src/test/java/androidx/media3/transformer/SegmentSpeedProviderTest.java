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

import androidx.media3.common.Metadata;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.metadata.mp4.SlowMotionData;
import androidx.media3.extractor.metadata.mp4.SlowMotionData.Segment;
import androidx.media3.extractor.metadata.mp4.SmtaMetadataEntry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.common.collect.ImmutableList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit tests for {@link SegmentSpeedProvider}. */
@RunWith(AndroidJUnit4.class)
public class SegmentSpeedProviderTest {

  private static final SmtaMetadataEntry SMTA_SPEED_8 =
      new SmtaMetadataEntry(/* captureFrameRate= */ 240, /* svcTemporalLayerCount= */ 4);

  @Test
  public void getSpeed_noSegments_returnsBaseSpeed() {
    SegmentSpeedProvider provider = new SegmentSpeedProvider(new Metadata(SMTA_SPEED_8));
    assertThat(provider.getSpeed(0)).isEqualTo(8);
    assertThat(provider.getSpeed(1_000_000)).isEqualTo(8);
  }

  @Test
  public void getSpeed_returnsCorrectSpeed() {
    List<Segment> segments =
        ImmutableList.of(
            new Segment(/* startTimeMs= */ 500, /* endTimeMs= */ 1000, /* speedDivisor= */ 8),
            new Segment(/* startTimeMs= */ 1500, /* endTimeMs= */ 2000, /* speedDivisor= */ 4),
            new Segment(/* startTimeMs= */ 2000, /* endTimeMs= */ 2500, /* speedDivisor= */ 2));

    SegmentSpeedProvider provider =
        new SegmentSpeedProvider(new Metadata(new SlowMotionData(segments), SMTA_SPEED_8));

    assertThat(provider.getSpeed(Util.msToUs(0))).isEqualTo(8);
    assertThat(provider.getSpeed(Util.msToUs(500))).isEqualTo(1);
    assertThat(provider.getSpeed(Util.msToUs(800))).isEqualTo(1);
    assertThat(provider.getSpeed(Util.msToUs(1000))).isEqualTo(8);
    assertThat(provider.getSpeed(Util.msToUs(1250))).isEqualTo(8);
    assertThat(provider.getSpeed(Util.msToUs(1500))).isEqualTo(2);
    assertThat(provider.getSpeed(Util.msToUs(1650))).isEqualTo(2);
    assertThat(provider.getSpeed(Util.msToUs(2000))).isEqualTo(4);
    assertThat(provider.getSpeed(Util.msToUs(2400))).isEqualTo(4);
    assertThat(provider.getSpeed(Util.msToUs(2500))).isEqualTo(8);
    assertThat(provider.getSpeed(Util.msToUs(3000))).isEqualTo(8);
  }

  @Test
  public void getSpeed_withNegativeTimestamp_throwsException() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new SegmentSpeedProvider(new Metadata(SMTA_SPEED_8)).getSpeed(-1));
  }
}
