/*
 * Copyright (c) 2018-2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

import android.content.Context;
import org.webrtc.PeerConnectionFactory.InitializationOptions;

public class PeerConnectionFactoryInitializationHelper {
  private static class MockLoader implements NativeLibraryLoader {
    @Override
    public boolean load(String name) {
      return true;
    }
  }

  @CalledByNative
  public static void initializeFactoryForTests() {
    Context ctx = ContextUtils.getApplicationContext();
    InitializationOptions options = InitializationOptions.builder(ctx)
                                        .setNativeLibraryLoader(new MockLoader())
                                        .createInitializationOptions();

    PeerConnectionFactory.initialize(options);
  }
}
