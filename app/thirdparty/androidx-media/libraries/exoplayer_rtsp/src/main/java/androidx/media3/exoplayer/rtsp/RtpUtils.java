/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.rtsp;

import android.net.Uri;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.DataSpec;

/** Utility methods for RTP. */
@UnstableApi
public final class RtpUtils {

  private static final String RTP_ANY_INCOMING_IPV4 = "rtp://0.0.0.0";

  /** Returns the {@link DataSpec} with the {@link Uri} for incoming RTP connection. */
  public static DataSpec getIncomingRtpDataSpec(int portNumber) {
    return new DataSpec(
        Uri.parse(Util.formatInvariant("%s:%d", RTP_ANY_INCOMING_IPV4, portNumber)));
  }

  private RtpUtils() {}
}
