/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.Looper;
import androidx.annotation.Nullable;
import androidx.media3.common.util.Clock;
import androidx.media3.common.util.HandlerWrapper;
import androidx.media3.common.util.Log;

/**
 * Handles a {@link WifiLock}
 *
 * <p>The handling of wifi locks requires the {@link android.Manifest.permission#WAKE_LOCK}
 * permission.
 */
/* package */ final class WifiLockManager {

  private static final String TAG = "WifiLockManager";
  private static final String WIFI_LOCK_TAG = "ExoPlayer:WifiLockManager";

  private final WifiLockManagerInternal wifiLockManagerInternal;
  private final HandlerWrapper wifiLockHandler;

  private boolean enabled;
  private boolean stayAwake;

  /**
   * Creates the wifi lock manager.
   *
   * @param context A {@link Context}
   * @param wifiLockLooper The {@link Looper} to call wifi lock system calls on.
   * @param clock The {@link Clock} to schedule handler messages.
   */
  public WifiLockManager(Context context, Looper wifiLockLooper, Clock clock) {
    wifiLockManagerInternal = new WifiLockManagerInternal(context.getApplicationContext());
    wifiLockHandler = clock.createHandler(wifiLockLooper, /* callback= */ null);
  }

  /**
   * Sets whether to enable the usage of a {@link WifiLock}.
   *
   * <p>By default, wifi lock handling is not enabled. Enabling will acquire the wifi lock if
   * necessary. Disabling will release the wifi lock if held.
   *
   * <p>Enabling {@link WifiLock} requires the {@link android.Manifest.permission#WAKE_LOCK}.
   *
   * @param enabled True if the player should handle a {@link WifiLock}.
   */
  public void setEnabled(boolean enabled) {
    if (this.enabled == enabled) {
      return;
    }
    this.enabled = enabled;
    boolean stayAwakeCurrent = stayAwake;
    wifiLockHandler.post(() -> wifiLockManagerInternal.updateWifiLock(enabled, stayAwakeCurrent));
  }

  /**
   * Sets whether to acquire or release the {@link WifiLock}.
   *
   * <p>The wifi lock will not be acquired unless handling has been enabled through {@link
   * #setEnabled(boolean)}.
   *
   * @param stayAwake True if the player should acquire the {@link WifiLock}. False if it should
   *     release.
   */
  public void setStayAwake(boolean stayAwake) {
    if (this.stayAwake == stayAwake) {
      return;
    }
    this.stayAwake = stayAwake;
    if (enabled) {
      wifiLockHandler.post(
          () -> wifiLockManagerInternal.updateWifiLock(/* enabled= */ true, stayAwake));
    }
  }

  /** Internal methods called on the wifi lock Looper. */
  private static final class WifiLockManagerInternal {

    private final Context applicationContext;

    @Nullable private WifiLock wifiLock;

    public WifiLockManagerInternal(Context applicationContext) {
      this.applicationContext = applicationContext;
    }

    public void updateWifiLock(boolean enabled, boolean stayAwake) {
      if (enabled && wifiLock == null) {
        WifiManager wifiManager =
            (WifiManager)
                applicationContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager == null) {
          Log.w(TAG, "WifiManager is null, therefore not creating the WifiLock.");
          return;
        }
        wifiLock = wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL_HIGH_PERF, WIFI_LOCK_TAG);
        wifiLock.setReferenceCounted(false);
      }

      if (wifiLock == null) {
        return;
      }

      if (enabled && stayAwake) {
        wifiLock.acquire();
      } else {
        wifiLock.release();
      }
    }
  }
}
