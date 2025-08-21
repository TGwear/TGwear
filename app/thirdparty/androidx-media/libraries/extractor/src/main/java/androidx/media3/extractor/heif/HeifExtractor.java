/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.heif;

import androidx.media3.common.C;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.extractor.Extractor;
import androidx.media3.extractor.ExtractorInput;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.PositionHolder;
import androidx.media3.extractor.SingleSampleExtractor;
import java.io.IOException;

/** Extracts data from the HEIF (.heic) container format. */
@UnstableApi
public final class HeifExtractor implements Extractor {

  // Specification reference: ISO/IEC 23008-12:2022
  private static final int HEIF_FILE_SIGNATURE_PART_1 = 0x66747970;
  private static final int HEIF_FILE_SIGNATURE_PART_2 = 0x68656963;
  private static final int FILE_SIGNATURE_SEGMENT_LENGTH = 4;

  private final ParsableByteArray scratch;
  private final SingleSampleExtractor imageExtractor;

  /** Creates an instance. */
  public HeifExtractor() {
    scratch = new ParsableByteArray(FILE_SIGNATURE_SEGMENT_LENGTH);
    imageExtractor = new SingleSampleExtractor(C.INDEX_UNSET, C.LENGTH_UNSET, MimeTypes.IMAGE_HEIF);
  }

  @Override
  public boolean sniff(ExtractorInput input) throws IOException {
    input.advancePeekPosition(4);
    return readAndCompareFourBytes(input, HEIF_FILE_SIGNATURE_PART_1)
        && readAndCompareFourBytes(input, HEIF_FILE_SIGNATURE_PART_2);
  }

  @Override
  public void init(ExtractorOutput output) {
    imageExtractor.init(output);
  }

  @Override
  public @ReadResult int read(ExtractorInput input, PositionHolder seekPosition)
      throws IOException {
    return imageExtractor.read(input, seekPosition);
  }

  @Override
  public void seek(long position, long timeUs) {
    imageExtractor.seek(position, timeUs);
  }

  @Override
  public void release() {
    // Do nothing.
  }

  private boolean readAndCompareFourBytes(ExtractorInput input, int bytesToCompare)
      throws IOException {
    scratch.reset(/* limit= */ FILE_SIGNATURE_SEGMENT_LENGTH);
    input.peekFully(scratch.getData(), /* offset= */ 0, FILE_SIGNATURE_SEGMENT_LENGTH);
    return scratch.readUnsignedInt() == bytesToCompare;
  }
}
