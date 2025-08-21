/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.transformer;

import static androidx.media3.common.util.Assertions.checkArgument;
import static androidx.media3.extractor.metadata.mp4.SlowMotionData.Segment.BY_START_THEN_END_THEN_DIVISOR;

import androidx.annotation.Nullable;
import androidx.media3.common.C;
import androidx.media3.common.Metadata;
import androidx.media3.common.audio.SpeedProvider;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.metadata.mp4.SlowMotionData;
import androidx.media3.extractor.metadata.mp4.SlowMotionData.Segment;
import androidx.media3.extractor.metadata.mp4.SmtaMetadataEntry;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/** A {@link SpeedProvider} for slow motion segments. */
/* package */ class SegmentSpeedProvider implements SpeedProvider {

  private static final int INPUT_FRAME_RATE = 30;

  private final ImmutableSortedMap<Long, Float> speedsByStartTimeUs;
  private final float baseSpeedMultiplier;

  public SegmentSpeedProvider(Metadata metadata) {
    float captureFrameRate = getCaptureFrameRate(metadata);
    this.baseSpeedMultiplier =
        captureFrameRate == C.RATE_UNSET ? 1 : captureFrameRate / INPUT_FRAME_RATE;
    this.speedsByStartTimeUs = buildSpeedByStartTimeUsMap(metadata, baseSpeedMultiplier);
  }

  @Override
  public float getSpeed(long timeUs) {
    checkArgument(timeUs >= 0);
    @Nullable Map.Entry<Long, Float> entry = speedsByStartTimeUs.floorEntry(timeUs);
    return entry != null ? entry.getValue() : baseSpeedMultiplier;
  }

  @Override
  public long getNextSpeedChangeTimeUs(long timeUs) {
    checkArgument(timeUs >= 0);
    @Nullable Long nextTimeUs = speedsByStartTimeUs.higherKey(timeUs);
    return nextTimeUs != null ? nextTimeUs : C.TIME_UNSET;
  }

  private static ImmutableSortedMap<Long, Float> buildSpeedByStartTimeUsMap(
      Metadata metadata, float baseSpeed) {
    ImmutableList<Segment> segments = extractSlowMotionSegments(metadata);

    if (segments.isEmpty()) {
      return ImmutableSortedMap.of();
    }

    TreeMap<Long, Float> speedsByStartTimeUs = new TreeMap<>();

    // Start time maps to the segment speed.
    for (int i = 0; i < segments.size(); i++) {
      Segment currentSegment = segments.get(i);
      speedsByStartTimeUs.put(
          Util.msToUs(currentSegment.startTimeMs), baseSpeed / currentSegment.speedDivisor);
    }

    // If the map has an entry at endTime, this is the next segments start time. If no such entry
    // exists, map the endTime to base speed because the times after the end time are not in a
    // segment.
    for (int i = 0; i < segments.size(); i++) {
      Segment currentSegment = segments.get(i);
      if (!speedsByStartTimeUs.containsKey(Util.msToUs(currentSegment.endTimeMs))) {
        speedsByStartTimeUs.put(Util.msToUs(currentSegment.endTimeMs), baseSpeed);
      }
    }

    return ImmutableSortedMap.copyOf(speedsByStartTimeUs);
  }

  private static float getCaptureFrameRate(Metadata metadata) {
    for (int i = 0; i < metadata.length(); i++) {
      Metadata.Entry entry = metadata.get(i);
      if (entry instanceof SmtaMetadataEntry) {
        return ((SmtaMetadataEntry) entry).captureFrameRate;
      }
    }

    return C.RATE_UNSET;
  }

  private static ImmutableList<Segment> extractSlowMotionSegments(Metadata metadata) {
    List<Segment> segments = new ArrayList<>();
    for (int i = 0; i < metadata.length(); i++) {
      Metadata.Entry entry = metadata.get(i);
      if (entry instanceof SlowMotionData) {
        segments.addAll(((SlowMotionData) entry).segments);
      }
    }
    return ImmutableList.sortedCopyOf(BY_START_THEN_END_THEN_DIVISOR, segments);
  }
}
