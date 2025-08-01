/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package androidx.media3.transformer;

import android.media.MediaCodecInfo;

/** Provides bitrates for encoders to use as a target. */
/* package */ interface EncoderBitrateProvider {

  /**
   * Returns a recommended bitrate that the encoder should target.
   *
   * @param encoderName The name of the encoder, see {@link MediaCodecInfo#getName()}.
   * @param width The output width of the video after encoding.
   * @param height The output height of the video after encoding.
   * @param frameRate The expected output frame rate of the video after encoding.
   * @return The bitrate the encoder should target.
   */
  int getBitrate(String encoderName, int width, int height, float frameRate);
}
