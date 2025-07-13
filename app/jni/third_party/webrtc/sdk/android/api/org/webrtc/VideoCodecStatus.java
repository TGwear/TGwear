/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

/**
 * Status codes reported by video encoding/decoding components. This should be kept in sync with
 * video_error_codes.h.
 */
public enum VideoCodecStatus {
  TARGET_BITRATE_OVERSHOOT(5),
  REQUEST_SLI(2),
  NO_OUTPUT(1),
  OK(0),
  ERROR(-1),
  LEVEL_EXCEEDED(-2),
  MEMORY(-3),
  ERR_PARAMETER(-4),
  ERR_SIZE(-5),
  TIMEOUT(-6),
  UNINITIALIZED(-7),
  ERR_REQUEST_SLI(-12),
  FALLBACK_SOFTWARE(-13);

  private final int number;

  private VideoCodecStatus(int number) {
    this.number = number;
  }

  @CalledByNative
  public int getNumber() {
    return number;
  }
}
