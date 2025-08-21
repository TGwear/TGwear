/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.os.Bundle;
import android.view.Surface;
import java.nio.ByteBuffer;

/**
 * Subset of methods defined in {@link android.media.MediaCodec} needed by
 * {@link HardwareVideoEncoder} and {@link AndroidVideoDecoder}. This interface
 * exists to allow mocking and using a fake implementation in tests.
 */
interface MediaCodecWrapper {
  void configure(MediaFormat format, Surface surface, MediaCrypto crypto, int flags);

  void start();

  void flush();

  void stop();

  void release();

  int dequeueInputBuffer(long timeoutUs);

  void queueInputBuffer(int index, int offset, int size, long presentationTimeUs, int flags);

  int dequeueOutputBuffer(MediaCodec.BufferInfo info, long timeoutUs);

  void releaseOutputBuffer(int index, boolean render);

  MediaFormat getInputFormat();

  MediaFormat getOutputFormat();

  MediaFormat getOutputFormat(int index);

  ByteBuffer getInputBuffer(int index);

  ByteBuffer getOutputBuffer(int index);

  Surface createInputSurface();

  void setParameters(Bundle params);

  MediaCodecInfo getCodecInfo();
}
