/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common.util;

import android.os.Handler;
import android.os.Looper;
import androidx.annotation.Nullable;

/**
 * An interface to call through to a {@link Handler}. Instances must be created by calling {@link
 * Clock#createHandler(Looper, Handler.Callback)} on {@link Clock#DEFAULT} for all non-test cases.
 */
@UnstableApi
public interface HandlerWrapper {

  /** A message obtained from the handler. */
  interface Message {

    /** See {@link android.os.Message#sendToTarget()}. */
    void sendToTarget();

    /** See {@link android.os.Message#getTarget()}. */
    HandlerWrapper getTarget();
  }

  /** See {@link Handler#getLooper()}. */
  Looper getLooper();

  /** See {@link Handler#hasMessages(int)}. */
  boolean hasMessages(int what);

  /** See {@link Handler#obtainMessage(int)}. */
  Message obtainMessage(int what);

  /** See {@link Handler#obtainMessage(int, Object)}. */
  Message obtainMessage(int what, @Nullable Object obj);

  /** See {@link Handler#obtainMessage(int, int, int)}. */
  Message obtainMessage(int what, int arg1, int arg2);

  /** See {@link Handler#obtainMessage(int, int, int, Object)}. */
  Message obtainMessage(int what, int arg1, int arg2, @Nullable Object obj);

  /** See {@link Handler#sendMessageAtFrontOfQueue(android.os.Message)}. */
  boolean sendMessageAtFrontOfQueue(Message message);

  /** See {@link Handler#sendEmptyMessage(int)}. */
  boolean sendEmptyMessage(int what);

  /** See {@link Handler#sendEmptyMessageDelayed(int, long)}. */
  boolean sendEmptyMessageDelayed(int what, int delayMs);

  /** See {@link Handler#sendEmptyMessageAtTime(int, long)}. */
  boolean sendEmptyMessageAtTime(int what, long uptimeMs);

  /** See {@link Handler#removeMessages(int)}. */
  void removeMessages(int what);

  /** See {@link Handler#removeCallbacksAndMessages(Object)}. */
  void removeCallbacksAndMessages(@Nullable Object token);

  /** See {@link Handler#post(Runnable)}. */
  boolean post(Runnable runnable);

  /** See {@link Handler#postDelayed(Runnable, long)}. */
  boolean postDelayed(Runnable runnable, long delayMs);

  /** See {@link android.os.Handler#postAtFrontOfQueue(Runnable)}. */
  boolean postAtFrontOfQueue(Runnable runnable);
}
