/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package androidx.media3.transformer;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.UnstableApi;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

/** Selector of {@link MediaCodec} encoder instances. */
@UnstableApi
public interface EncoderSelector {

  /**
   * Default implementation of {@code EncoderSelector}, which returns the preferred encoders for the
   * given {@link MimeTypes MIME type}.
   *
   * <p>The {@code EncoderSelector} selection result contains only hardware encoders if they exist,
   * or only software encoders otherwise.
   */
  EncoderSelector DEFAULT =
      mimeType -> {
        ImmutableList<MediaCodecInfo> supportedEncoders =
            EncoderUtil.getSupportedEncoders(mimeType);
        ImmutableList<MediaCodecInfo> supportedHardwareEncoders =
            ImmutableList.copyOf(
                Iterables.filter(
                    supportedEncoders,
                    encoderInfo -> EncoderUtil.isHardwareAccelerated(encoderInfo, mimeType)));
        return supportedHardwareEncoders.isEmpty() ? supportedEncoders : supportedHardwareEncoders;
      };

  /**
   * Returns a list of encoders that can encode media in the specified {@code mimeType}, in priority
   * order.
   *
   * @param mimeType The {@linkplain MimeTypes MIME type} for which an encoder is required.
   * @return An immutable list of {@linkplain MediaCodecInfo encoders} that support the {@code
   *     mimeType}. The list may be empty.
   */
  ImmutableList<MediaCodecInfo> selectEncoderInfos(String mimeType);
}
