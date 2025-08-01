/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package androidx.media3.demo.compose.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOn
import androidx.compose.material.icons.filled.RepeatOneOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.media3.common.Player
import androidx.media3.demo.compose.R
import androidx.media3.ui.compose.state.rememberRepeatButtonState

@Composable
internal fun RepeatButton(player: Player, modifier: Modifier = Modifier) {
    val state = rememberRepeatButtonState(player)
    val icon = repeatModeIcon(state.repeatModeState)
    val contentDescription = repeatModeContentDescription(state.repeatModeState)
    IconButton(onClick = state::onClick, modifier = modifier, enabled = state.isEnabled) {
        Icon(icon, contentDescription = contentDescription, modifier = modifier)
    }
}

private fun repeatModeIcon(repeatMode: @Player.RepeatMode Int): ImageVector {
    return when (repeatMode) {
        Player.REPEAT_MODE_OFF -> Icons.Default.Repeat
        Player.REPEAT_MODE_ONE -> Icons.Default.RepeatOneOn
        else -> Icons.Default.RepeatOn
    }
}

@Composable
private fun repeatModeContentDescription(repeatMode: @Player.RepeatMode Int): String {
    return when (repeatMode) {
        Player.REPEAT_MODE_OFF -> stringResource(R.string.repeat_button_repeat_off_description)
        Player.REPEAT_MODE_ONE -> stringResource(R.string.repeat_button_repeat_one_description)
        else -> stringResource(R.string.repeat_button_repeat_all_description)
    }
}
