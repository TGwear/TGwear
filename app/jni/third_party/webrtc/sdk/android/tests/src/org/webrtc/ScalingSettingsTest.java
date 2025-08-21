/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

import static org.junit.Assert.assertEquals;

import androidx.test.runner.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;
import org.webrtc.VideoEncoder.ScalingSettings;

@RunWith(AndroidJUnit4.class)
@Config(manifest = Config.NONE)
public class ScalingSettingsTest {
  @Test
  public void testToString() {
    assertEquals("[ 1, 2 ]", new ScalingSettings(1, 2).toString());
    assertEquals("OFF", ScalingSettings.OFF.toString());
  }
}
