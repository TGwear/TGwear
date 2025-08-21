/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.ui;

import static androidx.media3.common.Player.COMMAND_GET_METADATA;

import android.app.PendingIntent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import androidx.annotation.Nullable;
import androidx.media3.common.Player;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.ui.PlayerNotificationManager.BitmapCallback;
import androidx.media3.ui.PlayerNotificationManager.MediaDescriptionAdapter;

/**
 * Default implementation of {@link MediaDescriptionAdapter}.
 *
 * <p>Uses values from the {@link Player#getMediaMetadata() player mediaMetadata} to populate the
 * notification.
 */
@UnstableApi
public final class DefaultMediaDescriptionAdapter implements MediaDescriptionAdapter {

  @Nullable private final PendingIntent pendingIntent;

  /**
   * Creates a default {@link MediaDescriptionAdapter}.
   *
   * @param pendingIntent The {@link PendingIntent} to be returned from {@link
   *     #createCurrentContentIntent(Player)}, or null if no intent should be fired.
   */
  public DefaultMediaDescriptionAdapter(@Nullable PendingIntent pendingIntent) {
    this.pendingIntent = pendingIntent;
  }

  @Override
  public CharSequence getCurrentContentTitle(Player player) {
    if (!player.isCommandAvailable(COMMAND_GET_METADATA)) {
      return "";
    }
    @Nullable CharSequence displayTitle = player.getMediaMetadata().displayTitle;
    if (!TextUtils.isEmpty(displayTitle)) {
      return displayTitle;
    }

    @Nullable CharSequence title = player.getMediaMetadata().title;
    return title != null ? title : "";
  }

  @Nullable
  @Override
  public PendingIntent createCurrentContentIntent(Player player) {
    return pendingIntent;
  }

  @Nullable
  @Override
  public CharSequence getCurrentContentText(Player player) {
    if (!player.isCommandAvailable(COMMAND_GET_METADATA)) {
      return null;
    }
    @Nullable CharSequence artist = player.getMediaMetadata().artist;
    if (!TextUtils.isEmpty(artist)) {
      return artist;
    }

    return player.getMediaMetadata().albumArtist;
  }

  @Nullable
  @Override
  public Bitmap getCurrentLargeIcon(Player player, BitmapCallback callback) {
    if (!player.isCommandAvailable(COMMAND_GET_METADATA)) {
      return null;
    }
    @Nullable byte[] data = player.getMediaMetadata().artworkData;
    if (data == null) {
      return null;
    }
    return BitmapFactory.decodeByteArray(data, /* offset= */ 0, data.length);
  }
}
