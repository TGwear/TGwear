/*
 * Copyright (c) 2013-2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

import org.webrtc.VideoFrame;

/**
 * Used from native api and implements a simple VideoCapturer.CapturerObserver that feeds frames to
 * a webrtc::jni::AndroidVideoTrackSource.
 */
class NativeCapturerObserver implements CapturerObserver {
  private final NativeAndroidVideoTrackSource nativeAndroidVideoTrackSource;

  @CalledByNative
  public NativeCapturerObserver(long nativeSource) {
    this.nativeAndroidVideoTrackSource = new NativeAndroidVideoTrackSource(nativeSource);
  }

  @Override
  public void onCapturerStarted(boolean success) {
    nativeAndroidVideoTrackSource.setState(success);
  }

  @Override
  public void onCapturerStopped() {
    nativeAndroidVideoTrackSource.setState(/* isLive= */ false);
  }

  @Override
  public void onFrameCaptured(VideoFrame frame) {
    final VideoProcessor.FrameAdaptationParameters parameters =
        nativeAndroidVideoTrackSource.adaptFrame(frame);
    if (parameters == null) {
      // Drop frame.
      return;
    }

    final VideoFrame.Buffer adaptedBuffer =
        frame.getBuffer().cropAndScale(parameters.cropX, parameters.cropY, parameters.cropWidth,
            parameters.cropHeight, parameters.scaleWidth, parameters.scaleHeight);
    nativeAndroidVideoTrackSource.onFrameCaptured(
        new VideoFrame(adaptedBuffer, frame.getRotation(), parameters.timestampNs));
    adaptedBuffer.release();
  }
}
