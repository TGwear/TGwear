/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.session;

import static androidx.media3.common.util.Util.postOrRun;
import static androidx.media3.session.LegacyConversions.extractMaxCommandsForMediaItemFromRootHints;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.media3.common.util.ConditionVariable;
import androidx.media3.common.util.Log;
import androidx.media3.session.MediaSession.ControllerInfo;
import androidx.media3.session.legacy.MediaBrowserCompat.MediaItem;
import androidx.media3.session.legacy.MediaBrowserServiceCompat;
import androidx.media3.session.legacy.MediaSessionCompat;
import androidx.media3.session.legacy.MediaSessionManager;
import androidx.media3.session.legacy.MediaSessionManager.RemoteUserInfo;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Implementation of {@link MediaBrowserServiceCompat} for interoperability between {@link
 * MediaLibraryService} and {@code android.support.v4.media.MediaBrowserCompat}.
 */
/* package */ class MediaSessionServiceLegacyStub extends MediaBrowserServiceCompat {

  private static final String TAG = "MSSLegacyStub";

  private final MediaSessionManager manager;
  private final MediaSessionImpl sessionImpl;
  private final ConnectedControllersManager<RemoteUserInfo> connectedControllersManager;

  /** Creates a new instance. Caller must call {@link #initialize} to the instance. */
  public MediaSessionServiceLegacyStub(MediaSessionImpl sessionImpl) {
    super();
    manager = MediaSessionManager.getSessionManager(sessionImpl.getContext());
    this.sessionImpl = sessionImpl;
    connectedControllersManager = new ConnectedControllersManager<>(sessionImpl);
  }

  public void initialize(MediaSessionCompat.Token token) {
    attachToBaseContext(sessionImpl.getContext());
    onCreate();
    setSessionToken(token);
  }

  @Override
  @Nullable
  public BrowserRoot onGetRoot(
      @Nullable String clientPackageName, int clientUid, @Nullable Bundle rootHints) {
    RemoteUserInfo info = getCurrentBrowserInfo();
    MediaSession.ControllerInfo controller =
        createControllerInfo(info, rootHints != null ? rootHints : Bundle.EMPTY);

    AtomicReference<MediaSession.ConnectionResult> resultReference = new AtomicReference<>();
    ConditionVariable haveResult = new ConditionVariable();
    postOrRun(
        sessionImpl.getApplicationHandler(),
        () -> {
          resultReference.set(sessionImpl.onConnectOnHandler(controller));
          haveResult.open();
        });
    try {
      haveResult.block();
    } catch (InterruptedException e) {
      Log.e(TAG, "Couldn't get a result from onConnect", e);
      return null;
    }
    MediaSession.ConnectionResult result = resultReference.get();
    if (!result.isAccepted) {
      return null;
    }
    connectedControllersManager.addController(
        info, controller, result.availableSessionCommands, result.availablePlayerCommands);
    // No library root, but keep browser compat connected to allow getting session.
    return MediaUtils.defaultBrowserRoot;
  }

  @Override
  public void onLoadChildren(@Nullable String parentId, Result<List<MediaItem>> result) {
    result.sendResult(/* result= */ null);
  }

  public ControllerInfo createControllerInfo(RemoteUserInfo info, Bundle rootHints) {
    return new ControllerInfo(
        info,
        ControllerInfo.LEGACY_CONTROLLER_VERSION,
        ControllerInfo.LEGACY_CONTROLLER_INTERFACE_VERSION,
        manager.isTrustedForMediaControl(info),
        /* cb= */ null,
        /* connectionHints= */ rootHints,
        extractMaxCommandsForMediaItemFromRootHints(rootHints));
  }

  public final MediaSessionManager getMediaSessionManager() {
    return manager;
  }

  public final ConnectedControllersManager<RemoteUserInfo> getConnectedControllersManager() {
    return connectedControllersManager;
  }
}
