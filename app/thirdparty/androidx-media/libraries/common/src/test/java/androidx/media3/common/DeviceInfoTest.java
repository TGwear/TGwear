/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common;

import static com.google.common.truth.Truth.assertThat;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit tests for {@link DeviceInfo}. */
@RunWith(AndroidJUnit4.class)
public class DeviceInfoTest {

  @Test
  public void roundTripViaBundle_yieldsEqualInstance() {
    DeviceInfo deviceInfo =
        new DeviceInfo.Builder(DeviceInfo.PLAYBACK_TYPE_REMOTE)
            .setMinVolume(1)
            .setMaxVolume(9)
            .setRoutingControllerId("route")
            .build();

    assertThat(DeviceInfo.fromBundle(deviceInfo.toBundle())).isEqualTo(deviceInfo);
  }
}
