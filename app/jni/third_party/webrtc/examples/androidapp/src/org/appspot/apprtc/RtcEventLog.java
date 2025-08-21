/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.appspot.apprtc;

import android.os.ParcelFileDescriptor;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import org.webrtc.PeerConnection;

public class RtcEventLog {
  private static final String TAG = "RtcEventLog";
  private static final int OUTPUT_FILE_MAX_BYTES = 10_000_000;
  private final PeerConnection peerConnection;
  private RtcEventLogState state = RtcEventLogState.INACTIVE;

  enum RtcEventLogState {
    INACTIVE,
    STARTED,
    STOPPED,
  }

  public RtcEventLog(PeerConnection peerConnection) {
    if (peerConnection == null) {
      throw new NullPointerException("The peer connection is null.");
    }
    this.peerConnection = peerConnection;
  }

  public void start(final File outputFile) {
    if (state == RtcEventLogState.STARTED) {
      Log.e(TAG, "RtcEventLog has already started.");
      return;
    }
    final ParcelFileDescriptor fileDescriptor;
    try {
      fileDescriptor = ParcelFileDescriptor.open(outputFile,
          ParcelFileDescriptor.MODE_READ_WRITE | ParcelFileDescriptor.MODE_CREATE
              | ParcelFileDescriptor.MODE_TRUNCATE);
    } catch (IOException e) {
      Log.e(TAG, "Failed to create a new file", e);
      return;
    }

    // Passes ownership of the file to WebRTC.
    boolean success =
        peerConnection.startRtcEventLog(fileDescriptor.detachFd(), OUTPUT_FILE_MAX_BYTES);
    if (!success) {
      Log.e(TAG, "Failed to start RTC event log.");
      return;
    }
    state = RtcEventLogState.STARTED;
    Log.d(TAG, "RtcEventLog started.");
  }

  public void stop() {
    if (state != RtcEventLogState.STARTED) {
      Log.e(TAG, "RtcEventLog was not started.");
      return;
    }
    peerConnection.stopRtcEventLog();
    state = RtcEventLogState.STOPPED;
    Log.d(TAG, "RtcEventLog stopped.");
  }
}
