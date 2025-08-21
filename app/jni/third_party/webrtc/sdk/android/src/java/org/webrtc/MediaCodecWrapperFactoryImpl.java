/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaCodecInfo;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.os.Bundle;
import android.view.Surface;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Implementation of MediaCodecWrapperFactory that returns MediaCodecInterfaces wrapping
 * {@link android.media.MediaCodec} objects.
 */
class MediaCodecWrapperFactoryImpl implements MediaCodecWrapperFactory {
  private static class MediaCodecWrapperImpl implements MediaCodecWrapper {
    private final MediaCodec mediaCodec;

    public MediaCodecWrapperImpl(MediaCodec mediaCodec) {
      this.mediaCodec = mediaCodec;
    }

    @Override
    public void configure(MediaFormat format, Surface surface, MediaCrypto crypto, int flags) {
      mediaCodec.configure(format, surface, crypto, flags);
    }

    @Override
    public void start() {
      mediaCodec.start();
    }

    @Override
    public void flush() {
      mediaCodec.flush();
    }

    @Override
    public void stop() {
      mediaCodec.stop();
    }

    @Override
    public void release() {
      mediaCodec.release();
    }

    @Override
    public int dequeueInputBuffer(long timeoutUs) {
      return mediaCodec.dequeueInputBuffer(timeoutUs);
    }

    @Override
    public void queueInputBuffer(
        int index, int offset, int size, long presentationTimeUs, int flags) {
      mediaCodec.queueInputBuffer(index, offset, size, presentationTimeUs, flags);
    }

    @Override
    public int dequeueOutputBuffer(BufferInfo info, long timeoutUs) {
      return mediaCodec.dequeueOutputBuffer(info, timeoutUs);
    }

    @Override
    public void releaseOutputBuffer(int index, boolean render) {
      mediaCodec.releaseOutputBuffer(index, render);
    }

    @Override
    public MediaFormat getInputFormat() {
      return mediaCodec.getInputFormat();
    }

    @Override
    public MediaFormat getOutputFormat() {
      return mediaCodec.getOutputFormat();
    }

    @Override
    public MediaFormat getOutputFormat(int index) {
      return mediaCodec.getOutputFormat(index);
    }

    @Override
    public ByteBuffer getInputBuffer(int index) {
      return mediaCodec.getInputBuffer(index);
    }

    @Override
    public ByteBuffer getOutputBuffer(int index) {
      return mediaCodec.getOutputBuffer(index);
    }

    @Override
    public Surface createInputSurface() {
      return mediaCodec.createInputSurface();
    }

    @Override
    public void setParameters(Bundle params) {
      mediaCodec.setParameters(params);
    }

    @Override
    public MediaCodecInfo getCodecInfo() {
      return mediaCodec.getCodecInfo();
    }
  }

  @Override
  public MediaCodecWrapper createByCodecName(String name) throws IOException {
    return new MediaCodecWrapperImpl(MediaCodec.createByCodecName(name));
  }
}
