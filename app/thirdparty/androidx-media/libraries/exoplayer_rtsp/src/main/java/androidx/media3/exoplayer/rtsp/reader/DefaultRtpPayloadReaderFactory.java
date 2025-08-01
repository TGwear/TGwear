/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package androidx.media3.exoplayer.rtsp.reader;

import static androidx.media3.common.util.Assertions.checkNotNull;

import androidx.annotation.Nullable;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.rtsp.RtpPayloadFormat;

/** Default {@link RtpPayloadReader.Factory} implementation. */
@UnstableApi
public final class DefaultRtpPayloadReaderFactory implements RtpPayloadReader.Factory {

  @Override
  @Nullable
  public RtpPayloadReader createPayloadReader(RtpPayloadFormat payloadFormat) {
    switch (checkNotNull(payloadFormat.format.sampleMimeType)) {
      case MimeTypes.AUDIO_AC3:
        return new RtpAc3Reader(payloadFormat);
      case MimeTypes.AUDIO_AAC:
        if (payloadFormat.mediaEncoding.equals(RtpPayloadFormat.RTP_MEDIA_MPEG4_LATM_AUDIO)) {
          return new RtpMp4aReader(payloadFormat);
        } else {
          return new RtpAacReader(payloadFormat);
        }
      case MimeTypes.AUDIO_AMR_NB:
      case MimeTypes.AUDIO_AMR_WB:
        return new RtpAmrReader(payloadFormat);
      case MimeTypes.AUDIO_OPUS:
        return new RtpOpusReader(payloadFormat);
      case MimeTypes.AUDIO_RAW:
      case MimeTypes.AUDIO_ALAW:
      case MimeTypes.AUDIO_MLAW:
        return new RtpPcmReader(payloadFormat);
      case MimeTypes.VIDEO_H263:
        return new RtpH263Reader(payloadFormat);
      case MimeTypes.VIDEO_H264:
        return new RtpH264Reader(payloadFormat);
      case MimeTypes.VIDEO_H265:
        return new RtpH265Reader(payloadFormat);
      case MimeTypes.VIDEO_MP4V:
        return new RtpMpeg4Reader(payloadFormat);
      case MimeTypes.VIDEO_VP8:
        return new RtpVp8Reader(payloadFormat);
      case MimeTypes.VIDEO_VP9:
        return new RtpVp9Reader(payloadFormat);
      default:
        // No supported reader, returning null.
    }
    return null;
  }
}
