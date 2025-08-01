/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.common.util;

import androidx.annotation.Nullable;
import java.util.concurrent.Executor;

/** A utility class to obtain an {@link Executor} for background tasks. */
@UnstableApi
public final class BackgroundExecutor {

  @SuppressWarnings("NonFinalStaticField")
  @Nullable
  private static Executor staticInstance;

  /**
   * Returns an {@link Executor} for background tasks.
   *
   * <p>Must only be used for quick, high-priority tasks to ensure other background tasks are not
   * blocked.
   *
   * <p>The thread is guaranteed to be alive for the lifetime of the application.
   */
  public static synchronized Executor get() {
    if (staticInstance == null) {
      staticInstance = Util.newSingleThreadExecutor("ExoPlayer:BackgroundExecutor");
    }
    return staticInstance;
  }

  /**
   * Sets the {@link Executor} to be returned from {@link #get()}.
   *
   * <p>Note that the thread of the provided {@link Executor} must stay alive for the lifetime of
   * the application.
   *
   * @param executor An {@link Executor} that runs tasks on background threads and should only be
   *     used for quick, high-priority tasks to ensure other background tasks are not blocked.
   */
  public static synchronized void set(Executor executor) {
    staticInstance = executor;
  }

  private BackgroundExecutor() {}
}
