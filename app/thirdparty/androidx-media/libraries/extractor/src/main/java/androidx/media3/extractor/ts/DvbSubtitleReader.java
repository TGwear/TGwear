/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.ts;

import static androidx.media3.common.util.Assertions.checkState;
import static androidx.media3.extractor.ts.TsPayloadReader.FLAG_DATA_ALIGNMENT_INDICATOR;

import androidx.media3.common.C;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.TrackOutput;
import androidx.media3.extractor.ts.TsPayloadReader.DvbSubtitleInfo;
import androidx.media3.extractor.ts.TsPayloadReader.TrackIdGenerator;
import java.util.Collections;
import java.util.List;

/** Parses DVB subtitle data and extracts individual frames. */
@UnstableApi
public final class DvbSubtitleReader implements ElementaryStreamReader {

  private final List<DvbSubtitleInfo> subtitleInfos;
  private final String containerMimeType;
  private final TrackOutput[] outputs;

  private boolean writingSample;
  private int bytesToCheck;
  private int sampleBytesWritten;
  private long sampleTimeUs;

  /**
   * @param subtitleInfos Information about the DVB subtitles associated to the stream.
   * @param containerMimeType The MIME type of the container holding the stream.
   */
  public DvbSubtitleReader(List<DvbSubtitleInfo> subtitleInfos, String containerMimeType) {
    this.subtitleInfos = subtitleInfos;
    this.containerMimeType = containerMimeType;
    outputs = new TrackOutput[subtitleInfos.size()];
    sampleTimeUs = C.TIME_UNSET;
  }

  @Override
  public void seek() {
    writingSample = false;
    sampleTimeUs = C.TIME_UNSET;
  }

  @Override
  public void createTracks(ExtractorOutput extractorOutput, TrackIdGenerator idGenerator) {
    for (int i = 0; i < outputs.length; i++) {
      DvbSubtitleInfo subtitleInfo = subtitleInfos.get(i);
      idGenerator.generateNewId();
      TrackOutput output = extractorOutput.track(idGenerator.getTrackId(), C.TRACK_TYPE_TEXT);
      output.format(
          new Format.Builder()
              .setId(idGenerator.getFormatId())
              .setContainerMimeType(containerMimeType)
              .setSampleMimeType(MimeTypes.APPLICATION_DVBSUBS)
              .setInitializationData(Collections.singletonList(subtitleInfo.initializationData))
              .setLanguage(subtitleInfo.language)
              .build());
      outputs[i] = output;
    }
  }

  @Override
  public void packetStarted(long pesTimeUs, @TsPayloadReader.Flags int flags) {
    if ((flags & FLAG_DATA_ALIGNMENT_INDICATOR) == 0) {
      return;
    }
    writingSample = true;
    sampleTimeUs = pesTimeUs;
    sampleBytesWritten = 0;
    bytesToCheck = 2;
  }

  @Override
  public void packetFinished(boolean isEndOfInput) {
    if (writingSample) {
      // packetStarted method must be called before reading sample.
      checkState(sampleTimeUs != C.TIME_UNSET);
      for (TrackOutput output : outputs) {
        output.sampleMetadata(sampleTimeUs, C.BUFFER_FLAG_KEY_FRAME, sampleBytesWritten, 0, null);
      }
      writingSample = false;
    }
  }

  @Override
  public void consume(ParsableByteArray data) {
    if (writingSample) {
      if (bytesToCheck == 2 && !checkNextByte(data, 0x20)) {
        // Failed to check data_identifier
        return;
      }
      if (bytesToCheck == 1 && !checkNextByte(data, 0x00)) {
        // Check and discard the subtitle_stream_id
        return;
      }
      int dataPosition = data.getPosition();
      int bytesAvailable = data.bytesLeft();
      for (TrackOutput output : outputs) {
        data.setPosition(dataPosition);
        output.sampleData(data, bytesAvailable);
      }
      sampleBytesWritten += bytesAvailable;
    }
  }

  private boolean checkNextByte(ParsableByteArray data, int expectedValue) {
    if (data.bytesLeft() == 0) {
      return false;
    }
    if (data.readUnsignedByte() != expectedValue) {
      writingSample = false;
    }
    bytesToCheck--;
    return writingSample;
  }
}
