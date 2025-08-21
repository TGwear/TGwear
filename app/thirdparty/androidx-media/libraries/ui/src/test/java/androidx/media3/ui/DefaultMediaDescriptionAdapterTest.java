/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.ui;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.Player;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Tests for the {@link DefaultMediaDescriptionAdapter}. */
@RunWith(AndroidJUnit4.class)
public class DefaultMediaDescriptionAdapterTest {

  @Test
  public void getters_withGetMetatadataCommandAvailable_returnMediaMetadataValues() {
    Context context = ApplicationProvider.getApplicationContext();
    Player player = mock(Player.class);
    MediaMetadata mediaMetadata =
        new MediaMetadata.Builder().setDisplayTitle("display title").setArtist("artist").build();
    PendingIntent pendingIntent =
        PendingIntent.getActivity(context, 0, new Intent(), PendingIntent.FLAG_IMMUTABLE);
    DefaultMediaDescriptionAdapter adapter = new DefaultMediaDescriptionAdapter(pendingIntent);

    when(player.isCommandAvailable(Player.COMMAND_GET_METADATA)).thenReturn(true);
    when(player.getMediaMetadata()).thenReturn(mediaMetadata);

    assertThat(adapter.createCurrentContentIntent(player)).isEqualTo(pendingIntent);
    assertThat(adapter.getCurrentContentTitle(player).toString())
        .isEqualTo(mediaMetadata.displayTitle.toString());
    assertThat(adapter.getCurrentContentText(player).toString())
        .isEqualTo(mediaMetadata.artist.toString());
  }

  @Test
  public void getters_withoutGetMetatadataCommandAvailable_returnMediaMetadataValues() {
    Context context = ApplicationProvider.getApplicationContext();
    Player player = mock(Player.class);
    MediaMetadata mediaMetadata =
        new MediaMetadata.Builder().setDisplayTitle("display title").setArtist("artist").build();
    PendingIntent pendingIntent =
        PendingIntent.getActivity(context, 0, new Intent(), PendingIntent.FLAG_IMMUTABLE);
    DefaultMediaDescriptionAdapter adapter = new DefaultMediaDescriptionAdapter(pendingIntent);

    when(player.isCommandAvailable(Player.COMMAND_GET_METADATA)).thenReturn(false);
    when(player.getMediaMetadata()).thenReturn(mediaMetadata);

    assertThat(adapter.createCurrentContentIntent(player)).isEqualTo(pendingIntent);
    assertThat(adapter.getCurrentContentTitle(player).toString()).isEqualTo("");
    assertThat(adapter.getCurrentContentText(player)).isNull();
  }
}
