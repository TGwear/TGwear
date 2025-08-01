/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common.util;

import static androidx.media3.common.util.Assertions.checkArgument;
import static androidx.media3.common.util.Assertions.checkNotNull;

import android.os.Handler;
import android.os.Looper;
import androidx.annotation.GuardedBy;
import androidx.annotation.Nullable;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.ArrayList;
import java.util.List;

/** The standard implementation of {@link HandlerWrapper}. */
/* package */ final class SystemHandlerWrapper implements HandlerWrapper {

  private static final int MAX_POOL_SIZE = 50;

  @GuardedBy("messagePool")
  private static final List<SystemMessage> messagePool = new ArrayList<>(MAX_POOL_SIZE);

  private final android.os.Handler handler;

  public SystemHandlerWrapper(android.os.Handler handler) {
    this.handler = handler;
  }

  @Override
  public Looper getLooper() {
    return handler.getLooper();
  }

  @Override
  public boolean hasMessages(int what) {
    // Using what == 0 when using hasMessages is dangerous as it also checks for pending Runnables.
    checkArgument(what != 0);
    return handler.hasMessages(what);
  }

  @Override
  public Message obtainMessage(int what) {
    return obtainSystemMessage().setMessage(handler.obtainMessage(what), /* handler= */ this);
  }

  @Override
  public Message obtainMessage(int what, @Nullable Object obj) {
    return obtainSystemMessage().setMessage(handler.obtainMessage(what, obj), /* handler= */ this);
  }

  @Override
  public Message obtainMessage(int what, int arg1, int arg2) {
    return obtainSystemMessage()
        .setMessage(handler.obtainMessage(what, arg1, arg2), /* handler= */ this);
  }

  @Override
  public Message obtainMessage(int what, int arg1, int arg2, @Nullable Object obj) {
    return obtainSystemMessage()
        .setMessage(handler.obtainMessage(what, arg1, arg2, obj), /* handler= */ this);
  }

  @Override
  public boolean sendMessageAtFrontOfQueue(Message message) {
    return ((SystemMessage) message).sendAtFrontOfQueue(handler);
  }

  @Override
  public boolean sendEmptyMessage(int what) {
    return handler.sendEmptyMessage(what);
  }

  @Override
  public boolean sendEmptyMessageDelayed(int what, int delayMs) {
    return handler.sendEmptyMessageDelayed(what, delayMs);
  }

  @Override
  public boolean sendEmptyMessageAtTime(int what, long uptimeMs) {
    return handler.sendEmptyMessageAtTime(what, uptimeMs);
  }

  @Override
  public void removeMessages(int what) {
    // Using what == 0 when removing messages is dangerous as it also removes all pending Runnables.
    checkArgument(what != 0);
    handler.removeMessages(what);
  }

  @Override
  public void removeCallbacksAndMessages(@Nullable Object token) {
    handler.removeCallbacksAndMessages(token);
  }

  @Override
  public boolean post(Runnable runnable) {
    return handler.post(runnable);
  }

  @Override
  public boolean postDelayed(Runnable runnable, long delayMs) {
    return handler.postDelayed(runnable, delayMs);
  }

  @Override
  public boolean postAtFrontOfQueue(Runnable runnable) {
    return handler.postAtFrontOfQueue(runnable);
  }

  private static SystemMessage obtainSystemMessage() {
    synchronized (messagePool) {
      return messagePool.isEmpty()
          ? new SystemMessage()
          : messagePool.remove(messagePool.size() - 1);
    }
  }

  private static void recycleMessage(SystemMessage message) {
    synchronized (messagePool) {
      if (messagePool.size() < MAX_POOL_SIZE) {
        messagePool.add(message);
      }
    }
  }

  private static final class SystemMessage implements Message {

    @Nullable private android.os.Message message;
    @Nullable private SystemHandlerWrapper handler;

    @CanIgnoreReturnValue
    public SystemMessage setMessage(android.os.Message message, SystemHandlerWrapper handler) {
      this.message = message;
      this.handler = handler;
      return this;
    }

    public boolean sendAtFrontOfQueue(Handler handler) {
      boolean success = handler.sendMessageAtFrontOfQueue(checkNotNull(message));
      recycle();
      return success;
    }

    @Override
    public void sendToTarget() {
      checkNotNull(message).sendToTarget();
      recycle();
    }

    @Override
    public HandlerWrapper getTarget() {
      return checkNotNull(handler);
    }

    private void recycle() {
      message = null;
      handler = null;
      recycleMessage(this);
    }
  }
}
