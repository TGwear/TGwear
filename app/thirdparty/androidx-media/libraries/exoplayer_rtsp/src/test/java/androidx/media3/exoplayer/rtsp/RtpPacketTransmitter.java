/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.rtsp;

import androidx.media3.common.util.Clock;
import androidx.media3.common.util.HandlerWrapper;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.rtsp.RtspMessageChannel.InterleavedBinaryDataListener;
import com.google.common.collect.ImmutableList;

/** Transmits media RTP packets periodically. */
/* package */ final class RtpPacketTransmitter {

  private static final byte[] END_OF_STREAM = new byte[0];

  private final ImmutableList<String> packets;
  private final HandlerWrapper transmissionHandler;
  private final long transmissionIntervalMs;

  private RtspMessageChannel.InterleavedBinaryDataListener binaryDataListener;
  private int packetIndex;
  private volatile boolean isTransmitting;

  /**
   * Creates a new instance.
   *
   * @param rtpPacketStreamDump The {@link RtpPacketStreamDump} to provide RTP packets.
   * @param clock The {@link Clock} to use.
   */
  public RtpPacketTransmitter(RtpPacketStreamDump rtpPacketStreamDump, Clock clock) {
    this.packets = ImmutableList.copyOf(rtpPacketStreamDump.packets);
    this.transmissionHandler =
        clock.createHandler(Util.getCurrentOrMainLooper(), /* callback= */ null);
    this.transmissionIntervalMs = rtpPacketStreamDump.transmissionIntervalMs;
  }

  /**
   * Starts transmitting binary data to the {@link InterleavedBinaryDataListener}.
   *
   * <p>Calling this method after starting the transmission has no effect.
   */
  public void startTransmitting(InterleavedBinaryDataListener binaryDataListener) {
    if (isTransmitting) {
      return;
    }

    this.binaryDataListener = binaryDataListener;
    packetIndex = 0;
    isTransmitting = true;
    transmissionHandler.post(this::transmitNextPacket);
  }

  /** Stops transmitting, if transmitting has started. */
  private void stopTransmitting() {
    if (!isTransmitting) {
      return;
    }

    signalEndOfStream();
    transmissionHandler.removeCallbacksAndMessages(/* token= */ null);
    isTransmitting = false;
  }

  private void transmitNextPacket() {
    if (packetIndex == packets.size()) {
      stopTransmitting();
      return;
    }

    byte[] data = Util.getBytesFromHexString(packets.get(packetIndex++));
    binaryDataListener.onInterleavedBinaryDataReceived(data);
    transmissionHandler.postDelayed(this::transmitNextPacket, transmissionIntervalMs);
  }

  private void signalEndOfStream() {
    binaryDataListener.onInterleavedBinaryDataReceived(END_OF_STREAM);
  }
}
