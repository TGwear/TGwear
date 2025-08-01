/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.ts;

import static androidx.media3.extractor.Ac4Util.AC40_SYNCWORD;
import static androidx.media3.extractor.Ac4Util.AC41_SYNCWORD;
import static androidx.media3.extractor.metadata.id3.Id3Decoder.ID3_HEADER_LENGTH;
import static androidx.media3.extractor.metadata.id3.Id3Decoder.ID3_TAG;
import static androidx.media3.extractor.ts.TsPayloadReader.FLAG_DATA_ALIGNMENT_INDICATOR;

import androidx.media3.common.C;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.extractor.Ac4Util;
import androidx.media3.extractor.Extractor;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.ExtractorsFactory;
import androidx.media3.extractor.PositionHolder;
import androidx.media3.extractor.SeekMap;
import androidx.media3.extractor.ts.TsPayloadReader.TrackIdGenerator;
import java.io.IOException;

/** Extracts data from AC-4 bitstreams. */
@UnstableApi
public final class Ac4Extractor implements Extractor {

  /** Factory for {@link Ac4Extractor} instances. */
  public static final ExtractorsFactory FACTORY = () -> new Extractor[] {new Ac4Extractor()};

  /**
   * The maximum number of bytes to search when sniffing, excluding ID3 information, before giving
   * up.
   */
  private static final int MAX_SNIFF_BYTES = 8 * 1024;

  /**
   * The size of the reading buffer, in bytes. This value is determined based on the maximum frame
   * size used in broadcast applications.
   */
  private static final int READ_BUFFER_SIZE = 16384;

  /** The size of the frame header, in bytes. */
  private static final int FRAME_HEADER_SIZE = 7;

  private final Ac4Reader reader;
  private final ParsableByteArray sampleData;

  private boolean startedPacket;

  /** Creates a new extractor for AC-4 bitstreams. */
  public Ac4Extractor() {
    reader = new Ac4Reader(MimeTypes.AUDIO_AC4);
    sampleData = new ParsableByteArray(READ_BUFFER_SIZE);
  }

  // Extractor implementation.

  @Override
  public boolean sniff(ExtractorInput input) throws IOException {
    // Skip any ID3 headers.
    ParsableByteArray scratch = new ParsableByteArray(ID3_HEADER_LENGTH);
    int startPosition = 0;
    while (true) {
      input.peekFully(scratch.getData(), /* offset= */ 0, ID3_HEADER_LENGTH);
      scratch.setPosition(0);
      if (scratch.readUnsignedInt24() != ID3_TAG) {
        break;
      }
      scratch.skipBytes(3); // version, flags
      int length = scratch.readSynchSafeInt();
      startPosition += 10 + length;
      input.advancePeekPosition(length);
    }
    input.resetPeekPosition();
    input.advancePeekPosition(startPosition);

    int headerPosition = startPosition;
    int validFramesCount = 0;
    while (true) {
      input.peekFully(scratch.getData(), /* offset= */ 0, /* length= */ FRAME_HEADER_SIZE);
      scratch.setPosition(0);
      int syncBytes = scratch.readUnsignedShort();
      if (syncBytes != AC40_SYNCWORD && syncBytes != AC41_SYNCWORD) {
        validFramesCount = 0;
        input.resetPeekPosition();
        if (++headerPosition - startPosition >= MAX_SNIFF_BYTES) {
          return false;
        }
        input.advancePeekPosition(headerPosition);
      } else {
        if (++validFramesCount >= 4) {
          return true;
        }
        int frameSize = Ac4Util.parseAc4SyncframeSize(scratch.getData(), syncBytes);
        if (frameSize == C.LENGTH_UNSET) {
          return false;
        }
        input.advancePeekPosition(frameSize - FRAME_HEADER_SIZE);
      }
    }
  }

  @Override
  public void init(ExtractorOutput output) {
    reader.createTracks(
        output, new TrackIdGenerator(/* firstTrackId= */ 0, /* trackIdIncrement= */ 1));
    output.endTracks();
    output.seekMap(new SeekMap.Unseekable(/* durationUs= */ C.TIME_UNSET));
  }

  @Override
  public void seek(long position, long timeUs) {
    startedPacket = false;
    reader.seek();
  }

  @Override
  public void release() {
    // Do nothing.
  }

  @Override
  public int read(ExtractorInput input, PositionHolder seekPosition) throws IOException {
    int bytesRead =
        input.read(sampleData.getData(), /* offset= */ 0, /* length= */ READ_BUFFER_SIZE);
    if (bytesRead == C.RESULT_END_OF_INPUT) {
      return RESULT_END_OF_INPUT;
    }

    // Feed whatever data we have to the reader, regardless of whether the read finished or not.
    sampleData.setPosition(0);
    sampleData.setLimit(bytesRead);

    if (!startedPacket) {
      // Pass data to the reader as though it's contained within a single infinitely long packet.
      reader.packetStarted(/* pesTimeUs= */ 0, FLAG_DATA_ALIGNMENT_INDICATOR);
      startedPacket = true;
    }
    // TODO: Make it possible for the reader to consume the dataSource directly, so that it becomes
    // unnecessary to copy the data through packetBuffer.
    reader.consume(sampleData);
    return RESULT_CONTINUE;
  }
}
