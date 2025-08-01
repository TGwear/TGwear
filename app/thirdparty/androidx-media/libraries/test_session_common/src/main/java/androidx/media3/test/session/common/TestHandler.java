/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.test.session.common;

import static androidx.media3.test.session.common.TestUtils.LONG_TIMEOUT_MS;
import static com.google.common.truth.Truth.assertThat;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

/** Handler for testing. */
public class TestHandler extends Handler {

  private static final long DEFAULT_TIMEOUT_MS = LONG_TIMEOUT_MS;

  public TestHandler(Looper looper) {
    super(looper);
  }

  /** Posts {@link Runnable} and waits until it finishes, or runs it directly on the same looper. */
  public void postAndSync(TestRunnable runnable) throws Exception {
    postAndSync(runnable, DEFAULT_TIMEOUT_MS);
  }

  /** Posts {@link Runnable} and waits until it finishes, or runs it directly on the same looper. */
  public void postAndSync(TestRunnable runnable, long timeoutMs) throws Exception {
    if (getLooper() == Looper.myLooper()) {
      runnable.run();
    } else {
      AtomicReference<Exception> exception = new AtomicReference<>();
      CountDownLatch latch = new CountDownLatch(1);
      post(
          () -> {
            try {
              runnable.run();
            } catch (Exception e) {
              exception.set(e);
            }
            latch.countDown();
          });
      assertThat(latch.await(timeoutMs, MILLISECONDS)).isTrue();
      if (exception.get() != null) {
        throw exception.get();
      }
    }
  }

  /**
   * Posts {@link Callable} and returns the result when it finishes, or calls it directly on the
   * same looper.
   */
  public <V> V postAndSync(Callable<V> callable) throws Exception {
    return postAndSync(callable, DEFAULT_TIMEOUT_MS);
  }

  /**
   * Posts {@link Callable} and returns the result when it finishes, or calls it directly on the
   * same looper.
   */
  public <V> V postAndSync(Callable<V> callable, long timeoutMs) throws Exception {
    if (getLooper() == Looper.myLooper()) {
      return callable.call();
    } else {
      AtomicReference<V> result = new AtomicReference<>();
      AtomicReference<Exception> exception = new AtomicReference<>();
      CountDownLatch latch = new CountDownLatch(1);
      post(
          () -> {
            try {
              result.set(callable.call());
            } catch (Exception e) {
              exception.set(e);
            }
            latch.countDown();
          });
      assertThat(latch.await(timeoutMs, MILLISECONDS)).isTrue();
      if (exception.get() != null) {
        throw exception.get();
      }
      return result.get();
    }
  }

  /** {@link Runnable} variant which can throw a checked exception. */
  public interface TestRunnable {
    void run() throws Exception;
  }
}
