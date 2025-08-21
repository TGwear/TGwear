/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.ui.compose

import android.view.SurfaceView
import android.view.TextureView
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.media3.common.ForwardingPlayer
import androidx.media3.common.Player
import androidx.media3.ui.compose.utils.TestPlayer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.inOrder
import org.mockito.Mockito.spy

/** Unit test for [PlayerSurface]. */
@RunWith(AndroidJUnit4::class)
class PlayerSurfaceTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun playerSurface_withSurfaceViewType_setsSurfaceViewOnPlayer() {
        val player = TestPlayer()

        composeTestRule.setContent {
            PlayerSurface(player = player, surfaceType = SURFACE_TYPE_SURFACE_VIEW)
        }
        composeTestRule.waitForIdle()

        assertThat(player.videoOutput).isInstanceOf(SurfaceView::class.java)
    }

    @Test
    fun playerSurface_withTextureViewType_setsTextureViewOnPlayer() {
        val player = TestPlayer()

        composeTestRule.setContent {
            PlayerSurface(player = player, surfaceType = SURFACE_TYPE_TEXTURE_VIEW)
        }
        composeTestRule.waitForIdle()

        assertThat(player.videoOutput).isInstanceOf(TextureView::class.java)
    }

    @Test
    fun playerSurface_withoutSupportedCommand_doesNotSetSurfaceOnPlayer() {
        val player = TestPlayer()
        player.removeCommands(Player.COMMAND_SET_VIDEO_SURFACE)

        composeTestRule.setContent {
            PlayerSurface(player = player, surfaceType = SURFACE_TYPE_TEXTURE_VIEW)
        }
        composeTestRule.waitForIdle()

        assertThat(player.videoOutput).isNull()
    }

    @Test
    fun playerSurface_withUpdateSurfaceType_setsNewSurfaceOnPlayer() {
        val player = TestPlayer()

        lateinit var surfaceType: MutableIntState
        composeTestRule.setContent {
            surfaceType = remember { mutableIntStateOf(SURFACE_TYPE_TEXTURE_VIEW) }
            PlayerSurface(player = player, surfaceType = surfaceType.intValue)
        }
        composeTestRule.waitForIdle()
        surfaceType.intValue = SURFACE_TYPE_SURFACE_VIEW
        composeTestRule.waitForIdle()

        assertThat(player.videoOutput).isInstanceOf(SurfaceView::class.java)
    }

    @Test
    fun playerSurface_withNewPlayer_unsetsSurfaceOnOldPlayerFirst() {
        val player0 = TestPlayer()
        val player1 = TestPlayer()
        val spyPlayer0 = spy(ForwardingPlayer(player0))
        val spyPlayer1 = spy(ForwardingPlayer(player1))

        lateinit var playerIndex: MutableIntState
        composeTestRule.setContent {
            playerIndex = remember { mutableIntStateOf(0) }
            PlayerSurface(
                player = if (playerIndex.intValue == 0) spyPlayer0 else spyPlayer1,
                surfaceType = SURFACE_TYPE_SURFACE_VIEW,
            )
        }
        composeTestRule.waitForIdle()
        playerIndex.intValue = 1
        composeTestRule.waitForIdle()

        assertThat(player0.videoOutput).isNull()
        assertThat(player1.videoOutput).isNotNull()
        val inOrder = inOrder(spyPlayer0, spyPlayer1)
        inOrder.verify(spyPlayer0).clearVideoSurfaceView(any())
        inOrder.verify(spyPlayer1).setVideoSurfaceView(any())
    }
}
