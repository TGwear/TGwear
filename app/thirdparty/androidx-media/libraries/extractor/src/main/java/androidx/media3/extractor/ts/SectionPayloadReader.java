/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.ts;

import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.TimestampAdjuster;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.TrackOutput;
import androidx.media3.extractor.ts.TsPayloadReader.TrackIdGenerator;

/** Reads section data. */
@UnstableApi
public interface SectionPayloadReader {

  /**
   * Initializes the section payload reader.
   *
   * @param timestampAdjuster A timestamp adjuster for offsetting and scaling sample timestamps.
   * @param extractorOutput The {@link ExtractorOutput} that receives the extracted data.
   * @param idGenerator A {@link PesReader.TrackIdGenerator} that generates unique track ids for the
   *     {@link TrackOutput}s.
   */
  void init(
      TimestampAdjuster timestampAdjuster,
      ExtractorOutput extractorOutput,
      TrackIdGenerator idGenerator);

  /**
   * Called by a {@link SectionReader} when a full section is received.
   *
   * @param sectionData The data belonging to a section starting from the table_id. If
   *     section_syntax_indicator is set to '1', {@code sectionData} excludes the CRC_32 field.
   *     Otherwise, all bytes belonging to the table section are included.
   */
  void consume(ParsableByteArray sectionData);
}
