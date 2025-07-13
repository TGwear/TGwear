/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

import java.nio.ByteBuffer;

/**
 * This class wraps a webrtc::I420BufferInterface into a VideoFrame.I420Buffer.
 */
class WrappedNativeI420Buffer implements VideoFrame.I420Buffer {
  private final int width;
  private final int height;
  private final ByteBuffer dataY;
  private final int strideY;
  private final ByteBuffer dataU;
  private final int strideU;
  private final ByteBuffer dataV;
  private final int strideV;
  private final long nativeBuffer;

  @CalledByNative
  WrappedNativeI420Buffer(int width, int height, ByteBuffer dataY, int strideY, ByteBuffer dataU,
      int strideU, ByteBuffer dataV, int strideV, long nativeBuffer) {
    this.width = width;
    this.height = height;
    this.dataY = dataY;
    this.strideY = strideY;
    this.dataU = dataU;
    this.strideU = strideU;
    this.dataV = dataV;
    this.strideV = strideV;
    this.nativeBuffer = nativeBuffer;

    retain();
  }

  @Override
  public int getWidth() {
    return width;
  }

  @Override
  public int getHeight() {
    return height;
  }

  @Override
  public ByteBuffer getDataY() {
    // Return a slice to prevent relative reads from changing the position.
    return dataY.slice();
  }

  @Override
  public ByteBuffer getDataU() {
    // Return a slice to prevent relative reads from changing the position.
    return dataU.slice();
  }

  @Override
  public ByteBuffer getDataV() {
    // Return a slice to prevent relative reads from changing the position.
    return dataV.slice();
  }

  @Override
  public int getStrideY() {
    return strideY;
  }

  @Override
  public int getStrideU() {
    return strideU;
  }

  @Override
  public int getStrideV() {
    return strideV;
  }

  @Override
  public VideoFrame.I420Buffer toI420() {
    retain();
    return this;
  }

  @Override
  public void retain() {
    JniCommon.nativeAddRef(nativeBuffer);
  }

  @Override
  public void release() {
    JniCommon.nativeReleaseRef(nativeBuffer);
  }

  @Override
  public VideoFrame.Buffer cropAndScale(
      int cropX, int cropY, int cropWidth, int cropHeight, int scaleWidth, int scaleHeight) {
    return JavaI420Buffer.cropAndScaleI420(
        this, cropX, cropY, cropWidth, cropHeight, scaleWidth, scaleHeight);
  }
}
