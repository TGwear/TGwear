/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.testapp.controller

import androidx.media3.common.Player
import androidx.media3.session.SessionCommand

object MediaIntToString {
    val playbackStateMap =
        mapOf(
            Player.STATE_IDLE to "STATE_IDLE",
            Player.STATE_BUFFERING to "STATE_BUFFERING",
            Player.STATE_READY to "STATE_READY",
            Player.STATE_ENDED to "STATE_ENDED"
        )
    val playerCommandMap =
        mapOf(
            Player.COMMAND_INVALID to "COMMAND_INVALID",
            Player.COMMAND_PLAY_PAUSE to "COMMAND_PLAY_PAUSE",
            Player.COMMAND_PREPARE to "COMMAND_PREPARE",
            Player.COMMAND_STOP to "COMMAND_STOP",
            Player.COMMAND_SEEK_TO_DEFAULT_POSITION to "COMMAND_SEEK_TO_DEFAULT_POSITION",
            Player.COMMAND_SEEK_TO_DEFAULT_POSITION to "COMMAND_SEEK_TO_DEFAULT_POSITION",
            Player.COMMAND_SEEK_IN_CURRENT_MEDIA_ITEM to "COMMAND_SEEK_IN_CURRENT_MEDIA_ITEM",
            Player.COMMAND_SEEK_TO_PREVIOUS_MEDIA_ITEM to "COMMAND_SEEK_TO_PREVIOUS_MEDIA_ITEM",
            Player.COMMAND_SEEK_TO_PREVIOUS to "COMMAND_SEEK_TO_PREVIOUS",
            Player.COMMAND_SEEK_TO_NEXT_MEDIA_ITEM to "COMMAND_SEEK_TO_NEXT_MEDIA_ITEM",
            Player.COMMAND_SEEK_TO_NEXT to "COMMAND_SEEK_TO_NEXT",
            Player.COMMAND_SEEK_TO_MEDIA_ITEM to "COMMAND_SEEK_TO_MEDIA_ITEM",
            Player.COMMAND_SEEK_BACK to "COMMAND_SEEK_BACK",
            Player.COMMAND_SEEK_FORWARD to "COMMAND_SEEK_FORWARD",
            Player.COMMAND_SET_SPEED_AND_PITCH to "COMMAND_SET_SPEED_AND_PITCH",
            Player.COMMAND_SET_SHUFFLE_MODE to "COMMAND_SET_SHUFFLE_MODE",
            Player.COMMAND_SET_REPEAT_MODE to "COMMAND_SET_REPEAT_MODE",
            Player.COMMAND_GET_CURRENT_MEDIA_ITEM to "COMMAND_GET_CURRENT_MEDIA_ITEM",
            Player.COMMAND_GET_TIMELINE to "COMMAND_GET_TIMELINE",
            Player.COMMAND_GET_METADATA to "COMMAND_GET_METADATA",
            Player.COMMAND_SET_PLAYLIST_METADATA to "COMMAND_SET_PLAYLIST_METADATA",
            Player.COMMAND_SET_MEDIA_ITEM to "COMMAND_SET_MEDIA_ITEM",
            Player.COMMAND_CHANGE_MEDIA_ITEMS to "COMMAND_CHANGE_MEDIA_ITEMS",
            Player.COMMAND_GET_AUDIO_ATTRIBUTES to "COMMAND_GET_AUDIO_ATTRIBUTES",
            Player.COMMAND_GET_VOLUME to "COMMAND_GET_VOLUME",
            Player.COMMAND_GET_DEVICE_VOLUME to "COMMAND_GET_DEVICE_VOLUME",
            Player.COMMAND_SET_VOLUME to "COMMAND_SET_VOLUME",
            Player.COMMAND_SET_DEVICE_VOLUME to "COMMAND_SET_DEVICE_VOLUME",
            Player.COMMAND_ADJUST_DEVICE_VOLUME to "COMMAND_ADJUST_DEVICE_VOLUME",
            Player.COMMAND_SET_VIDEO_SURFACE to "COMMAND_SET_VIDEO_SURFACE",
            Player.COMMAND_GET_TEXT to "COMMAND_GET_TEXT"
        )
    val sessionCommandMap =
        mapOf(
            SessionCommand.COMMAND_CODE_SESSION_SET_RATING to "COMMAND_SESSION_SET_RATING",
            SessionCommand.COMMAND_CODE_LIBRARY_GET_LIBRARY_ROOT to "COMMAND_LIBRARY_GET_LIBRARY_ROOT",
            SessionCommand.COMMAND_CODE_LIBRARY_SUBSCRIBE to "COMMAND_LIBRARY_SUBSCRIBE",
            SessionCommand.COMMAND_CODE_LIBRARY_UNSUBSCRIBE to "COMMAND_LIBRARY_UNSUBSCRIBE",
            SessionCommand.COMMAND_CODE_LIBRARY_GET_CHILDREN to "COMMAND_LIBRARY_GET_CHILDREN",
            SessionCommand.COMMAND_CODE_LIBRARY_GET_ITEM to "COMMAND_LIBRARY_GET_ITEM",
            SessionCommand.COMMAND_CODE_LIBRARY_SEARCH to "COMMAND_LIBRARY_SEARCH",
            SessionCommand.COMMAND_CODE_LIBRARY_GET_SEARCH_RESULT to "COMMAND_LIBRARY_GET_SEARCH_RESULT"
        )
}
