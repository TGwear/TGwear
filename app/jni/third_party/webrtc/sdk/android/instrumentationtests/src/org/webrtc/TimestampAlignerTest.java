/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

import androidx.test.filters.SmallTest;
import org.junit.BeforeClass;
import org.junit.Test;

public class TimestampAlignerTest {
  @BeforeClass
  public static void setUp() {
    System.loadLibrary(TestConstants.NATIVE_LIBRARY);
  }

  @Test
  @SmallTest
  public void testGetRtcTimeNanos() {
    TimestampAligner.getRtcTimeNanos();
  }

  @Test
  @SmallTest
  public void testDispose() {
    final TimestampAligner timestampAligner = new TimestampAligner();
    timestampAligner.dispose();
  }

  @Test
  @SmallTest
  public void testTranslateTimestamp() {
    final TimestampAligner timestampAligner = new TimestampAligner();
    timestampAligner.translateTimestamp(/* cameraTimeNs= */ 123);
    timestampAligner.dispose();
  }
}
