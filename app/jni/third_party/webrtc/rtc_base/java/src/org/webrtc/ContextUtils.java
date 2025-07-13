/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.webrtc;

import android.content.Context;

/**
 * Class for storing the application context and retrieving it in a static context. Similar to
 * org.chromium.base.ContextUtils.
 */
public class ContextUtils {
  private static final String TAG = "ContextUtils";
  private static Context applicationContext;

  /**
   * Stores the application context that will be returned by getApplicationContext. This is called
   * by PeerConnectionFactory.initialize. The application context must be set before creating
   * a PeerConnectionFactory and must not be modified while it is alive.
   */
  public static void initialize(Context applicationContext) {
    if (applicationContext == null) {
      throw new IllegalArgumentException(
          "Application context cannot be null for ContextUtils.initialize.");
    }
    ContextUtils.applicationContext = applicationContext;
  }

  /**
   * Returns the stored application context.
   *
   * @deprecated crbug.com/webrtc/8937
   */
  @Deprecated
  public static Context getApplicationContext() {
    return applicationContext;
  }
}
