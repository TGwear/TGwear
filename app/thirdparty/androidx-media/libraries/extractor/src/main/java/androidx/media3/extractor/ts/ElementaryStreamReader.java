/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.extractor.ts;

import androidx.media3.common.ParserException;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.extractor.ExtractorOutput;
import androidx.media3.extractor.TrackOutput;

/**
 * Extracts individual samples from an elementary media stream, preserving original order.
 *
 * <p>The expected sequence of method calls is as follows:
 *
 * <ol>
 *   <li>{@link #createTracks(ExtractorOutput, PesReader.TrackIdGenerator)} (once at initialization)
 *   <li>{@link #seek()} (optional, to reset the state)
 *   <li>{@link #packetStarted(long, int)} (to signal the start of a new packet)
 *   <li>{@link #consume(ParsableByteArray)} (zero or more times, to provide packet data)
 *   <li>{@link #packetFinished(boolean)} (to signal the end of the current packet)
 *   <li>Repeat steps 3-5 for subsequent packets
 * </ol>
 */
@UnstableApi
public interface ElementaryStreamReader {

  /** Notifies the reader that a seek has occurred. */
  void seek();

  /**
   * Initializes the reader by providing outputs and ids for the tracks.
   *
   * @param extractorOutput The {@link ExtractorOutput} that receives the extracted data.
   * @param idGenerator A {@link PesReader.TrackIdGenerator} that generates unique track ids for the
   *     {@link TrackOutput}s.
   */
  void createTracks(ExtractorOutput extractorOutput, PesReader.TrackIdGenerator idGenerator);

  /**
   * Called when a packet starts.
   *
   * @param pesTimeUs The timestamp associated with the packet.
   * @param flags See {@link TsPayloadReader.Flags}.
   */
  void packetStarted(long pesTimeUs, @TsPayloadReader.Flags int flags);

  /**
   * Consumes (possibly partial) data from the current packet.
   *
   * @param data The data to consume.
   * @throws ParserException If the data could not be parsed.
   */
  void consume(ParsableByteArray data) throws ParserException;

  /** Called when a packet ends. */
  void packetFinished(boolean isEndOfInput);
}
