/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

/** Interface to handle completion of addIceCandidate  */
public interface AddIceObserver {
  /** Called when ICE candidate added successfully.*/
  @CalledByNative public void onAddSuccess();

  /** Called when ICE candidate addition failed.*/
  @CalledByNative public void onAddFailure(String error);
}
