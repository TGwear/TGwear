/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.source.mediaparser;

import android.media.MediaFormat;
import android.media.MediaParser;
import android.media.metrics.LogSessionId;
import androidx.annotation.RequiresApi;
import androidx.media3.common.Format;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.analytics.PlayerId;

/**
 * Miscellaneous constants and utility methods related to the {@link MediaParser} integration.
 *
 * <p>For documentation on constants, please see the {@link MediaParser} documentation.
 */
@UnstableApi
public final class MediaParserUtil {

  public static final String PARAMETER_IN_BAND_CRYPTO_INFO =
      "android.media.mediaparser.inBandCryptoInfo";
  public static final String PARAMETER_INCLUDE_SUPPLEMENTAL_DATA =
      "android.media.mediaparser.includeSupplementalData";
  public static final String PARAMETER_EAGERLY_EXPOSE_TRACK_TYPE =
      "android.media.mediaparser.eagerlyExposeTrackType";
  public static final String PARAMETER_EXPOSE_DUMMY_SEEK_MAP =
      "android.media.mediaparser.exposeDummySeekMap";
  public static final String PARAMETER_EXPOSE_CHUNK_INDEX_AS_MEDIA_FORMAT =
      "android.media.mediaParser.exposeChunkIndexAsMediaFormat";
  public static final String PARAMETER_OVERRIDE_IN_BAND_CAPTION_DECLARATIONS =
      "android.media.mediaParser.overrideInBandCaptionDeclarations";
  public static final String PARAMETER_EXPOSE_CAPTION_FORMATS =
      "android.media.mediaParser.exposeCaptionFormats";
  public static final String PARAMETER_IGNORE_TIMESTAMP_OFFSET =
      "android.media.mediaparser.ignoreTimestampOffset";

  private MediaParserUtil() {}

  /**
   * Returns a {@link MediaFormat} with equivalent {@link MediaFormat#KEY_MIME} and {@link
   * MediaFormat#KEY_CAPTION_SERVICE_NUMBER} to the given {@link Format}.
   */
  public static MediaFormat toCaptionsMediaFormat(Format format) {
    MediaFormat mediaFormat = new MediaFormat();
    mediaFormat.setString(MediaFormat.KEY_MIME, format.sampleMimeType);
    if (format.accessibilityChannel != Format.NO_VALUE) {
      mediaFormat.setInteger(MediaFormat.KEY_CAPTION_SERVICE_NUMBER, format.accessibilityChannel);
    }
    return mediaFormat;
  }

  /**
   * Calls {@link MediaParser#setLogSessionId(LogSessionId)}.
   *
   * @param mediaParser The {@link MediaParser} to call the method on.
   * @param playerId The {@link PlayerId} to obtain the {@link LogSessionId} from.
   */
  @RequiresApi(31)
  public static void setLogSessionIdOnMediaParser(MediaParser mediaParser, PlayerId playerId) {
    Api31.setLogSessionIdOnMediaParser(mediaParser, playerId);
  }

  @RequiresApi(31)
  private static final class Api31 {
    private Api31() {}

    public static void setLogSessionIdOnMediaParser(MediaParser mediaParser, PlayerId playerId) {
      LogSessionId logSessionId = playerId.getLogSessionId();
      if (!logSessionId.equals(LogSessionId.LOG_SESSION_ID_NONE)) {
        mediaParser.setLogSessionId(logSessionId);
      }
    }
  }
}
