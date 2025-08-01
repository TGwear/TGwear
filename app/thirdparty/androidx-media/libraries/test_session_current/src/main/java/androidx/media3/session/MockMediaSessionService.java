/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.session;

import static androidx.media3.test.session.common.CommonConstants.MEDIA_CONTROLLER_PACKAGE_NAME_API_21;
import static androidx.media3.test.session.common.CommonConstants.SUPPORT_APP_PACKAGE_NAME;

import android.content.Intent;
import android.os.HandlerThread;
import android.os.IBinder;
import android.text.TextUtils;
import androidx.annotation.Nullable;
import androidx.media3.common.util.ConditionVariable;
import androidx.media3.session.MediaSession.ControllerInfo;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

/** A mock MediaSessionService */
public class MockMediaSessionService extends MediaSessionService {
  /** ID of the session that this service will create. */
  public static final String ID = "TestSession";

  private final AtomicInteger boundControllerCount;
  private final ConditionVariable allControllersUnbound;

  @Nullable public MediaSession session;
  @Nullable private HandlerThread handlerThread;
  private boolean cleanupServiceRegistryOnDestroy;

  public MockMediaSessionService() {
    boundControllerCount = new AtomicInteger(/* initialValue= */ 0);
    allControllersUnbound = new ConditionVariable();
    allControllersUnbound.open();
    cleanupServiceRegistryOnDestroy = true;
  }

  /**
   * Whether the service should clean up the service registry {@link #onDestroy()} by calling {@link
   * TestServiceRegistry#cleanUp()} on {@link TestServiceRegistry#getInstance()}.
   *
   * <p>The cleanup will release all sessions of the service. A test can clean up when tearing down
   * the test, to prevent the sessions to be released by the service.
   */
  public void setCleanupServiceRegistryOnDestroy(boolean cleanupServiceRegistryOnDestroy) {
    this.cleanupServiceRegistryOnDestroy = cleanupServiceRegistryOnDestroy;
  }

  /** Returns whether at least one controller is bound to this service. */
  public boolean hasBoundController() {
    return !allControllersUnbound.isOpen();
  }

  /**
   * Blocks until all bound controllers unbind.
   *
   * @param timeoutMs The block timeout in milliseconds.
   * @throws TimeoutException If the block timed out.
   * @throws InterruptedException If the block was interrupted.
   */
  public void blockUntilAllControllersUnbind(long timeoutMs)
      throws TimeoutException, InterruptedException {
    if (!allControllersUnbound.block(timeoutMs)) {
      throw new TimeoutException();
    }
  }

  @Override
  public void onCreate() {
    TestServiceRegistry.getInstance().setServiceInstance(this);
    super.onCreate();
    handlerThread = new HandlerThread("MockMediaSessionService");
    handlerThread.start();
  }

  @Override
  public IBinder onBind(@Nullable Intent intent) {
    boundControllerCount.incrementAndGet();
    allControllersUnbound.close();
    return super.onBind(intent);
  }

  @Override
  public boolean onUnbind(Intent intent) {
    if (boundControllerCount.decrementAndGet() == 0) {
      allControllersUnbound.open();
    }
    return super.onUnbind(intent);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (cleanupServiceRegistryOnDestroy) {
      TestServiceRegistry.getInstance().cleanUp();
    }
    handlerThread.quitSafely();
  }

  @Override
  public MediaSession onGetSession(ControllerInfo controllerInfo) {
    TestServiceRegistry registry = TestServiceRegistry.getInstance();
    TestServiceRegistry.OnGetSessionHandler onGetSessionHandler = registry.getOnGetSessionHandler();
    if (onGetSessionHandler != null) {
      return onGetSessionHandler.onGetSession(controllerInfo);
    }

    if (session == null) {
      MediaSession.Callback callback = registry.getSessionCallback();
      MockPlayer player =
          new MockPlayer.Builder().setApplicationLooper(handlerThread.getLooper()).build();
      session =
          new MediaSession.Builder(MockMediaSessionService.this, player)
              .setId(ID)
              .setCallback(callback != null ? callback : new TestSessionCallback())
              .build();
    }
    return session;
  }

  private static class TestSessionCallback implements MediaSession.Callback {

    @Override
    public MediaSession.ConnectionResult onConnect(
        MediaSession session, ControllerInfo controller) {
      if (TextUtils.equals(SUPPORT_APP_PACKAGE_NAME, controller.getPackageName())
          || TextUtils.equals(MEDIA_CONTROLLER_PACKAGE_NAME_API_21, controller.getPackageName())) {
        return MediaSession.Callback.super.onConnect(session, controller);
      }
      return MediaSession.ConnectionResult.reject();
    }
  }
}
