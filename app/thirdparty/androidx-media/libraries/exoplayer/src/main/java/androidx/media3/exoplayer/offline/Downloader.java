/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.offline;

import androidx.annotation.Nullable;
import androidx.media3.common.C;
import androidx.media3.common.util.UnstableApi;
import java.io.IOException;
import java.util.concurrent.CancellationException;

/** Downloads and removes a piece of content. */
@UnstableApi
public interface Downloader {

  /** Receives progress updates during download operations. */
  interface ProgressListener {

    /**
     * Called when progress is made during a download operation.
     *
     * <p>May be called directly from {@link #download}, or from any other thread used by the
     * downloader. In all cases, {@link #download} is guaranteed not to return until after the last
     * call to this method has finished executing.
     *
     * @param contentLength The length of the content in bytes, or {@link C#LENGTH_UNSET} if
     *     unknown.
     * @param bytesDownloaded The number of bytes that have been downloaded.
     * @param percentDownloaded The percentage of the content that has been downloaded, or {@link
     *     C#PERCENTAGE_UNSET}.
     */
    void onProgress(long contentLength, long bytesDownloaded, float percentDownloaded);
  }

  /**
   * Downloads the content.
   *
   * <p>If downloading fails, this method can be called again to resume the download. It cannot be
   * called again after the download has been {@link #cancel canceled}.
   *
   * <p>If downloading is canceled whilst this method is executing, then it is expected that it will
   * return reasonably quickly. However, there are no guarantees about how the method will return,
   * meaning that it can return without throwing, or by throwing any of its documented exceptions.
   * The caller must use its own knowledge about whether downloading has been canceled to determine
   * whether this is why the method has returned, rather than relying on the method returning in a
   * particular way.
   *
   * @param progressListener A listener to receive progress updates, or {@code null}.
   * @throws IOException If the download failed to complete successfully.
   * @throws InterruptedException If the download was interrupted.
   * @throws CancellationException If the download was canceled.
   */
  void download(@Nullable ProgressListener progressListener)
      throws IOException, InterruptedException;

  /**
   * Permanently cancels the downloading by this downloader. The caller should also interrupt the
   * downloading thread immediately after calling this method.
   *
   * <p>Once canceled, {@link #download} cannot be called again.
   */
  void cancel();

  /** Removes the content. */
  void remove();
}
