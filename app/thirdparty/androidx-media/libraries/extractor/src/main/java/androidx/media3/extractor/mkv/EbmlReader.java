/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.mkv;

import androidx.media3.common.ParserException;
import androidx.media3.extractor.ExtractorInput;
import java.io.IOException;

/**
 * Event-driven EBML reader that delivers events to an {@link EbmlProcessor}.
 *
 * <p>EBML can be summarized as a binary XML format somewhat similar to Protocol Buffers. It was
 * originally designed for the Matroska container format. More information about EBML and Matroska
 * is available <a href="http://www.matroska.org/technical/specs/index.html">here</a>.
 */
/* package */ interface EbmlReader {

  /**
   * Initializes the extractor with an {@link EbmlProcessor}.
   *
   * @param processor An {@link EbmlProcessor} to process events.
   */
  void init(EbmlProcessor processor);

  /**
   * Resets the state of the reader.
   *
   * <p>Subsequent calls to {@link #read(ExtractorInput)} will start reading a new EBML structure
   * from scratch.
   */
  void reset();

  /**
   * Reads from an {@link ExtractorInput}, invoking an event callback if possible.
   *
   * @param input The {@link ExtractorInput} from which data should be read.
   * @return True if data can continue to be read. False if the end of the input was encountered.
   * @throws ParserException If parsing fails.
   * @throws IOException If an error occurs reading from the input.
   */
  boolean read(ExtractorInput input) throws IOException;
}
