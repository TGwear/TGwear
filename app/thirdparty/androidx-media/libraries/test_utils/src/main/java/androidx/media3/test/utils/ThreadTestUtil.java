/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.test.utils;

import android.os.Looper;
import androidx.annotation.GuardedBy;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.ConditionVariable;
import androidx.media3.common.util.UnstableApi;
import com.google.common.collect.ArrayListMultimap;

/** Static utility to coordinate threads in testing environments. */
@UnstableApi
public final class ThreadTestUtil {

  @GuardedBy("blockedThreadConditions")
  private static final ArrayListMultimap<Looper, ConditionVariable> blockedThreadConditions =
      ArrayListMultimap.create();

  /**
   * Registers that the current thread will be blocked with the provided {@link ConditionVariable}
   * until the specified {@link Looper} reports to have made progress via {@link
   * #unblockThreadsWaitingForProgressOnCurrentLooper()}.
   *
   * @param conditionVariable The {@link ConditionVariable} that will block the current thread.
   * @param looper The {@link Looper} that must report progress to unblock the current thread. Must
   *     not be the {@link Looper} of the current thread.
   */
  public static void registerThreadIsBlockedUntilProgressOnLooper(
      ConditionVariable conditionVariable, Looper looper) {
    Assertions.checkArgument(looper != Looper.myLooper());
    synchronized (blockedThreadConditions) {
      blockedThreadConditions.put(looper, conditionVariable);
    }
  }

  /** Unblocks any threads that are waiting for progress on the current {@link Looper} thread. */
  public static void unblockThreadsWaitingForProgressOnCurrentLooper() {
    Looper myLooper = Assertions.checkNotNull(Looper.myLooper());
    synchronized (blockedThreadConditions) {
      for (ConditionVariable condition : blockedThreadConditions.removeAll(myLooper)) {
        condition.open();
      }
    }
  }

  private ThreadTestUtil() {}
}
