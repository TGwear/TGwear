/*
 * Copyright (c) 2020-2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc.examples.androidvoip;

import java.util.List;

public interface OnVoipClientTaskCompleted {
  void onGetLocalIPAddressCompleted(String localIPAddress);
  void onGetSupportedCodecsCompleted(List<String> supportedCodecs);
  void onVoipClientInitializationCompleted(boolean isSuccessful);
  void onStartSessionCompleted(boolean isSuccessful);
  void onStopSessionCompleted(boolean isSuccessful);
  void onStartSendCompleted(boolean isSuccessful);
  void onStopSendCompleted(boolean isSuccessful);
  void onStartPlayoutCompleted(boolean isSuccessful);
  void onStopPlayoutCompleted(boolean isSuccessful);
  void onUninitializedVoipClient();
}
