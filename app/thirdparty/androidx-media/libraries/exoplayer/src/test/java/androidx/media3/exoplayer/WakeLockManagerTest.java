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
import android.os.HandlerThread;
import android.os.PowerManager.WakeLock;
import androidx.media3.common.util.Clock;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.shadows.ShadowPowerManager;

/** Unit tests for {@link WakeLockManager} */
@RunWith(AndroidJUnit4.class)
public class WakeLockManagerTest {

  private Context context;
  private HandlerThread handlerThread;

  @Before
  public void setUp() {
    context = ApplicationProvider.getApplicationContext();
    handlerThread = new HandlerThread("wakeLockManagerTest");
    handlerThread.start();
  }

  @After
  public void tearDown() {
    handlerThread.quit();
  }

  @Test
  public void stayAwakeFalse_wakeLockIsNeverHeld() {
    WakeLockManager wakeLockManager =
        new WakeLockManager(context, handlerThread.getLooper(), Clock.DEFAULT);
    wakeLockManager.setEnabled(true);
    wakeLockManager.setStayAwake(false);
    shadowOf(handlerThread.getLooper()).idle();

    WakeLock wakeLock = ShadowPowerManager.getLatestWakeLock();
    assertThat(wakeLock.isHeld()).isFalse();

    wakeLockManager.setEnabled(false);
    shadowOf(handlerThread.getLooper()).idle();

    assertThat(wakeLock.isHeld()).isFalse();
  }

  @Test
  public void stayAwakeTrue_wakeLockIsOnlyHeldWhenEnabled() {
    WakeLockManager wakeLockManager =
        new WakeLockManager(context, handlerThread.getLooper(), Clock.DEFAULT);
    wakeLockManager.setEnabled(true);
    wakeLockManager.setStayAwake(true);
    shadowOf(handlerThread.getLooper()).idle();

    WakeLock wakeLock = ShadowPowerManager.getLatestWakeLock();

    assertThat(wakeLock.isHeld()).isTrue();

    wakeLockManager.setEnabled(false);
    shadowOf(handlerThread.getLooper()).idle();

    assertThat(wakeLock.isHeld()).isFalse();
  }
}
