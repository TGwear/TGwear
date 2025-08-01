/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.rtsp;

/** Factory for {@link TransferRtpDataChannel}. */
/* package */ final class TransferRtpDataChannelFactory implements RtpDataChannel.Factory {

  private static final int INTERLEAVED_CHANNELS_PER_TRACK = 2;

  private final long timeoutMs;

  /**
   * Creates a new instance.
   *
   * @param timeoutMs A positive number of milliseconds to wait before lack of received RTP packets
   *     is treated as the end of input.
   */
  public TransferRtpDataChannelFactory(long timeoutMs) {
    this.timeoutMs = timeoutMs;
  }

  @Override
  public RtpDataChannel createAndOpenDataChannel(int trackId) {
    TransferRtpDataChannel dataChannel = new TransferRtpDataChannel(timeoutMs);
    dataChannel.open(RtpUtils.getIncomingRtpDataSpec(trackId * INTERLEAVED_CHANNELS_PER_TRACK));
    return dataChannel;
  }
}
