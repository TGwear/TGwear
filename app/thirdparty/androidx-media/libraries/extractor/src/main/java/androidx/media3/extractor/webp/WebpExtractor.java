/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.webp;

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

/** Extracts data from the WEBP container format. */
@UnstableApi
public final class WebpExtractor implements Extractor {

  // Documentation reference:
  // https://developers.google.com/speed/webp/docs/riff_container#webp_file_header
  private static final int FILE_SIGNATURE_SEGMENT_LENGTH = 4;
  private static final int RIFF_FILE_SIGNATURE = 0x52494646;
  private static final int WEBP_FILE_SIGNATURE = 0x57454250;

  private final ParsableByteArray scratch;
  private final SingleSampleExtractor imageExtractor;

  /** Creates an instance. */
  public WebpExtractor() {
    scratch = new ParsableByteArray(FILE_SIGNATURE_SEGMENT_LENGTH);
    imageExtractor = new SingleSampleExtractor(C.INDEX_UNSET, C.LENGTH_UNSET, MimeTypes.IMAGE_WEBP);
  }

  @Override
  public boolean sniff(ExtractorInput input) throws IOException {
    // The full file signature for the format webp is RIFF????WEBP.
    scratch.reset(/* limit= */ FILE_SIGNATURE_SEGMENT_LENGTH);
    input.peekFully(scratch.getData(), /* offset= */ 0, FILE_SIGNATURE_SEGMENT_LENGTH);
    if (scratch.readUnsignedInt() != RIFF_FILE_SIGNATURE) {
      return false;
    }

    input.advancePeekPosition(FILE_SIGNATURE_SEGMENT_LENGTH);
    scratch.reset(/* limit= */ FILE_SIGNATURE_SEGMENT_LENGTH);

    input.peekFully(scratch.getData(), /* offset= */ 0, FILE_SIGNATURE_SEGMENT_LENGTH);
    return scratch.readUnsignedInt() == WEBP_FILE_SIGNATURE;
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
}
