/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.video.spherical;

import androidx.media3.common.util.UnstableApi;

/** Listens camera motion. */
@UnstableApi
public interface CameraMotionListener {

  /**
   * Called when a new camera motion is read. This method is called on the playback thread.
   *
   * @param timeUs The presentation time of the data.
   * @param rotation Angle axis orientation in radians representing the rotation from camera
   *     coordinate system to world coordinate system.
   */
  void onCameraMotion(long timeUs, float[] rotation);

  /** Called when the camera motion track position is reset or the track is disabled. */
  void onCameraMotionReset();
}
