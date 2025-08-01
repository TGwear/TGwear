/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.transformer;

import static androidx.media3.common.util.Assertions.checkNotNull;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import androidx.media3.common.util.Util;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

/**
 * A watchdog timer.
 *
 * <p>Callers must follow this sequence:
 *
 * <ul>
 *   <li>Call {@link #start()} once to start the timer.
 *   <li>Call {@link #reset()} periodically to reset the timer and prevent a timeout.
 *   <li>Call {@link #stop()} once to stop the timer.
 * </ul>
 */
/* package */ final class WatchdogTimer {
  /** A listener for timeout events. */
  public interface Listener {
    /** Called when a timeout occurs. */
    void onTimeout();
  }

  private final long timeoutDurationMs;
  private final Listener listener;
  private final ScheduledExecutorService watchdogScheduledExecutorService;

  private @MonotonicNonNull ScheduledFuture<?> timeoutScheduledFuture;

  /**
   * Creates an instance.
   *
   * @param timeoutDurationMs The timeout duration in milliseconds.
   * @param listener The {@link Listener} to be notified when a timeout occurs.
   */
  public WatchdogTimer(long timeoutDurationMs, Listener listener) {
    this.timeoutDurationMs = timeoutDurationMs;
    this.listener = listener;
    watchdogScheduledExecutorService = Util.newSingleThreadScheduledExecutor("WatchdogTimer");
  }

  /** Starts the watchdog timer. */
  public void start() {
    scheduleNewTimer();
  }

  /** Resets the watchdog timer. */
  public void reset() {
    cancelExistingTimer();
    scheduleNewTimer();
  }

  /**
   * Stops the watchdog timer.
   *
   * <p>The watchdog timer can not be used after its stopped.
   */
  public void stop() {
    cancelExistingTimer();
    watchdogScheduledExecutorService.shutdownNow();
  }

  private void cancelExistingTimer() {
    checkNotNull(timeoutScheduledFuture).cancel(/* mayInterruptIfRunning= */ false);
  }

  private void scheduleNewTimer() {
    timeoutScheduledFuture =
        watchdogScheduledExecutorService.schedule(
            listener::onTimeout, timeoutDurationMs, MILLISECONDS);
  }
}
