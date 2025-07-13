/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

import static org.junit.Assert.assertEquals;
import static org.webrtc.CameraEnumerationAndroid.getClosestSupportedFramerateRange;

import androidx.test.runner.AndroidJUnit4;
import java.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;
import org.webrtc.CameraEnumerationAndroid.CaptureFormat.FramerateRange;

/**
 * Tests for CameraEnumerationAndroid.
 */
@RunWith(AndroidJUnit4.class)
@Config(manifest = Config.NONE)
public class CameraEnumerationTest {
  @Test
  public void testGetClosestSupportedFramerateRange() {
    assertEquals(new FramerateRange(10000, 30000),
        getClosestSupportedFramerateRange(
            Arrays.asList(new FramerateRange(10000, 30000), new FramerateRange(30000, 30000)),
            30 /* requestedFps */));

    assertEquals(new FramerateRange(10000, 20000),
        getClosestSupportedFramerateRange(
            Arrays.asList(new FramerateRange(0, 30000), new FramerateRange(10000, 20000),
                new FramerateRange(14000, 16000), new FramerateRange(15000, 15000)),
            15 /* requestedFps */));

    assertEquals(new FramerateRange(10000, 20000),
        getClosestSupportedFramerateRange(
            Arrays.asList(new FramerateRange(15000, 15000), new FramerateRange(10000, 20000),
                new FramerateRange(10000, 30000)),
            10 /* requestedFps */));
  }
}
