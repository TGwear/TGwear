/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package androidx.media3.ui.compose.state

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.media3.common.Player
import androidx.media3.ui.compose.utils.TestPlayer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Unit test for [NextButtonState]. */
@RunWith(AndroidJUnit4::class)
class NextButtonStateTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun addSeekNextCommandToPlayer_buttonStateTogglesFromDisabledToEnabled() {
        val player = TestPlayer()
        player.playbackState = Player.STATE_READY
        player.playWhenReady = true
        player.removeCommands(Player.COMMAND_SEEK_TO_NEXT)

        lateinit var state: NextButtonState
        composeTestRule.setContent { state = rememberNextButtonState(player = player) }

        assertThat(state.isEnabled).isFalse()

        player.addCommands(Player.COMMAND_SEEK_TO_NEXT)
        composeTestRule.waitForIdle()

        assertThat(state.isEnabled).isTrue()
    }

    @Test
    fun removeSeekNextCommandToPlayer_buttonStateTogglesFromEnabledToDisabled() {
        val player = TestPlayer()
        player.playbackState = Player.STATE_READY
        player.playWhenReady = true

        lateinit var state: NextButtonState
        composeTestRule.setContent { state = rememberNextButtonState(player = player) }

        assertThat(state.isEnabled).isTrue()

        player.removeCommands(Player.COMMAND_SEEK_TO_NEXT)
        composeTestRule.waitForIdle()

        assertThat(state.isEnabled).isFalse()
    }

    @Test
    fun clickNextOnPenultimateMediaItem_buttonStateTogglesFromEnabledToDisabled() {
        val player = TestPlayer()
        player.playbackState = Player.STATE_READY
        player.playWhenReady = true

        lateinit var state: NextButtonState
        composeTestRule.setContent { state = rememberNextButtonState(player = player) }

        assertThat(state.isEnabled).isTrue()

        player.seekToNext()
        composeTestRule.waitForIdle()

        assertThat(state.isEnabled).isFalse()
    }

    @Test
    fun playerInReadyState_buttonClicked_nextItemPlaying() {
        val player = TestPlayer()
        player.playbackState = Player.STATE_READY
        player.playWhenReady = true
        val state = NextButtonState(player)

        assertThat(player.currentMediaItemIndex).isEqualTo(0)

        state.onClick()

        assertThat(player.currentMediaItemIndex).isEqualTo(1)
    }
}
