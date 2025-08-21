/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

/** Java wrapper for a C++ MediaSourceInterface. */
public class MediaSource {
  /** Tracks MediaSourceInterface.SourceState */
  public enum State {
    INITIALIZING,
    LIVE,
    ENDED,
    MUTED;

    @CalledByNative("State")
    static State fromNativeIndex(int nativeIndex) {
      return values()[nativeIndex];
    }
  }

  private final RefCountDelegate refCountDelegate;
  private long nativeSource;

  public MediaSource(long nativeSource) {
    refCountDelegate = new RefCountDelegate(() -> JniCommon.nativeReleaseRef(nativeSource));
    this.nativeSource = nativeSource;
  }

  public State state() {
    checkMediaSourceExists();
    return nativeGetState(nativeSource);
  }

  public void dispose() {
    checkMediaSourceExists();
    refCountDelegate.release();
    nativeSource = 0;
  }

  /** Returns a pointer to webrtc::MediaSourceInterface. */
  protected long getNativeMediaSource() {
    checkMediaSourceExists();
    return nativeSource;
  }

  /**
   * Runs code in {@code runnable} holding a reference to the media source. If the object has
   * already been released, does nothing.
   */
  void runWithReference(Runnable runnable) {
    if (refCountDelegate.safeRetain()) {
      try {
        runnable.run();
      } finally {
        refCountDelegate.release();
      }
    }
  }

  private void checkMediaSourceExists() {
    if (nativeSource == 0) {
      throw new IllegalStateException("MediaSource has been disposed.");
    }
  }

  private static native State nativeGetState(long pointer);
}
