/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.flv;

import androidx.media3.common.C;
import androidx.media3.common.ParserException;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.extractor.TrackOutput;

/** Extracts individual samples from FLV tags, preserving original order. */
/* package */ abstract class TagPayloadReader {

  /** Thrown when the format is not supported. */
  public static final class UnsupportedFormatException extends ParserException {

    public UnsupportedFormatException(String msg) {
      super(msg, /* cause= */ null, /* contentIsMalformed= */ false, C.DATA_TYPE_MEDIA);
    }
  }

  protected final TrackOutput output;

  /**
   * @param output A {@link TrackOutput} to which samples should be written.
   */
  protected TagPayloadReader(TrackOutput output) {
    this.output = output;
  }

  /**
   * Notifies the reader that a seek has occurred.
   *
   * <p>Following a call to this method, the data passed to the next invocation of {@link
   * #consume(ParsableByteArray, long)} will not be a continuation of the data that was previously
   * passed. Hence the reader should reset any internal state.
   */
  public abstract void seek();

  /**
   * Consumes payload data.
   *
   * @param data The payload data to consume.
   * @param timeUs The timestamp associated with the payload.
   * @return Whether a sample was output.
   * @throws ParserException If an error occurs parsing the data.
   */
  public final boolean consume(ParsableByteArray data, long timeUs) throws ParserException {
    return parseHeader(data) && parsePayload(data, timeUs);
  }

  /**
   * Parses tag header.
   *
   * @param data Buffer where the tag header is stored.
   * @return Whether the header was parsed successfully.
   * @throws ParserException If an error occurs parsing the header.
   */
  protected abstract boolean parseHeader(ParsableByteArray data) throws ParserException;

  /**
   * Parses tag payload.
   *
   * @param data Buffer where tag payload is stored.
   * @param timeUs Time position of the frame.
   * @return Whether a sample was output.
   * @throws ParserException If an error occurs parsing the payload.
   */
  protected abstract boolean parsePayload(ParsableByteArray data, long timeUs)
      throws ParserException;
}
