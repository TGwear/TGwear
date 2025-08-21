/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

/** Enumeration of supported video codec types. */
enum VideoCodecMimeType {
  VP8("video/x-vnd.on2.vp8"),
  VP9("video/x-vnd.on2.vp9"),
  H264("video/avc"),
  AV1("video/av01"),
  H265("video/hevc");

  private final String mimeType;

  private VideoCodecMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  String mimeType() {
    return mimeType;
  }
}
