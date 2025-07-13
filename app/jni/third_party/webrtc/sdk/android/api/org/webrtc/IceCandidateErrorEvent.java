/*
 * Copyright (c) 2021-2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

public final class IceCandidateErrorEvent {
  /** The local IP address used to communicate with the STUN or TURN server. */
  public final String address;
  /** The port used to communicate with the STUN or TURN server. */
  public final int port;
  /**
   * The STUN or TURN URL that identifies the STUN or TURN server for which the failure occurred.
   */
  public final String url;
  /**
   * The numeric STUN error code returned by the STUN or TURN server. If no host candidate can reach
   * the server, errorCode will be set to the value 701 which is outside the STUN error code range.
   * This error is only fired once per server URL while in the RTCIceGatheringState of "gathering".
   */
  public final int errorCode;
  /**
   * The STUN reason text returned by the STUN or TURN server. If the server could not be reached,
   * errorText will be set to an implementation-specific value providing details about the error.
   */
  public final String errorText;

  @CalledByNative
  public IceCandidateErrorEvent(
      String address, int port, String url, int errorCode, String errorText) {
    this.address = address;
    this.port = port;
    this.url = url;
    this.errorCode = errorCode;
    this.errorText = errorText;
  }
}
