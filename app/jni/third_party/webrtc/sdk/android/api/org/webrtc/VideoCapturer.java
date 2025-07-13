/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

import android.content.Context;

// Base interface for all VideoCapturers to implement.
public interface VideoCapturer {
  /**
   * This function is used to initialize the camera thread, the android application context, and the
   * capture observer. It will be called only once and before any startCapture() request. The
   * camera thread is guaranteed to be valid until dispose() is called. If the VideoCapturer wants
   * to deliver texture frames, it should do this by rendering on the SurfaceTexture in
   * {@code surfaceTextureHelper}, register itself as a listener, and forward the frames to
   * CapturerObserver.onFrameCaptured(). The caller still has ownership of {@code
   * surfaceTextureHelper} and is responsible for making sure surfaceTextureHelper.dispose() is
   * called. This also means that the caller can reuse the SurfaceTextureHelper to initialize a new
   * VideoCapturer once the previous VideoCapturer has been disposed.
   */
  void initialize(SurfaceTextureHelper surfaceTextureHelper, Context applicationContext,
      CapturerObserver capturerObserver);

  /**
   * Start capturing frames in a format that is as close as possible to {@code width x height} and
   * {@code framerate}.
   */
  void startCapture(int width, int height, int framerate);

  /**
   * Stop capturing. This function should block until capture is actually stopped.
   */
  void stopCapture() throws InterruptedException;

  void changeCaptureFormat(int width, int height, int framerate);

  /**
   * Perform any final cleanup here. No more capturing will be done after this call.
   */
  void dispose();

  /**
   * @return true if-and-only-if this is a screen capturer.
   */
  boolean isScreencast();
}
