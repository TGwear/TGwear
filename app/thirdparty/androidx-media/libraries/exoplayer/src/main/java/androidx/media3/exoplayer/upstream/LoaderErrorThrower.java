/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.upstream;

import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.upstream.Loader.Loadable;
import java.io.IOException;

/** Conditionally throws errors affecting a {@link Loader}. */
@UnstableApi
public interface LoaderErrorThrower {

  /**
   * Throws a fatal error, or a non-fatal error if loading is currently backed off and the current
   * {@link Loadable} has incurred a number of errors greater than the {@link Loader}s default
   * minimum number of retries. Else does nothing.
   *
   * @throws IOException The error.
   */
  void maybeThrowError() throws IOException;

  /**
   * Throws a fatal error, or a non-fatal error if loading is currently backed off and the current
   * {@link Loadable} has incurred a number of errors greater than the specified minimum number of
   * retries. Else does nothing.
   *
   * @param minRetryCount A minimum retry count that must be exceeded for a non-fatal error to be
   *     thrown. Should be non-negative.
   * @throws IOException The error.
   */
  void maybeThrowError(int minRetryCount) throws IOException;

  /** A {@link LoaderErrorThrower} that never throws. */
  final class Placeholder implements LoaderErrorThrower {

    @Override
    public void maybeThrowError() {
      // Do nothing.
    }

    @Override
    public void maybeThrowError(int minRetryCount) {
      // Do nothing.
    }
  }
}
