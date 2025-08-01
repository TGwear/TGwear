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
 * Remembers the value of [PreviousButtonState] created based on the passed [Player] and launch a
 * coroutine to listen to [Player's][Player] changes. If the [Player] instance changes between
 * compositions, produce and remember a new value.
 */
@UnstableApi
@Composable
fun rememberPreviousButtonState(player: Player): PreviousButtonState {
    val previousButtonState = remember(player) { PreviousButtonState(player) }
    LaunchedEffect(player) { previousButtonState.observe() }
    return previousButtonState
}

/**
 * State that holds all interactions to correctly deal with a UI component representing a
 * seek-to-previous button.
 *
 * This button has no internal state to maintain, it can only be enabled or disabled.
 *
 * @property[isEnabled] determined by `isCommandAvailable(Player.COMMAND_SEEK_TO_PREVIOUS)`
 */
@UnstableApi
class PreviousButtonState(private val player: Player) {
    var isEnabled by mutableStateOf(player.isCommandAvailable(Player.COMMAND_SEEK_TO_PREVIOUS))
        private set

    fun onClick() {
        player.seekToPrevious()
    }

    /**
     * Subscribes to updates from [Player.Events] and listens to
     * [Player.EVENT_AVAILABLE_COMMANDS_CHANGED] in order to determine whether the button should be
     * enabled, i.e. respond to user input.
     */
    suspend fun observe(): Nothing =
        player.listen { events ->
            if (events.contains(Player.EVENT_AVAILABLE_COMMANDS_CHANGED)) {
                isEnabled = isCommandAvailable(Player.COMMAND_SEEK_TO_PREVIOUS)
            }
        }
}
