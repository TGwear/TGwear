/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.test.utils;

import static com.google.common.truth.Truth.assertThat;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.common.util.Util;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

/** Helper class to simulate main/UI thread in tests. */
@UnstableApi
public final class DummyMainThread {

  /** {@link Runnable} variant which can throw a checked exception. */
  public interface TestRunnable {
    void run() throws Exception;
  }

  /** Default timeout value used for {@link #runOnMainThread(Runnable)}. */
  public static final int TIMEOUT_MS = 10_000;

  private final HandlerThread thread;
  private final Handler handler;

  public DummyMainThread() {
    thread = new HandlerThread("DummyMainThread");
    thread.start();
    handler = new Handler(thread.getLooper());
  }

  /**
   * Runs the provided {@link Runnable} on the main thread, blocking until execution completes or
   * until {@link #TIMEOUT_MS} milliseconds have passed.
   *
   * @param runnable The {@link Runnable} to run.
   */
  public void runOnMainThread(final Runnable runnable) {
    runOnMainThread(TIMEOUT_MS, runnable);
  }

  /**
   * Runs the provided {@link Runnable} on the main thread, blocking until execution completes or
   * until timeout milliseconds have passed.
   *
   * @param timeoutMs The maximum time to wait in milliseconds.
   * @param runnable The {@link Runnable} to run.
   */
  public void runOnMainThread(int timeoutMs, final Runnable runnable) {
    runTestOnMainThread(timeoutMs, runnable::run);
  }

  /**
   * Runs the provided {@link TestRunnable} on the main thread, blocking until execution completes
   * or until {@link #TIMEOUT_MS} milliseconds have passed.
   *
   * @param runnable The {@link TestRunnable} to run.
   */
  public void runTestOnMainThread(final TestRunnable runnable) {
    runTestOnMainThread(TIMEOUT_MS, runnable);
  }

  /**
   * Runs the provided {@link TestRunnable} on the main thread, blocking until execution completes
   * or until timeout milliseconds have passed.
   *
   * @param timeoutMs The maximum time to wait in milliseconds.
   * @param runnable The {@link TestRunnable} to run.
   */
  public void runTestOnMainThread(int timeoutMs, final TestRunnable runnable) {
    if (Looper.myLooper() == handler.getLooper()) {
      try {
        runnable.run();
      } catch (Exception e) {
        Util.sneakyThrow(e);
      }
    } else {
      CountDownLatch finishedLatch = new CountDownLatch(1);
      AtomicReference<Throwable> thrown = new AtomicReference<>();
      handler.post(
          () -> {
            try {
              runnable.run();
            } catch (Throwable t) {
              thrown.set(t);
            }
            finishedLatch.countDown();
          });
      try {
        assertThat(finishedLatch.await(timeoutMs, MILLISECONDS)).isTrue();
      } catch (InterruptedException e) {
        Util.sneakyThrow(e);
      }
      if (thrown.get() != null) {
        Util.sneakyThrow(thrown.get());
      }
    }
  }

  public void release() {
    thread.quit();
  }
}
