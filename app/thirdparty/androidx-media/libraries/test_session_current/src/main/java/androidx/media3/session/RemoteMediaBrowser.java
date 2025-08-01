/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.session;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import androidx.annotation.Nullable;
import androidx.media3.common.MediaItem;
import androidx.media3.session.MediaLibraryService.LibraryParams;
import com.google.common.collect.ImmutableList;

/**
 * Represents remote {@link MediaBrowser} the client app's MediaControllerService. Users can run
 * {@link MediaBrowser} methods remotely with this object.
 */
public class RemoteMediaBrowser extends RemoteMediaController {

  /**
   * Create a {@link MediaBrowser} in the client app. Should NOT be called main thread.
   *
   * @param waitForConnection true if the remote browser needs to wait for the connection, false
   *     otherwise.
   * @param connectionHints connection hints
   */
  public RemoteMediaBrowser(
      Context context, SessionToken token, boolean waitForConnection, Bundle connectionHints)
      throws RemoteException {
    super(context, token, connectionHints, waitForConnection);
  }

  /** {@link MediaBrowser} methods. */
  public LibraryResult<MediaItem> getLibraryRoot(@Nullable LibraryParams params)
      throws RemoteException {
    Bundle result = binder.getLibraryRoot(controllerId, params == null ? null : params.toBundle());
    return LibraryResult.fromItemBundle(result);
  }

  public LibraryResult<Void> subscribe(String parentId, @Nullable LibraryParams params)
      throws RemoteException {
    Bundle result =
        binder.subscribe(controllerId, parentId, params == null ? null : params.toBundle());
    return LibraryResult.fromVoidBundle(result);
  }

  public LibraryResult<Void> unsubscribe(String parentId) throws RemoteException {
    Bundle result = binder.unsubscribe(controllerId, parentId);
    return LibraryResult.fromVoidBundle(result);
  }

  public LibraryResult<ImmutableList<MediaItem>> getChildren(
      String parentId, int page, int pageSize, @Nullable LibraryParams params)
      throws RemoteException {
    Bundle result =
        binder.getChildren(
            controllerId, parentId, page, pageSize, params == null ? null : params.toBundle());
    return LibraryResult.fromItemListBundle(result);
  }

  public LibraryResult<MediaItem> getItem(String mediaId) throws RemoteException {
    Bundle result = binder.getItem(controllerId, mediaId);
    return LibraryResult.fromItemBundle(result);
  }

  public LibraryResult<Void> search(String query, @Nullable LibraryParams params)
      throws RemoteException {
    Bundle result = binder.search(controllerId, query, params == null ? null : params.toBundle());
    return LibraryResult.fromVoidBundle(result);
  }

  public LibraryResult<ImmutableList<MediaItem>> getSearchResult(
      String query, int page, int pageSize, @Nullable LibraryParams params) throws RemoteException {
    Bundle result =
        binder.getSearchResult(
            controllerId, query, page, pageSize, params == null ? null : params.toBundle());
    return LibraryResult.fromItemListBundle(result);
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Non-public methods
  ////////////////////////////////////////////////////////////////////////////////

  @Override
  protected void create(SessionToken token, Bundle connectionHints, boolean waitForConnection)
      throws RemoteException {
    binder.create(
        /* isBrowser= */ true, controllerId, token.toBundle(), connectionHints, waitForConnection);
  }
}
