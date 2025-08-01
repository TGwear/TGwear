/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common;

import static java.lang.Math.max;

import androidx.media3.common.util.UnstableApi;
import androidx.media3.common.util.Util;
import java.io.IOException;
import java.util.Collections;
import java.util.PriorityQueue;

/**
 * Allows tasks with associated {@linkplain C.Priority priorities} to control how they proceed
 * relative to one another.
 *
 * <p>A task should call {@link #add(int)} to register with the manager and {@link #remove(int)} to
 * unregister. A registered task will prevent tasks of lower priority from proceeding, and should
 * call {@link #proceed(int)}, {@link #proceedNonBlocking(int)} or {@link #proceedOrThrow(int)} each
 * time it wishes to check whether it is itself allowed to proceed.
 *
 * <p>It is recommended to use predefined {@linkplain C.Priority priorities} or priority values
 * defined relative to those defaults.
 */
@UnstableApi
public final class PriorityTaskManager {

  /** Thrown when task attempts to proceed when another registered task has a higher priority. */
  public static class PriorityTooLowException extends IOException {

    public PriorityTooLowException(@C.Priority int priority, @C.Priority int highestPriority) {
      super("Priority too low [priority=" + priority + ", highest=" + highestPriority + "]");
    }
  }

  private final Object lock = new Object();

  // Guarded by lock.
  private final PriorityQueue<@C.Priority Integer> queue;
  private @C.Priority int highestPriority;

  public PriorityTaskManager() {
    queue = new PriorityQueue<>(10, Collections.reverseOrder());
    highestPriority = Integer.MIN_VALUE;
  }

  /**
   * Register a new task. The task must call {@link #remove(int)} when done.
   *
   * <p>It is recommended to use predefined {@linkplain C.Priority priorities} or priority values
   * defined relative to those defaults.
   *
   * @param priority The {@link C.Priority} of the task. Larger values indicate higher priorities.
   */
  public void add(@C.Priority int priority) {
    synchronized (lock) {
      queue.add(priority);
      highestPriority = max(highestPriority, priority);
    }
  }

  /**
   * Blocks until the task is allowed to proceed.
   *
   * @param priority The {@link C.Priority} of the task.
   * @throws InterruptedException If the thread is interrupted.
   */
  public void proceed(@C.Priority int priority) throws InterruptedException {
    synchronized (lock) {
      while (highestPriority != priority) {
        lock.wait();
      }
    }
  }

  /**
   * A non-blocking variant of {@link #proceed(int)}.
   *
   * @param priority The {@link C.Priority} of the task.
   * @return Whether the task is allowed to proceed.
   */
  public boolean proceedNonBlocking(@C.Priority int priority) {
    synchronized (lock) {
      return highestPriority == priority;
    }
  }

  /**
   * A throwing variant of {@link #proceed(int)}.
   *
   * @param priority The {@link C.Priority} of the task.
   * @throws PriorityTooLowException If the task is not allowed to proceed.
   */
  public void proceedOrThrow(@C.Priority int priority) throws PriorityTooLowException {
    synchronized (lock) {
      if (highestPriority != priority) {
        throw new PriorityTooLowException(priority, highestPriority);
      }
    }
  }

  /**
   * Unregister a task.
   *
   * @param priority The {@link C.Priority} of the task.
   */
  public void remove(@C.Priority int priority) {
    synchronized (lock) {
      queue.remove(priority);
      highestPriority = queue.isEmpty() ? Integer.MIN_VALUE : Util.castNonNull(queue.peek());
      lock.notifyAll();
    }
  }
}
