/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc.native_test;

import android.app.Activity;
import org.chromium.native_test.NativeUnitTest;
import org.webrtc.ContextUtils;

/**
 * Native unit test that calls ContextUtils.initialize for WebRTC.
 */
public class RTCNativeUnitTest extends NativeUnitTest {
  @Override
  public void preCreate(Activity activity) {
    super.preCreate(activity);
    ContextUtils.initialize(activity.getApplicationContext());
  }
}
