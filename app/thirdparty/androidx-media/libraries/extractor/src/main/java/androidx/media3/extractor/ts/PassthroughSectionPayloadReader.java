/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.ts;

import androidx.media3.common.C;
import androidx.media3.common.Format;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.TimestampAdjuster;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.common.util.Util;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.TrackOutput;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

/**
 * A {@link SectionPayloadReader} that directly outputs the section bytes as sample data.
 *
 * <p>Timestamp adjustment is provided through {@link Format#subsampleOffsetUs}.
 */
@UnstableApi
public final class PassthroughSectionPayloadReader implements SectionPayloadReader {

  private Format format;
  private @MonotonicNonNull TimestampAdjuster timestampAdjuster;
  private @MonotonicNonNull TrackOutput output;

  /**
   * Create a new PassthroughSectionPayloadReader.
   *
   * @param sampleMimeType The MIME type set as {@link Format#sampleMimeType} on the created output
   *     track.
   * @param containerMimeType The MIME type set as {@link Format#containerMimeType} on the created
   *     output track.
   */
  public PassthroughSectionPayloadReader(String sampleMimeType, String containerMimeType) {
    this.format =
        new Format.Builder()
            .setContainerMimeType(containerMimeType)
            .setSampleMimeType(sampleMimeType)
            .build();
  }

  @Override
  public void init(
      TimestampAdjuster timestampAdjuster,
      ExtractorOutput extractorOutput,
      TsPayloadReader.TrackIdGenerator idGenerator) {
    this.timestampAdjuster = timestampAdjuster;
    idGenerator.generateNewId();
    output = extractorOutput.track(idGenerator.getTrackId(), C.TRACK_TYPE_METADATA);
    // Eagerly output an incomplete format (missing timestamp offset) to ensure source preparation
    // is not blocked waiting for potentially sparse metadata.
    output.format(format);
  }

  @Override
  public void consume(ParsableByteArray sectionData) {
    assertInitialized();
    long sampleTimestampUs = timestampAdjuster.getLastAdjustedTimestampUs();
    long subsampleOffsetUs = timestampAdjuster.getTimestampOffsetUs();
    if (sampleTimestampUs == C.TIME_UNSET || subsampleOffsetUs == C.TIME_UNSET) {
      // Don't output samples without a known sample timestamp and subsample offset.
      return;
    }
    if (subsampleOffsetUs != format.subsampleOffsetUs) {
      format = format.buildUpon().setSubsampleOffsetUs(subsampleOffsetUs).build();
      output.format(format);
    }
    int sampleSize = sectionData.bytesLeft();
    output.sampleData(sectionData, sampleSize);
    output.sampleMetadata(sampleTimestampUs, C.BUFFER_FLAG_KEY_FRAME, sampleSize, 0, null);
  }

  @EnsuresNonNull({"timestampAdjuster", "output"})
  private void assertInitialized() {
    Assertions.checkStateNotNull(timestampAdjuster);
    Util.castNonNull(output);
  }
}
