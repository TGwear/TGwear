/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

import androidx.test.InstrumentationRegistry;
import androidx.test.filters.SmallTest;
import org.junit.Test;
import org.webrtc.PeerConnectionFactory;

// This test is intended to run on ARM and catch LoadLibrary errors when we load the WebRTC
// JNI. It can't really be setting up calls since ARM emulators are too slow, but instantiating
// a peer connection isn't timing-sensitive, so we can at least do that.
public class WebRtcJniBootTest {
  @Test
  @SmallTest
  public void testJniLoadsWithoutError() throws InterruptedException {
    PeerConnectionFactory.initialize(PeerConnectionFactory.InitializationOptions
                                         .builder(InstrumentationRegistry.getTargetContext())
                                         .setNativeLibraryName(TestConstants.NATIVE_LIBRARY)
                                         .createInitializationOptions());
    PeerConnectionFactory.builder().createPeerConnectionFactory();
  }
}
