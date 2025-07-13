/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc.native_test;

import android.app.Activity;
import android.os.Bundle;

/**
 * Activity that uses RTCNativeUnitTest to run the tests.
 */
public class RTCNativeUnitTestActivity extends Activity {
  private RTCNativeUnitTest mTest = new RTCNativeUnitTest();

  @Override
  public void onCreate(Bundle savedInstanceState) {
    mTest.preCreate(this);
    super.onCreate(savedInstanceState);
    mTest.postCreate(this);
  }

  @Override
  public void onStart() {
    super.onStart();
    mTest.postStart(this, false);
  }
}
