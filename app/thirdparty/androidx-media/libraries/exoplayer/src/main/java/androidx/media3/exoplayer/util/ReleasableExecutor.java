/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.util;

import androidx.media3.common.util.Consumer;
import androidx.media3.common.util.UnstableApi;
import java.util.concurrent.Executor;

/**
 * An {@link Executor} with a dedicated {@link #release} method to signal when it is not longer
 * needed.
 */
@UnstableApi
public interface ReleasableExecutor extends Executor {

  /**
   * Releases the {@link Executor}, indicating that the caller no longer requires it for executing
   * new commands.
   *
   * <p>When calling this method, there may still be pending commands that are currently executed.
   */
  void release();

  /**
   * Creates a {@link ReleasableExecutor} from an {@link Executor} and a release callback.
   *
   * @param executor The {@link Executor}
   * @param releaseCallback The release callback, accepting the {@code executor} as an argument.
   * @return The releasable executor.
   * @param <T> The type of {@link Executor}.
   */
  static <T extends Executor> ReleasableExecutor from(T executor, Consumer<T> releaseCallback) {
    return new ReleasableExecutor() {
      @Override
      public void execute(Runnable command) {
        executor.execute(command);
      }

      @Override
      public void release() {
        releaseCallback.accept(executor);
      }
    };
  }
}
