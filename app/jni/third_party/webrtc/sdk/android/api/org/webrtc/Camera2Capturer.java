/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

import android.content.Context;
import android.hardware.camera2.CameraManager;
import androidx.annotation.Nullable;

public class Camera2Capturer extends CameraCapturer {
  private final Context context;
  @Nullable private final CameraManager cameraManager;

  public Camera2Capturer(Context context, String cameraName, CameraEventsHandler eventsHandler) {
    super(cameraName, eventsHandler, new Camera2Enumerator(context));

    this.context = context;
    cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
  }

  @Override
  protected void createCameraSession(CameraSession.CreateSessionCallback createSessionCallback,
      CameraSession.Events events, Context applicationContext,
      SurfaceTextureHelper surfaceTextureHelper, String cameraName, int width, int height,
      int framerate) {
    Camera2Session.create(createSessionCallback, events, applicationContext, cameraManager,
        surfaceTextureHelper, cameraName, width, height, framerate);
  }
}
