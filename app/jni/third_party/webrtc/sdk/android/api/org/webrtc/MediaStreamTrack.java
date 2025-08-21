/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

import androidx.annotation.Nullable;

/** Java wrapper for a C++ MediaStreamTrackInterface. */
public class MediaStreamTrack {
  public static final String AUDIO_TRACK_KIND = "audio";
  public static final String VIDEO_TRACK_KIND = "video";

  /** Tracks MediaStreamTrackInterface.TrackState */
  public enum State {
    LIVE,
    ENDED;

    @CalledByNative("State")
    static State fromNativeIndex(int nativeIndex) {
      return values()[nativeIndex];
    }
  }

  // Must be kept in sync with cricket::MediaType.
  public enum MediaType {
    MEDIA_TYPE_AUDIO(0),
    MEDIA_TYPE_VIDEO(1);

    private final int nativeIndex;

    private MediaType(int nativeIndex) {
      this.nativeIndex = nativeIndex;
    }

    @CalledByNative("MediaType")
    int getNative() {
      return nativeIndex;
    }

    @CalledByNative("MediaType")
    static MediaType fromNativeIndex(int nativeIndex) {
      for (MediaType type : MediaType.values()) {
        if (type.getNative() == nativeIndex) {
          return type;
        }
      }
      throw new IllegalArgumentException("Unknown native media type: " + nativeIndex);
    }
  }

  /** Factory method to create an AudioTrack or VideoTrack subclass. */
  static @Nullable MediaStreamTrack createMediaStreamTrack(long nativeTrack) {
    if (nativeTrack == 0) {
      return null;
    }
    String trackKind = nativeGetKind(nativeTrack);
    if (trackKind.equals(AUDIO_TRACK_KIND)) {
      return new AudioTrack(nativeTrack);
    } else if (trackKind.equals(VIDEO_TRACK_KIND)) {
      return new VideoTrack(nativeTrack);
    } else {
      return null;
    }
  }

  private long nativeTrack;

  public MediaStreamTrack(long nativeTrack) {
    if (nativeTrack == 0) {
      throw new IllegalArgumentException("nativeTrack may not be null");
    }
    this.nativeTrack = nativeTrack;
  }

  public String id() {
    checkMediaStreamTrackExists();
    return nativeGetId(nativeTrack);
  }

  public String kind() {
    checkMediaStreamTrackExists();
    return nativeGetKind(nativeTrack);
  }

  public boolean enabled() {
    checkMediaStreamTrackExists();
    return nativeGetEnabled(nativeTrack);
  }

  public boolean setEnabled(boolean enable) {
    checkMediaStreamTrackExists();
    return nativeSetEnabled(nativeTrack, enable);
  }

  public State state() {
    checkMediaStreamTrackExists();
    return nativeGetState(nativeTrack);
  }

  public void dispose() {
    checkMediaStreamTrackExists();
    JniCommon.nativeReleaseRef(nativeTrack);
    nativeTrack = 0;
  }

  long getNativeMediaStreamTrack() {
    checkMediaStreamTrackExists();
    return nativeTrack;
  }

  private void checkMediaStreamTrackExists() {
    if (nativeTrack == 0) {
      throw new IllegalStateException("MediaStreamTrack has been disposed.");
    }
  }

  private static native String nativeGetId(long track);
  private static native String nativeGetKind(long track);
  private static native boolean nativeGetEnabled(long track);
  private static native boolean nativeSetEnabled(long track, boolean enabled);
  private static native State nativeGetState(long track);
}
