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
import org.checkerframework.checker.initialization.qual.UnknownInitialization;

/**
 * An interface through which system clocks can be read and {@link HandlerWrapper}s created. The
 * {@link #DEFAULT} implementation must be used for all non-test cases.
 */
@UnstableApi
public interface Clock {

  /** Default {@link Clock} to use for all non-test cases. */
  Clock DEFAULT = new SystemClock();

  /**
   * Returns the current time in milliseconds since the Unix Epoch.
   *
   * @see System#currentTimeMillis()
   */
  long currentTimeMillis();

  /**
   * @see android.os.SystemClock#elapsedRealtime()
   */
  long elapsedRealtime();

  /**
   * @see android.os.SystemClock#uptimeMillis()
   */
  long uptimeMillis();

  /** See {@link java.lang.System#nanoTime()} */
  long nanoTime();

  /**
   * Creates a {@link HandlerWrapper} using a specified looper and a specified callback for handling
   * messages.
   *
   * @see Handler#Handler(Looper, Handler.Callback)
   */
  HandlerWrapper createHandler(
      Looper looper, @Nullable Handler.@UnknownInitialization Callback callback);

  /**
   * Notifies the clock that the current thread is about to be blocked and won't return until a
   * condition on another thread becomes true.
   *
   * <p>Should be a no-op for all non-test cases.
   */
  void onThreadBlocked();
}
