/*
 * Copyright (c) 2018-2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc.examples.androidnativeapi;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import org.webrtc.CapturerObserver;
import org.webrtc.SurfaceTextureHelper;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoSink;

public class CallClient {
  private static final String TAG = "CallClient";
  private static final int CAPTURE_WIDTH = 640;
  private static final int CAPTURE_HEIGHT = 480;
  private static final int CAPTURE_FPS = 30;

  private final Context applicationContext;
  private final HandlerThread thread;
  private final Handler handler;

  private long nativeClient;
  private SurfaceTextureHelper surfaceTextureHelper;
  private VideoCapturer videoCapturer;

  public CallClient(Context applicationContext) {
    this.applicationContext = applicationContext;
    thread = new HandlerThread(TAG + "Thread");
    thread.start();
    handler = new Handler(thread.getLooper());
    handler.post(() -> { nativeClient = nativeCreateClient(); });
  }

  public void call(VideoSink localSink, VideoSink remoteSink, VideoCapturer videoCapturer,
      SurfaceTextureHelper videoCapturerSurfaceTextureHelper) {
    handler.post(() -> {
      nativeCall(nativeClient, localSink, remoteSink);
      videoCapturer.initialize(videoCapturerSurfaceTextureHelper, applicationContext,
          nativeGetJavaVideoCapturerObserver(nativeClient));
      videoCapturer.startCapture(CAPTURE_WIDTH, CAPTURE_HEIGHT, CAPTURE_FPS);
    });
  }

  public void hangup() {
    handler.post(() -> { nativeHangup(nativeClient); });
  }

  public void close() {
    handler.post(() -> {
      nativeDelete(nativeClient);
      nativeClient = 0;
    });
    thread.quitSafely();
  }

  private static native long nativeCreateClient();
  private static native void nativeCall(
      long nativeAndroidCallClient, VideoSink localSink, VideoSink remoteSink);
  private static native void nativeHangup(long nativeAndroidCallClient);
  private static native void nativeDelete(long nativeAndroidCallClient);
  private static native CapturerObserver nativeGetJavaVideoCapturerObserver(
      long nativeAndroidCallClient);
}
