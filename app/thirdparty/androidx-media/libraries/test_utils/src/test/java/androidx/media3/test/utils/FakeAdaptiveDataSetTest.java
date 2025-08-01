/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.test.utils;

import static com.google.common.truth.Truth.assertThat;

import androidx.media3.common.C;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.TrackGroup;
import androidx.media3.test.utils.FakeDataSet.FakeData;
import androidx.media3.test.utils.FakeDataSet.FakeData.Segment;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import java.util.List;
import java.util.Random;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit test for {@link FakeAdaptiveDataSet}. */
@RunWith(AndroidJUnit4.class)
public final class FakeAdaptiveDataSetTest {

  private static final Format[] TEST_FORMATS = {
    new Format.Builder()
        .setSampleMimeType(MimeTypes.VIDEO_H264)
        .setAverageBitrate(1_000_000)
        .setWidth(1280)
        .setHeight(720)
        .build(),
    new Format.Builder()
        .setSampleMimeType(MimeTypes.VIDEO_H264)
        .setAverageBitrate(300_000)
        .setWidth(640)
        .setHeight(360)
        .build()
  };
  private static final TrackGroup TRACK_GROUP = new TrackGroup(TEST_FORMATS);

  @Test
  public void testAdaptiveDataSet() {
    long chunkDuration = 2 * C.MICROS_PER_SECOND;
    FakeAdaptiveDataSet dataSet =
        new FakeAdaptiveDataSet(
            TRACK_GROUP, 10 * C.MICROS_PER_SECOND, chunkDuration, 0.0, new Random(0));
    assertThat(dataSet.getAllData().size()).isEqualTo(TEST_FORMATS.length);
    assertThat(dataSet.getUri(0).equals(dataSet.getUri(1))).isFalse();
    assertThat(dataSet.getChunkCount()).isEqualTo(5);
    assertThat(dataSet.getChunkIndexByPosition(4 * C.MICROS_PER_SECOND)).isEqualTo(2);
    assertThat(dataSet.getChunkIndexByPosition(9 * C.MICROS_PER_SECOND)).isEqualTo(4);
    for (int i = 0; i < dataSet.getChunkCount(); i++) {
      assertThat(dataSet.getChunkDuration(i)).isEqualTo(chunkDuration);
    }
    assertChunkData(dataSet, chunkDuration);
  }

  @Test
  public void testAdaptiveDataSetTrailingSmallChunk() {
    long chunkDuration = 3 * C.MICROS_PER_SECOND;
    FakeAdaptiveDataSet dataSet =
        new FakeAdaptiveDataSet(
            TRACK_GROUP, 10 * C.MICROS_PER_SECOND, chunkDuration, 0.0, new Random(0));
    assertThat(dataSet.getAllData().size()).isEqualTo(TEST_FORMATS.length);
    assertThat(dataSet.getUri(0).equals(dataSet.getUri(1))).isFalse();
    assertThat(dataSet.getChunkCount()).isEqualTo(4);
    assertThat(dataSet.getChunkIndexByPosition(4 * C.MICROS_PER_SECOND)).isEqualTo(1);
    assertThat(dataSet.getChunkIndexByPosition(9 * C.MICROS_PER_SECOND)).isEqualTo(3);
    for (int i = 0; i < dataSet.getChunkCount() - 1; i++) {
      assertThat(dataSet.getChunkDuration(i)).isEqualTo(chunkDuration);
    }
    assertThat(dataSet.getChunkDuration(3)).isEqualTo(C.MICROS_PER_SECOND);
    assertChunkData(dataSet, chunkDuration);
  }

  @Test
  public void testAdaptiveDataSetChunkSizeDistribution() {
    double expectedStdDev = 4.0;
    FakeAdaptiveDataSet dataSet =
        new FakeAdaptiveDataSet(
            TRACK_GROUP,
            100000 * C.MICROS_PER_SECOND,
            C.MICROS_PER_SECOND,
            expectedStdDev,
            new Random(0));
    for (int i = 0; i < TEST_FORMATS.length; i++) {
      FakeData data = dataSet.getData(dataSet.getUri(i));
      double mean = computeSegmentSizeMean(data.getSegments());
      double stddev = computeSegmentSizeStdDev(data.getSegments(), mean);
      double relativePercentStdDev = stddev / mean * 100.0;
      assertThat(relativePercentStdDev).isWithin(0.02).of(expectedStdDev);
      assertThat(mean * 8 / TEST_FORMATS[i].bitrate).isWithin(0.01).of(1.0);
    }
  }

  private void assertChunkData(FakeAdaptiveDataSet dataSet, long chunkDuration) {
    for (int i = 0; i < dataSet.getChunkCount(); i++) {
      assertThat(dataSet.getStartTime(i)).isEqualTo(chunkDuration * i);
    }
    for (int s = 0; s < TEST_FORMATS.length; s++) {
      FakeData data = dataSet.getData(dataSet.getUri(s));
      assertThat(data.getSegments().size()).isEqualTo(dataSet.getChunkCount());
      for (int i = 0; i < data.getSegments().size(); i++) {
        long expectedLength =
            TEST_FORMATS[s].bitrate * dataSet.getChunkDuration(i) / (8 * C.MICROS_PER_SECOND);
        assertThat(data.getSegments().get(i).length).isEqualTo(expectedLength);
      }
    }
  }

  private static double computeSegmentSizeMean(List<Segment> segments) {
    double totalSize = 0.0;
    for (Segment segment : segments) {
      totalSize += segment.length;
    }
    return totalSize / segments.size();
  }

  private static double computeSegmentSizeStdDev(List<Segment> segments, double mean) {
    double totalSquaredSize = 0.0;
    for (Segment segment : segments) {
      totalSquaredSize += (double) segment.length * segment.length;
    }
    return Math.sqrt(totalSquaredSize / segments.size() - mean * mean);
  }
}
