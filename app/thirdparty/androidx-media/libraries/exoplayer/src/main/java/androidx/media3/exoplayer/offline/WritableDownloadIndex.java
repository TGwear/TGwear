/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.offline;

import androidx.annotation.WorkerThread;
import androidx.media3.common.util.UnstableApi;
import java.io.IOException;

/** A writable index of {@link Download Downloads}. */
@WorkerThread
@UnstableApi
public interface WritableDownloadIndex extends DownloadIndex {

  /**
   * Adds or replaces a {@link Download}.
   *
   * <p>This method may be slow and shouldn't normally be called on the main thread.
   *
   * @param download The {@link Download} to be added.
   * @throws IOException If an error occurs setting the state.
   */
  void putDownload(Download download) throws IOException;

  /**
   * Removes the download with the given ID. Does nothing if a download with the given ID does not
   * exist.
   *
   * <p>This method may be slow and shouldn't normally be called on the main thread.
   *
   * @param id The ID of the download to remove.
   * @throws IOException If an error occurs removing the state.
   */
  void removeDownload(String id) throws IOException;

  /**
   * Sets all {@link Download#STATE_DOWNLOADING} states to {@link Download#STATE_QUEUED}.
   *
   * <p>This method may be slow and shouldn't normally be called on the main thread.
   *
   * @throws IOException If an error occurs updating the state.
   */
  void setDownloadingStatesToQueued() throws IOException;

  /**
   * Sets all states to {@link Download#STATE_REMOVING}.
   *
   * <p>This method may be slow and shouldn't normally be called on the main thread.
   *
   * @throws IOException If an error occurs updating the state.
   */
  void setStatesToRemoving() throws IOException;

  /**
   * Sets the stop reason of the downloads in a terminal state ({@link Download#STATE_COMPLETED},
   * {@link Download#STATE_FAILED}).
   *
   * <p>This method may be slow and shouldn't normally be called on the main thread.
   *
   * @param stopReason The stop reason.
   * @throws IOException If an error occurs updating the state.
   */
  void setStopReason(int stopReason) throws IOException;

  /**
   * Sets the stop reason of the download with the given ID in a terminal state ({@link
   * Download#STATE_COMPLETED}, {@link Download#STATE_FAILED}). Does nothing if a download with the
   * given ID does not exist, or if it's not in a terminal state.
   *
   * <p>This method may be slow and shouldn't normally be called on the main thread.
   *
   * @param id The ID of the download to update.
   * @param stopReason The stop reason.
   * @throws IOException If an error occurs updating the state.
   */
  void setStopReason(String id, int stopReason) throws IOException;
}
