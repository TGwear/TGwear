/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.mediacodec;

import android.media.MediaCodec;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.mediacodec.MediaCodecUtil.DecoderQueryException;
import java.util.List;

/** Selector of {@link MediaCodec} instances. */
@UnstableApi
public interface MediaCodecSelector {

  /**
   * Default implementation of {@link MediaCodecSelector}, which returns the preferred decoder for
   * the given format.
   */
  MediaCodecSelector DEFAULT = MediaCodecUtil::getDecoderInfos;

  /**
   * Implementation of {@link MediaCodecSelector}, which returns the {@link #DEFAULT} list of
   * decoders for the given format, giving higher priority to software decoders.
   */
  MediaCodecSelector PREFER_SOFTWARE =
      (String mimeType, boolean requiresSecureDecoder, boolean requiresTunnelingDecoder) ->
          MediaCodecUtil.getDecoderInfosSortedBySoftwareOnly(
              DEFAULT.getDecoderInfos(mimeType, requiresSecureDecoder, requiresTunnelingDecoder));

  /**
   * Returns a list of decoders that can decode media in the specified MIME type, in priority order.
   *
   * @param mimeType The MIME type for which a decoder is required.
   * @param requiresSecureDecoder Whether a secure decoder is required.
   * @param requiresTunnelingDecoder Whether a tunneling decoder is required.
   * @return An unmodifiable list of {@link MediaCodecInfo}s corresponding to decoders. May be
   *     empty.
   * @throws DecoderQueryException Thrown if there was an error querying decoders.
   */
  List<MediaCodecInfo> getDecoderInfos(
      String mimeType, boolean requiresSecureDecoder, boolean requiresTunnelingDecoder)
      throws DecoderQueryException;
}
