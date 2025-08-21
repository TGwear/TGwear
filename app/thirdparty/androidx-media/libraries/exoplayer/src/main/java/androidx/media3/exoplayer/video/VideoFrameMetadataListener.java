/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.video;

import android.media.MediaFormat;
import androidx.annotation.Nullable;
import androidx.media3.common.Format;
import androidx.media3.common.util.UnstableApi;

/** A listener for metadata corresponding to video frames being rendered. */
@UnstableApi
public interface VideoFrameMetadataListener {
  /**
   * Called on the playback thread when a video frame is about to be rendered.
   *
   * @param presentationTimeUs The presentation time of the frame, in microseconds.
   * @param releaseTimeNs The system time at which the frame should be displayed, in nanoseconds.
   *     Can be compared to {@link System#nanoTime()}.
   * @param format The format associated with the frame.
   * @param mediaFormat The framework media format associated with the frame, or {@code null} if not
   *     known or not applicable (e.g., because the frame was not output by a {@link
   *     android.media.MediaCodec MediaCodec}).
   */
  void onVideoFrameAboutToBeRendered(
      long presentationTimeUs,
      long releaseTimeNs,
      Format format,
      @Nullable MediaFormat mediaFormat);
}
