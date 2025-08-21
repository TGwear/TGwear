/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package androidx.media3.test.exoplayer.playback.gts;

import static androidx.media3.common.C.WIDEVINE_UUID;

import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaDrm;

/** Utility methods for GTS tests. */
public final class GtsTestUtil {

  private GtsTestUtil() {}

  /** Returns true if the device doesn't support Widevine and this is permitted. */
  public static boolean shouldSkipWidevineTest(Context context) {
    if (isGmsInstalled(context)) {
      // GMS devices are required to support Widevine.
      return false;
    }
    // For non-GMS devices Widevine is optional.
    return !MediaDrm.isCryptoSchemeSupported(WIDEVINE_UUID);
  }

  private static boolean isGmsInstalled(Context context) {
    try {
      context
          .getPackageManager()
          .getPackageInfo("com.google.android.gms", PackageManager.GET_SIGNATURES);
    } catch (PackageManager.NameNotFoundException e) {
      return false;
    }
    return true;
  }
}
