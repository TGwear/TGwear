/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

import android.media.MediaCodecInfo;
import androidx.annotation.Nullable;
import java.util.Arrays;

/** Factory for Android platform software VideoDecoders. */
public class PlatformSoftwareVideoDecoderFactory extends MediaCodecVideoDecoderFactory {
  /**
   * Default allowed predicate.
   */
  private static final Predicate<MediaCodecInfo> defaultAllowedPredicate =
      new Predicate<MediaCodecInfo>() {
        @Override
        public boolean test(MediaCodecInfo arg) {
          return MediaCodecUtils.isSoftwareOnly(arg);
        }
      };

  /**
   * Creates a PlatformSoftwareVideoDecoderFactory that supports surface texture rendering.
   *
   * @param sharedContext The textures generated will be accessible from this context. May be null,
   *                      this disables texture support.
   */
  public PlatformSoftwareVideoDecoderFactory(@Nullable EglBase.Context sharedContext) {
    super(sharedContext, defaultAllowedPredicate);
  }
}
