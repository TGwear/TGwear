/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

import android.content.Context;
import androidx.annotation.Nullable;

public class UnityUtility {
  private static final String VIDEO_CAPTURER_THREAD_NAME = "VideoCapturerThread";

  public static SurfaceTextureHelper LoadSurfaceTextureHelper() {
    final SurfaceTextureHelper surfaceTextureHelper =
        SurfaceTextureHelper.create(VIDEO_CAPTURER_THREAD_NAME, null);
    return surfaceTextureHelper;
  }

  private static boolean useCamera2() {
    return Camera2Enumerator.isSupported(ContextUtils.getApplicationContext());
  }

  private static @Nullable VideoCapturer createCameraCapturer(CameraEnumerator enumerator) {
    final String[] deviceNames = enumerator.getDeviceNames();

    for (String deviceName : deviceNames) {
      if (enumerator.isFrontFacing(deviceName)) {
        VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);

        if (videoCapturer != null) {
          return videoCapturer;
        }
      }
    }

    return null;
  }

  public static VideoCapturer LinkCamera(
      long nativeTrackSource, SurfaceTextureHelper surfaceTextureHelper) {
    VideoCapturer capturer =
        createCameraCapturer(new Camera2Enumerator(ContextUtils.getApplicationContext()));

    VideoSource videoSource = new VideoSource(nativeTrackSource);

    capturer.initialize(surfaceTextureHelper, ContextUtils.getApplicationContext(),
        videoSource.getCapturerObserver());

    capturer.startCapture(720, 480, 30);
    return capturer;
  }

  public static void StopCamera(VideoCapturer camera) throws InterruptedException {
    camera.stopCapture();
    camera.dispose();
  }

  public static void InitializePeerConncectionFactory(Context context) throws InterruptedException {
    PeerConnectionFactory.initialize(
        PeerConnectionFactory.InitializationOptions.builder(context).createInitializationOptions());
  }
}
