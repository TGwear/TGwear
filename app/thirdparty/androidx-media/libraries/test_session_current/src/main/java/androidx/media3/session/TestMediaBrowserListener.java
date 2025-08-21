/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.session;

import android.app.PendingIntent;
import android.os.Bundle;
import androidx.annotation.GuardedBy;
import androidx.annotation.Nullable;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.List;

/** A proxy class for {@link MediaBrowser.Listener}. */
public final class TestMediaBrowserListener implements MediaBrowser.Listener {

  private final MediaController.Listener delegate;

  @GuardedBy("this")
  @Nullable
  private Runnable onCustomCommandRunnable;

  public TestMediaBrowserListener(@Nullable MediaController.Listener delegate) {
    this.delegate = delegate == null ? new MediaBrowser.Listener() {} : delegate;
  }

  @Override
  public void onDisconnected(MediaController controller) {
    delegate.onDisconnected(controller);
  }

  @Override
  public ListenableFuture<SessionResult> onCustomCommand(
      MediaController controller, SessionCommand command, Bundle args) {
    synchronized (this) {
      if (onCustomCommandRunnable != null) {
        onCustomCommandRunnable.run();
      }
    }
    return delegate.onCustomCommand(controller, command, args);
  }

  @Override
  public ListenableFuture<SessionResult> onSetCustomLayout(
      MediaController controller, List<CommandButton> layout) {
    return delegate.onSetCustomLayout(controller, layout);
  }

  @Override
  public void onCustomLayoutChanged(MediaController controller, List<CommandButton> layout) {
    delegate.onCustomLayoutChanged(controller, layout);
  }

  @Override
  public void onMediaButtonPreferencesChanged(
      MediaController controller, List<CommandButton> mediaButtonPreferences) {
    delegate.onMediaButtonPreferencesChanged(controller, mediaButtonPreferences);
  }

  @Override
  public void onExtrasChanged(MediaController controller, Bundle extras) {
    delegate.onExtrasChanged(controller, extras);
  }

  @Override
  public void onError(MediaController controller, SessionError sessionError) {
    delegate.onError(controller, sessionError);
  }

  @Override
  public void onSessionActivityChanged(
      MediaController controller, @Nullable PendingIntent sessionActivity) {
    delegate.onSessionActivityChanged(controller, sessionActivity);
  }

  @Override
  public void onAvailableSessionCommandsChanged(
      MediaController controller, SessionCommands commands) {
    delegate.onAvailableSessionCommandsChanged(controller, commands);
  }

  @Override
  public void onChildrenChanged(
      MediaBrowser browser,
      String parentId,
      int itemCount,
      @Nullable MediaLibraryService.LibraryParams params) {
    ((MediaBrowser.Listener) delegate).onChildrenChanged(browser, parentId, itemCount, params);
  }

  @Override
  public void onSearchResultChanged(
      MediaBrowser browser,
      String query,
      int itemCount,
      @Nullable MediaLibraryService.LibraryParams params) {
    ((MediaBrowser.Listener) delegate).onSearchResultChanged(browser, query, itemCount, params);
  }

  public void setRunnableForOnCustomCommand(Runnable runnable) {
    synchronized (this) {
      onCustomCommandRunnable = runnable;
    }
  }
}
