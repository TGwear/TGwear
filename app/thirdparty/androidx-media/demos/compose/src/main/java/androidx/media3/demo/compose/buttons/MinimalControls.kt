/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package androidx.media3.demo.compose.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player

/**
 * Minimal playback controls for a [Player].
 *
 * Includes buttons for seeking to a previous/next items or playing/pausing the playback.
 */
@Composable
internal fun MinimalControls(player: Player, modifier: Modifier = Modifier) {
    val graySemiTransparentBackground = Color.Gray.copy(alpha = 0.1f)
    val modifierForIconButton =
        modifier.size(80.dp).background(graySemiTransparentBackground, CircleShape)
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PreviousButton(player, modifierForIconButton)
        PlayPauseButton(player, modifierForIconButton)
        NextButton(player, modifierForIconButton)
    }
}
