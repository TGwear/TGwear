/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.rtsp;

import androidx.annotation.Nullable;
import androidx.media3.common.C;
import androidx.media3.datasource.DataSource;
import androidx.media3.exoplayer.rtsp.RtspMessageChannel.InterleavedBinaryDataListener;
import java.io.IOException;

/** An RTP {@link DataSource}. */
/* package */ interface RtpDataChannel extends DataSource {

  /** Creates {@link RtpDataChannel} for RTSP streams. */
  interface Factory {

    /**
     * Creates a new {@link RtpDataChannel} instance for RTP data transfer.
     *
     * @param trackId The track ID.
     * @throws IOException If the data channels failed to open.
     */
    RtpDataChannel createAndOpenDataChannel(int trackId) throws IOException;

    /** Returns a fallback {@code Factory}, {@code null} when there is no fallback available. */
    @Nullable
    default Factory createFallbackDataChannelFactory() {
      return null;
    }
  }

  /** Returns the RTSP transport header for this {@link RtpDataChannel} */
  String getTransport();

  /**
   * Returns the receiving port or channel used by the underlying transport protocol, {@link
   * C#INDEX_UNSET} if the data channel is not opened.
   */
  int getLocalPort();

  /** Returns whether the {@code RtpDataChannel} needs to be closed when loading completes. */
  boolean needsClosingOnLoadCompletion();

  /**
   * Returns a {@link InterleavedBinaryDataListener} if the implementation supports receiving RTP
   * packets on a side-band protocol, for example RTP-over-RTSP; otherwise {@code null}.
   */
  @Nullable
  InterleavedBinaryDataListener getInterleavedBinaryDataListener();
}
