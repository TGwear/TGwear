/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package androidx.media3.exoplayer.rtsp.reader;

import androidx.annotation.Nullable;
import androidx.media3.common.ParserException;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.rtsp.RtpPayloadFormat;
import androidx.media3.extractor.ExtractorOutput;

/** Extracts media samples from the payload of received RTP packets. */
@UnstableApi
public interface RtpPayloadReader {

  /** Factory of {@link RtpPayloadReader} instances. */
  interface Factory {

    /**
     * Returns a {@link RtpPayloadReader} for a given {@link RtpPayloadFormat}.
     *
     * @param payloadFormat The {@link RtpPayloadFormat} of the RTP stream.
     * @return A {@link RtpPayloadReader} for the packet stream, or {@code null} if the stream
     *     format is not supported.
     */
    @Nullable
    RtpPayloadReader createPayloadReader(RtpPayloadFormat payloadFormat);
  }

  /**
   * Initializes the reader by providing its output and track id.
   *
   * @param extractorOutput The {@link ExtractorOutput} instance that receives the extracted data.
   * @param trackId The track identifier to set on the format.
   */
  void createTracks(ExtractorOutput extractorOutput, int trackId);

  /**
   * This method should be called on reading the first packet in a stream of incoming packets.
   *
   * @param timestamp The timestamp associated with the first received RTP packet. This number has
   *     no unit, the duration conveyed by it depends on the frequency of the media that the RTP
   *     packet is carrying.
   * @param sequenceNumber The sequence associated with the first received RTP packet.
   */
  void onReceivingFirstPacket(long timestamp, int sequenceNumber);

  /**
   * Consumes the payload from the an RTP packet.
   *
   * @param data The RTP payload to consume.
   * @param timestamp The timestamp of the RTP packet that transmitted the data. This number has no
   *     unit, the duration conveyed by it depends on the frequency of the media that the RTP packet
   *     is carrying.
   * @param sequenceNumber The sequence number of the RTP packet.
   * @param rtpMarker The marker bit of the RTP packet. The interpretation of this bit is specific
   *     to each payload format.
   * @throws ParserException If the data could not be parsed.
   */
  void consume(ParsableByteArray data, long timestamp, int sequenceNumber, boolean rtpMarker)
      throws ParserException;

  /**
   * Seeks the reader.
   *
   * <p>This method must only be invoked after the PLAY request for seeking is acknowledged by the
   * RTSP server.
   *
   * @param nextRtpTimestamp The timestamp of the first packet to arrive after seek.
   * @param timeUs The server acknowledged seek time in microseconds.
   */
  void seek(long nextRtpTimestamp, long timeUs);
}
