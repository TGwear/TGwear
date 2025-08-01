/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package androidx.media3.ui.compose

import android.content.Context
import android.view.SurfaceView
import android.view.TextureView
import android.view.View
import androidx.annotation.IntDef
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi

/**
 * Provides a dedicated drawing [Surface] for media playbacks using a [Player].
 *
 * The player's video output is displayed with either a [android.view.SurfaceView] or a
 * [android.view.TextureView].
 *
 * [Player] takes care of attaching the rendered output to the [Surface] and clearing it, when it is
 * destroyed.
 *
 * See
 * [Choosing a surface type](https://developer.android.com/media/media3/ui/playerview#surfacetype)
 * for more information.
 */
@UnstableApi
@Composable
fun PlayerSurface(
    player: Player,
    modifier: Modifier = Modifier,
    surfaceType: @SurfaceType Int = SURFACE_TYPE_SURFACE_VIEW,
) {
    when (surfaceType) {
        SURFACE_TYPE_SURFACE_VIEW ->
            PlayerSurfaceInternal(
                player,
                modifier,
                createView = { SurfaceView(it) },
                setViewOnPlayer = { player, view -> player.setVideoSurfaceView(view) },
                clearViewFromPlayer = { player, view -> player.clearVideoSurfaceView(view) },
            )

        SURFACE_TYPE_TEXTURE_VIEW ->
            PlayerSurfaceInternal(
                player,
                modifier,
                createView = { TextureView(it) },
                setViewOnPlayer = { player, view -> player.setVideoTextureView(view) },
                clearViewFromPlayer = { player, view -> player.clearVideoTextureView(view) },
            )

        else -> throw IllegalArgumentException("Unrecognized surface type: $surfaceType")
    }
}

@Composable
private fun <T : View> PlayerSurfaceInternal(
    player: Player,
    modifier: Modifier,
    createView: (Context) -> T,
    setViewOnPlayer: (Player, T) -> Unit,
    clearViewFromPlayer: (Player, T) -> Unit,
) {
    var view by remember { mutableStateOf<T?>(null) }
    var registeredPlayer by remember { mutableStateOf<Player?>(null) }
    AndroidView(
        factory = { createView(it).apply { view = this } },
        onReset = {},
        modifier = modifier
    )
    view?.let { view ->
        LaunchedEffect(view, player) {
            registeredPlayer?.let { previousPlayer ->
                if (previousPlayer.isCommandAvailable(Player.COMMAND_SET_VIDEO_SURFACE))
                    clearViewFromPlayer(previousPlayer, view)
                registeredPlayer = null
            }
            if (player.isCommandAvailable(Player.COMMAND_SET_VIDEO_SURFACE)) {
                setViewOnPlayer(player, view)
                registeredPlayer = player
            }
        }
    }
}

/**
 * The type of surface used for media playbacks. One of [SURFACE_TYPE_SURFACE_VIEW] or
 * [SURFACE_TYPE_TEXTURE_VIEW].
 */
@UnstableApi
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE, AnnotationTarget.TYPE_PARAMETER)
@IntDef(SURFACE_TYPE_SURFACE_VIEW, SURFACE_TYPE_TEXTURE_VIEW)
annotation class SurfaceType

/** Surface type to create [android.view.SurfaceView]. */
@UnstableApi
const val SURFACE_TYPE_SURFACE_VIEW = 1

/** Surface type to create [android.view.TextureView]. */
@UnstableApi
const val SURFACE_TYPE_TEXTURE_VIEW = 2
