/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer;

import static com.google.common.truth.Truth.assertThat;
import static org.robolectric.Shadows.shadowOf;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.HandlerThread;
import androidx.media3.common.util.Clock;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit test for {@link WifiLockManager}. */
@RunWith(AndroidJUnit4.class)
public class WifiLockManagerTest {

  private Context context;
  private HandlerThread handlerThread;
  private WifiManager wifiManager;

  @Before
  public void setUp() {
    context = ApplicationProvider.getApplicationContext();
    handlerThread = new HandlerThread("wifiLockManagerTest");
    handlerThread.start();
    wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
  }

  @After
  public void tearDown() {
    handlerThread.quit();
  }

  @Test
  public void stayAwakeFalse_wifiLockIsNeverHeld() {
    int initialLockCount = shadowOf(wifiManager).getActiveLockCount();
    WifiLockManager wifiLockManager =
        new WifiLockManager(context, handlerThread.getLooper(), Clock.DEFAULT);

    wifiLockManager.setEnabled(true);
    wifiLockManager.setStayAwake(false);
    shadowOf(handlerThread.getLooper()).idle();
    int lockCountWhenEnabled = shadowOf(wifiManager).getActiveLockCount();
    wifiLockManager.setEnabled(false);
    shadowOf(handlerThread.getLooper()).idle();
    int lockCountAfterDisable = shadowOf(wifiManager).getActiveLockCount();

    assertThat(lockCountWhenEnabled).isAtMost(initialLockCount);
    assertThat(lockCountAfterDisable).isAtMost(initialLockCount);
  }

  @Test
  public void stayAwakeTrue_wifiLockIsOnlyHeldWhenEnabled() {
    WifiLockManager wifiLockManager =
        new WifiLockManager(context, handlerThread.getLooper(), Clock.DEFAULT);
    wifiLockManager.setEnabled(true);
    shadowOf(handlerThread.getLooper()).idle();

    int initialLockCount = shadowOf(wifiManager).getActiveLockCount();
    wifiLockManager.setStayAwake(true);
    shadowOf(handlerThread.getLooper()).idle();
    int lockCountWhenStayAwake = shadowOf(wifiManager).getActiveLockCount();
    wifiLockManager.setEnabled(false);
    shadowOf(handlerThread.getLooper()).idle();
    int lockCountAfterDisable = shadowOf(wifiManager).getActiveLockCount();

    assertThat(lockCountWhenStayAwake).isGreaterThan(initialLockCount);
    assertThat(lockCountAfterDisable).isLessThan(lockCountWhenStayAwake);
  }
}
