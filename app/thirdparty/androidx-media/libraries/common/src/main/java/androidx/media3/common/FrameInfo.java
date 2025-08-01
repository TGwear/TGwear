/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common;

import static androidx.media3.common.util.Assertions.checkArgument;

import androidx.media3.common.util.UnstableApi;

/** Value class specifying information about a decoded video frame. */
@UnstableApi
public class FrameInfo {

  /**
   * The {@link Format} of the frame.
   *
   * <p>The {@link Format#colorInfo} must be set, and the {@link Format#width} and {@link
   * Format#height} must be greater than 0.
   */
  public final Format format;

  /** The offset that must be added to the frame presentation timestamp, in microseconds. */
  public final long offsetToAddUs;

  /**
   * Creates an instance.
   *
   * @param format See {@link #format}.
   * @param offsetToAddUs See {@link #offsetToAddUs}.
   */
  public FrameInfo(Format format, long offsetToAddUs) {
    checkArgument(format.colorInfo != null, "format colorInfo must be set");
    checkArgument(format.width > 0, "format width must be positive, but is: " + format.width);
    checkArgument(format.height > 0, "format height must be positive, but is: " + format.height);

    this.format = format;
    this.offsetToAddUs = offsetToAddUs;
  }
}
