/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.session;

import static androidx.media3.test.utils.TestUtil.getThrowingBundle;

import android.content.Context;
import android.os.Bundle;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.test.utils.TestExoPlayerBuilder;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Unit test for {@link MediaControllerStub}. */
@RunWith(AndroidJUnit4.class)
public class MediaControllerStubTest {

  @Test
  public void invalidBinderArguments_doNotCrashController() throws Exception {
    // Access controller stub directly and then send invalid arguments. None of them should crash
    // the controller and this test asserts this by running through without throwing an exception.
    Context context = ApplicationProvider.getApplicationContext();
    ExoPlayer player = new TestExoPlayerBuilder(context).build();
    MediaSession session = new MediaSession.Builder(context, player).build();
    MediaController controller =
        new MediaController.Builder(context, session.getToken()).buildAsync().get();
    IMediaController binder = controller.getBinder();

    // Call methods with non-primitive parameters set to null.
    binder.onConnected(/* seq= */ 0, /* connectionResult= */ null);
    binder.onSessionResult(/* seq= */ 0, /* sessionResult= */ null);
    binder.onLibraryResult(/* seq= */ 0, /* libraryResult= */ null);
    binder.onSetCustomLayout(/* seq= */ 0, /* commandButtonList= */ null);
    binder.onCustomCommand(
        /* seq= */ 0,
        /* command= */ new SessionCommand(SessionCommand.COMMAND_CODE_LIBRARY_SEARCH).toBundle(),
        /* args= */ null);
    binder.onCustomCommand(/* seq= */ 0, /* command= */ null, /* args= */ new Bundle());
    binder.onPlayerInfoChanged(
        /* seq= */ 0, /* playerInfoBundle= */ null, /* isTimelineExcluded= */ false);
    binder.onPlayerInfoChangedWithExclusions(
        /* seq= */ 0,
        /* playerInfoBundle= */ null,
        /* playerInfoExclusions= */ new PlayerInfo.BundlingExclusions(
                /* isTimelineExcluded= */ true, /* areCurrentTracksExcluded= */ false)
            .toBundle());
    binder.onPlayerInfoChangedWithExclusions(
        /* seq= */ 0,
        /* playerInfoBundle= */ PlayerInfo.DEFAULT.toBundleInProcess(),
        /* playerInfoExclusions= */ null);
    binder.onPeriodicSessionPositionInfoChanged(/* seq= */ 0, /* sessionPositionInfo= */ null);
    binder.onAvailableCommandsChangedFromPlayer(/* seq= */ 0, /* commandsBundle= */ null);
    binder.onAvailableCommandsChangedFromSession(
        /* seq= */ 0,
        /* sessionCommandsBundle= */ new SessionCommands.Builder().build().toBundle(),
        /* playerCommandsBundle= */ null);
    binder.onAvailableCommandsChangedFromSession(
        /* seq= */ 0,
        /* sessionCommandsBundle= */ null,
        /* playerCommandsBundle= */ new Player.Commands.Builder().build().toBundle());
    binder.onExtrasChanged(/* seq= */ 0, /* extras= */ null);
    binder.onSessionActivityChanged(/* seq= */ 0, /* pendingIntent= */ null);
    binder.onChildrenChanged(
        /* seq= */ 0,
        /* parentId= */ null,
        /* itemCount= */ 1,
        /* libraryParams= */ new MediaLibraryService.LibraryParams.Builder().build().toBundle());
    binder.onChildrenChanged(
        /* seq= */ 0, /* parentId= */ "", /* itemCount= */ 1, /* libraryParams= */ null);
    binder.onSearchResultChanged(
        /* seq= */ 0,
        /* query= */ null,
        /* itemCount= */ 1,
        /* libraryParams= */ new MediaLibraryService.LibraryParams.Builder().build().toBundle());
    binder.onSearchResultChanged(
        /* seq= */ 0, /* query= */ "", /* itemCount= */ 1, /* libraryParams= */ null);

    // Call methods with non-null arguments, but invalid Bundles.
    binder.onConnected(/* seq= */ 0, /* connectionResult= */ getThrowingBundle());
    binder.onSessionResult(/* seq= */ 0, /* sessionResult= */ getThrowingBundle());
    binder.onLibraryResult(/* seq= */ 0, /* libraryResult= */ getThrowingBundle());
    binder.onSetCustomLayout(
        /* seq= */ 0, /* commandButtonList= */ ImmutableList.of(getThrowingBundle()));
    binder.onCustomCommand(
        /* seq= */ 0,
        /* command= */ new SessionCommand(SessionCommand.COMMAND_CODE_LIBRARY_SEARCH).toBundle(),
        /* args= */ getThrowingBundle());
    binder.onCustomCommand(
        /* seq= */ 0, /* command= */ getThrowingBundle(), /* args= */ new Bundle());
    binder.onPlayerInfoChanged(
        /* seq= */ 0, /* playerInfoBundle= */ getThrowingBundle(), /* isTimelineExcluded= */ false);
    binder.onPlayerInfoChangedWithExclusions(
        /* seq= */ 0,
        /* playerInfoBundle= */ getThrowingBundle(),
        /* playerInfoExclusions= */ new PlayerInfo.BundlingExclusions(
                /* isTimelineExcluded= */ true, /* areCurrentTracksExcluded= */ false)
            .toBundle());
    binder.onPlayerInfoChangedWithExclusions(
        /* seq= */ 0,
        /* playerInfoBundle= */ PlayerInfo.DEFAULT.toBundleInProcess(),
        /* playerInfoExclusions= */ getThrowingBundle());
    binder.onPeriodicSessionPositionInfoChanged(
        /* seq= */ 0, /* sessionPositionInfo= */ getThrowingBundle());
    binder.onAvailableCommandsChangedFromPlayer(
        /* seq= */ 0, /* commandsBundle= */ getThrowingBundle());
    binder.onAvailableCommandsChangedFromSession(
        /* seq= */ 0,
        /* sessionCommandsBundle= */ new SessionCommands.Builder().build().toBundle(),
        /* playerCommandsBundle= */ getThrowingBundle());
    binder.onAvailableCommandsChangedFromSession(
        /* seq= */ 0,
        /* sessionCommandsBundle= */ getThrowingBundle(),
        /* playerCommandsBundle= */ new Player.Commands.Builder().build().toBundle());
    binder.onExtrasChanged(/* seq= */ 0, /* extras= */ getThrowingBundle());
    binder.onChildrenChanged(
        /* seq= */ 0,
        /* parentId= */ "",
        /* itemCount= */ 1,
        /* libraryParams= */ getThrowingBundle());
    binder.onSearchResultChanged(
        /* seq= */ 0,
        /* query= */ "",
        /* itemCount= */ 1,
        /* libraryParams= */ getThrowingBundle());

    session.release();
    player.release();
  }
}
