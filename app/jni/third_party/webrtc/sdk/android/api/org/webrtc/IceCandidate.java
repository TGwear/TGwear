/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

import androidx.annotation.Nullable;
import java.util.Arrays;
import org.webrtc.PeerConnection;

/**
 * Representation of a single ICE Candidate, mirroring
 * {@code IceCandidateInterface} in the C++ API.
 */
public class IceCandidate {
  public final String sdpMid;
  public final int sdpMLineIndex;
  public final String sdp;
  public final String serverUrl;
  public final PeerConnection.AdapterType adapterType;

  public IceCandidate(String sdpMid, int sdpMLineIndex, String sdp) {
    this.sdpMid = sdpMid;
    this.sdpMLineIndex = sdpMLineIndex;
    this.sdp = sdp;
    this.serverUrl = "";
    this.adapterType = PeerConnection.AdapterType.UNKNOWN;
  }

  @CalledByNative
  IceCandidate(String sdpMid, int sdpMLineIndex, String sdp, String serverUrl,
      PeerConnection.AdapterType adapterType) {
    this.sdpMid = sdpMid;
    this.sdpMLineIndex = sdpMLineIndex;
    this.sdp = sdp;
    this.serverUrl = serverUrl;
    this.adapterType = adapterType;
  }

  @Override
  public String toString() {
    return sdpMid + ":" + sdpMLineIndex + ":" + sdp + ":" + serverUrl + ":"
        + adapterType.toString();
  }

  @CalledByNative
  String getSdpMid() {
    return sdpMid;
  }

  @CalledByNative
  String getSdp() {
    return sdp;
  }

  /** equals() checks sdpMid, sdpMLineIndex, and sdp for equality. */
  @Override
  public boolean equals(@Nullable Object object) {
    if (!(object instanceof IceCandidate)) {
      return false;
    }

    IceCandidate that = (IceCandidate) object;
    return objectEquals(this.sdpMid, that.sdpMid) && this.sdpMLineIndex == that.sdpMLineIndex
        && objectEquals(this.sdp, that.sdp);
  }

  @Override
  public int hashCode() {
    Object[] values = {sdpMid, sdpMLineIndex, sdp};
    return Arrays.hashCode(values);
  }

  private static boolean objectEquals(Object o1, Object o2) {
    if (o1 == null) {
      return o2 == null;
    }
    return o1.equals(o2);
  }
}
