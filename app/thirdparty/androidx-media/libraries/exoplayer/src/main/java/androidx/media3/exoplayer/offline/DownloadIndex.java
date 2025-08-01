/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.offline;

import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.media3.common.util.UnstableApi;
import java.io.IOException;

/** An index of {@link Download Downloads}. */
@WorkerThread
@UnstableApi
public interface DownloadIndex {

  /**
   * Returns the {@link Download} with the given {@code id}, or null.
   *
   * <p>This method may be slow and shouldn't normally be called on the main thread.
   *
   * @param id ID of a {@link Download}.
   * @return The {@link Download} with the given {@code id}, or null if a download state with this
   *     id doesn't exist.
   * @throws IOException If an error occurs reading the state.
   */
  @Nullable
  Download getDownload(String id) throws IOException;

  /**
   * Returns a {@link DownloadCursor} to {@link Download}s with the given {@code states}.
   *
   * <p>This method may be slow and shouldn't normally be called on the main thread.
   *
   * @param states Returns only the {@link Download}s with this states. If empty, returns all.
   * @return A cursor to {@link Download}s with the given {@code states}.
   * @throws IOException If an error occurs reading the state.
   */
  DownloadCursor getDownloads(@Download.State int... states) throws IOException;
}
