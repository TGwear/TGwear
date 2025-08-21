/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

import java.io.IOException;

interface MediaCodecWrapperFactory {
  /**
   * Creates a new {@link MediaCodecWrapper} by codec name.
   *
   * <p>For additional information see {@link android.media.MediaCodec#createByCodecName}.
   */
  MediaCodecWrapper createByCodecName(String name) throws IOException;
}
