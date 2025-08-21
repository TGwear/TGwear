/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

import java.util.IdentityHashMap;

/** Java version of VideoTrackInterface. */
public class VideoTrack extends MediaStreamTrack {
  private final IdentityHashMap<VideoSink, Long> sinks = new IdentityHashMap<VideoSink, Long>();

  public VideoTrack(long nativeTrack) {
    super(nativeTrack);
  }

  /**
   * Adds a VideoSink to the track.
   *
   * A track can have any number of VideoSinks. VideoSinks will replace
   * renderers. However, converting old style texture frames will involve costly
   * conversion to I420 so it is not recommended to upgrade before all your
   * sources produce VideoFrames.
   */
  public void addSink(VideoSink sink) {
    if (sink == null) {
      throw new IllegalArgumentException("The VideoSink is not allowed to be null");
    }
    // We allow calling addSink() with the same sink multiple times. This is similar to the C++
    // VideoTrack::AddOrUpdateSink().
    if (!sinks.containsKey(sink)) {
      final long nativeSink = nativeWrapSink(sink);
      sinks.put(sink, nativeSink);
      nativeAddSink(getNativeMediaStreamTrack(), nativeSink);
    }
  }

  /**
   * Removes a VideoSink from the track.
   *
   * If the VideoSink was not attached to the track, this is a no-op.
   */
  public void removeSink(VideoSink sink) {
    final Long nativeSink = sinks.remove(sink);
    if (nativeSink != null) {
      nativeRemoveSink(getNativeMediaStreamTrack(), nativeSink);
      nativeFreeSink(nativeSink);
    }
  }

  @Override
  public void dispose() {
    for (long nativeSink : sinks.values()) {
      nativeRemoveSink(getNativeMediaStreamTrack(), nativeSink);
      nativeFreeSink(nativeSink);
    }
    sinks.clear();
    super.dispose();
  }

  /** Returns a pointer to webrtc::VideoTrackInterface. */
  public long getNativeVideoTrack() {
    return getNativeMediaStreamTrack();
  }

  private static native void nativeAddSink(long track, long nativeSink);
  private static native void nativeRemoveSink(long track, long nativeSink);
  private static native long nativeWrapSink(VideoSink sink);
  private static native void nativeFreeSink(long sink);
}
