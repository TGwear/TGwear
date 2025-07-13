/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

import android.content.Context;

public class Camera1Capturer extends CameraCapturer {
  private final boolean captureToTexture;

  public Camera1Capturer(
      String cameraName, CameraEventsHandler eventsHandler, boolean captureToTexture) {
    super(cameraName, eventsHandler, new Camera1Enumerator(captureToTexture));

    this.captureToTexture = captureToTexture;
  }

  @Override
  protected void createCameraSession(CameraSession.CreateSessionCallback createSessionCallback,
      CameraSession.Events events, Context applicationContext,
      SurfaceTextureHelper surfaceTextureHelper, String cameraName, int width, int height,
      int framerate) {
    Camera1Session.create(createSessionCallback, events, captureToTexture, applicationContext,
        surfaceTextureHelper, cameraName, width, height, framerate);
  }
}
