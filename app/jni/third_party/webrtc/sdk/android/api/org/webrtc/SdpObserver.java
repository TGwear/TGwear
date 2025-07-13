/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

/** Interface for observing SDP-related events. */
public interface SdpObserver {
  /** Called on success of Create{Offer,Answer}(). */
  @CalledByNative void onCreateSuccess(SessionDescription sdp);

  /** Called on success of Set{Local,Remote}Description(). */
  @CalledByNative void onSetSuccess();

  /** Called on error of Create{Offer,Answer}(). */
  @CalledByNative void onCreateFailure(String error);

  /** Called on error of Set{Local,Remote}Description(). */
  @CalledByNative void onSetFailure(String error);
}
