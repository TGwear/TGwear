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

/**
 * Remember the value of [ShuffleButtonState] created based on the passed [Player] and launch a
 * coroutine to listen to [Player's][Player] changes. If the [Player] instance changes between
 * compositions, produce and remember a new value.
 */
@UnstableApi
@Composable
fun rememberShuffleButtonState(player: Player): ShuffleButtonState {
    val shuffleButtonState = remember(player) { ShuffleButtonState(player) }
    LaunchedEffect(player) { shuffleButtonState.observe() }
    return shuffleButtonState
}

/**
 * State that holds all interactions to correctly deal with a UI component representing a Shuffle
 * On/Off button.
 *
 * @property[isEnabled] determined by `isCommandAvailable(Player.COMMAND_SET_SHUFFLE_MODE)`
 * @property[shuffleOn] determined by [Player's][Player] `shuffleModeEnabled`
 */
@UnstableApi
class ShuffleButtonState(private val player: Player) {
    var isEnabled by mutableStateOf(player.isCommandAvailable(Player.COMMAND_SET_SHUFFLE_MODE))
        private set

    var shuffleOn by mutableStateOf(player.shuffleModeEnabled)
        private set

    fun onClick() {
        player.shuffleModeEnabled = !player.shuffleModeEnabled
    }

    /**
     * Subscribes to updates from [Player.Events] and listens to
     * * [Player.EVENT_SHUFFLE_MODE_ENABLED_CHANGED] in order to determine the latest
     *   [Player.getShuffleModeEnabled].
     * * [Player.EVENT_AVAILABLE_COMMANDS_CHANGED] in order to determine whether the button should be
     *   enabled, i.e. respond to user input.
     */
    suspend fun observe(): Nothing =
        player.listen { events ->
            if (
                events.containsAny(
                    Player.EVENT_SHUFFLE_MODE_ENABLED_CHANGED,
                    Player.EVENT_AVAILABLE_COMMANDS_CHANGED,
                )
            ) {
                shuffleOn = shuffleModeEnabled
                isEnabled = isCommandAvailable(Player.COMMAND_SET_SHUFFLE_MODE)
            }
        }
}
