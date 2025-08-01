/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.session;

import static androidx.media3.test.session.common.CommonConstants.MOCK_MEDIA_BROWSER_SERVICE_COMPAT;
import static androidx.media3.test.session.common.TestUtils.SERVICE_CONNECTION_TIMEOUT_MS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import androidx.media3.test.session.common.IRemoteMediaBrowserServiceCompat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

/** A client to control service app's MockMediaBrowserServiceCompat remotely. */
public class RemoteMediaBrowserServiceCompat {

  private final Context context;
  private final ServiceConnection serviceConnection;
  private final CountDownLatch connectedLatch;
  private AtomicReference<IRemoteMediaBrowserServiceCompat> binderRef = new AtomicReference<>();

  public RemoteMediaBrowserServiceCompat(Context context) {
    this.context = context;
    serviceConnection = new MyServiceConnection();
    connectedLatch = new CountDownLatch(1);

    Intent intent = new Intent();
    intent.setComponent(MOCK_MEDIA_BROWSER_SERVICE_COMPAT);

    boolean bound = context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    if (!bound) {
      throw new IllegalArgumentException("Could not bind to the service");
    }

    boolean connected;
    try {
      connected = connectedLatch.await(SERVICE_CONNECTION_TIMEOUT_MS, MILLISECONDS);
    } catch (InterruptedException e) {
      throw new IllegalStateException("Interrupted while waiting for connection", e);
    }
    if (!connected) {
      throw new IllegalStateException("ServiceConnection was not made in time");
    }
  }

  public void release() {
    context.unbindService(serviceConnection);
  }

  /** Calls MockMediaBrowserServiceCompat#setMediaBrowserServiceProxy for a specific test case. */
  public void setProxyForTest(String testName) throws RemoteException {
    getBinderOrThrow().setProxyForTest(testName);
  }

  /** Calls MockMediaBrowserServiceCompat#notifyChildrenChanged. */
  public void notifyChildrenChanged(String parentId) throws RemoteException {
    getBinderOrThrow().notifyChildrenChanged(parentId);
  }

  private IRemoteMediaBrowserServiceCompat getBinderOrThrow() {
    IRemoteMediaBrowserServiceCompat binder = binderRef.get();
    if (binder == null) {
      throw new IllegalStateException("service is not connected");
    }
    return binder;
  }

  private class MyServiceConnection implements ServiceConnection {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      binderRef.set(IRemoteMediaBrowserServiceCompat.Stub.asInterface(service));
      connectedLatch.countDown();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
      binderRef.set(null);
    }
  }
}
