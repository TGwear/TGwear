/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package androidx.media3.ui.compose.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.media3.common.Player
import androidx.media3.common.listen
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util.handlePlayPauseButtonAction
import androidx.media3.common.util.Util.shouldEnablePlayPauseButton
import androidx.media3.common.util.Util.shouldShowPlayButton

/**
 * Remembers the value of [PlayPauseButtonState] created based on the passed [Player] and launch a
 * coroutine to listen to [Player's][Player] changes. If the [Player] instance changes between
 * compositions, produce and remember a new value.
 */
@UnstableApi
@Composable
fun rememberPlayPauseButtonState(player: Player): PlayPauseButtonState {
    val playPauseButtonState = remember(player) { PlayPauseButtonState(player) }
    LaunchedEffect(player) { playPauseButtonState.observe() }
    return playPauseButtonState
}

/**
 * State that converts the necessary information from the [Player] to correctly deal with a UI
 * component representing a PlayPause button.
 *
 * @property[isEnabled] determined by `isCommandAvailable(Player.COMMAND_PLAY_PAUSE)` and having
 *   something in the [Timeline][androidx.media3.common.Timeline] to play
 * @property[showPlay] determined by [shouldShowPlayButton]
 */
@UnstableApi
class PlayPauseButtonState(private val player: Player) {
    var isEnabled by mutableStateOf(shouldEnablePlayPauseButton(player))
        private set

    var showPlay by mutableStateOf(shouldShowPlayButton(player))
        private set

    /**
     * Handles the interaction with the PlayPause button according to the current state of the
     * [Player].
     *
     * The [Player] update that follows can take a form of [Player.play], [Player.pause],
     * [Player.prepare] or [Player.seekToDefaultPosition].
     *
     * @see [androidx.media3.common.util.Util.handlePlayButtonAction]
     * @see [androidx.media3.common.util.Util.handlePauseButtonAction]
     * @see [androidx.media3.common.util.Util.shouldShowPlayButton]
     */
    fun onClick() {
        handlePlayPauseButtonAction(player)
    }

    /**
     * Subscribes to updates from [Player.Events] and listens to
     * * [Player.EVENT_PLAYBACK_STATE_CHANGED] and [Player.EVENT_PLAY_WHEN_READY_CHANGED] in order to
     *   determine whether a play or a pause button should be presented on a UI element for playback
     *   control.
     * * [Player.EVENT_AVAILABLE_COMMANDS_CHANGED] in order to determine whether the button should be
     *   enabled, i.e. respond to user input.
     */
    suspend fun observe(): Nothing =
        player.listen { events ->
            if (
                events.containsAny(
                    Player.EVENT_PLAYBACK_STATE_CHANGED,
                    Player.EVENT_PLAY_WHEN_READY_CHANGED,
                    Player.EVENT_AVAILABLE_COMMANDS_CHANGED,
                )
            ) {
                showPlay = shouldShowPlayButton(this)
                isEnabled = shouldEnablePlayPauseButton(this)
            }
        }
}
