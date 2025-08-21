/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.session;

import static com.google.common.truth.Truth.assertThat;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.media3.common.Player;
import androidx.media3.test.utils.TestExoPlayerBuilder;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ServiceController;

@RunWith(AndroidJUnit4.class)
public class ConnectionResultTest {

  @Test
  public void acceptedResultBuilder_builtWidthMediaSession_correctDefaults() {
    Context context = ApplicationProvider.getApplicationContext();
    MediaSession mediaSession =
        new MediaSession.Builder(context, new TestExoPlayerBuilder(context).build()).build();

    MediaSession.ConnectionResult connectionResult =
        new MediaSession.ConnectionResult.AcceptedResultBuilder(mediaSession).build();

    assertThat(connectionResult.availableSessionCommands)
        .isEqualTo(MediaSession.ConnectionResult.DEFAULT_SESSION_COMMANDS);
    assertThat(connectionResult.availablePlayerCommands)
        .isEqualTo(MediaSession.ConnectionResult.DEFAULT_PLAYER_COMMANDS);
    assertThat(connectionResult.customLayout).isNull();
    assertThat(connectionResult.isAccepted).isTrue();

    mediaSession.getPlayer().release();
    mediaSession.release();
  }

  @Test
  public void acceptedResultBuilder_builtWidthMediaSession_correctlyOverridden() {
    Context context = ApplicationProvider.getApplicationContext();
    MediaSession mediaSession =
        new MediaSession.Builder(context, new TestExoPlayerBuilder(context).build()).build();

    MediaSession.ConnectionResult connectionResult =
        new MediaSession.ConnectionResult.AcceptedResultBuilder(mediaSession)
            .setAvailableSessionCommands(SessionCommands.EMPTY)
            .setAvailablePlayerCommands(Player.Commands.EMPTY)
            .setCustomLayout(ImmutableList.of())
            .build();

    assertThat(connectionResult.availableSessionCommands.commands).isEmpty();
    assertThat(connectionResult.availablePlayerCommands.size()).isEqualTo(0);
    assertThat(connectionResult.customLayout).isEmpty();
    assertThat(connectionResult.isAccepted).isTrue();

    mediaSession.getPlayer().release();
    mediaSession.release();
  }

  @Test
  public void
      acceptedResultBuilder_builtWidthMediaLibrarySession_correctDefaultLibrarySessionCommands() {
    Context context = ApplicationProvider.getApplicationContext();
    ServiceController<TestService> serviceController = Robolectric.buildService(TestService.class);
    TestService service = serviceController.create().get();
    MediaSession mediaLibrarySession =
        new MediaLibraryService.MediaLibrarySession.Builder(
                service,
                new TestExoPlayerBuilder(context).build(),
                new MediaLibraryService.MediaLibrarySession.Callback() {})
            .build();

    MediaSession.ConnectionResult connectionResult =
        new MediaSession.ConnectionResult.AcceptedResultBuilder(mediaLibrarySession).build();

    assertThat(connectionResult.availableSessionCommands)
        .isEqualTo(MediaSession.ConnectionResult.DEFAULT_SESSION_AND_LIBRARY_COMMANDS);
    assertThat(connectionResult.availablePlayerCommands)
        .isEqualTo(MediaSession.ConnectionResult.DEFAULT_PLAYER_COMMANDS);
    assertThat(connectionResult.customLayout).isNull();
    assertThat(connectionResult.isAccepted).isTrue();
    mediaLibrarySession.getPlayer().release();
    mediaLibrarySession.release();
    serviceController.destroy();
  }

  @Test
  public void accept() {
    SessionCommands sessionCommands =
        new SessionCommands.Builder().add(SessionCommand.COMMAND_CODE_LIBRARY_GET_ITEM).build();
    Player.Commands playerCommands =
        new Player.Commands.Builder().add(Player.COMMAND_PLAY_PAUSE).build();

    MediaSession.ConnectionResult connectionResult =
        MediaSession.ConnectionResult.accept(sessionCommands, playerCommands);

    assertThat(connectionResult.availableSessionCommands).isEqualTo(sessionCommands);
    assertThat(connectionResult.availablePlayerCommands).isEqualTo(playerCommands);
    assertThat(connectionResult.customLayout).isNull();
    assertThat(connectionResult.isAccepted).isTrue();
  }

  @Test
  public void reject() {
    MediaSession.ConnectionResult connectionResult = MediaSession.ConnectionResult.reject();

    assertThat(connectionResult.availableSessionCommands.commands).isEmpty();
    assertThat(connectionResult.availablePlayerCommands.size()).isEqualTo(0);
    assertThat(connectionResult.customLayout).isEmpty();
    assertThat(connectionResult.isAccepted).isFalse();
  }

  private static final class TestService extends MediaLibraryService {
    @Nullable
    @Override
    public MediaLibrarySession onGetSession(MediaSession.ControllerInfo controllerInfo) {
      return null;
    }
  }
}
