/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.testapp.controller

import android.app.Activity
import android.widget.ImageButton
import androidx.media3.common.Player
import androidx.media3.session.MediaController

/** Helper class which handles transport controls and the UI surrounding this feature. */
class TransportControlHelper(activity: Activity, mediaController: MediaController) {
    private val buttonCommandList: List<Pair<ImageButton, Int>>

    init {
        val controls =
            listOf(
                Control(
                    { controller: MediaController -> controller.play() },
                    activity.findViewById(R.id.action_resume),
                    Player.COMMAND_PLAY_PAUSE
                ),
                Control(
                    { controller: MediaController -> controller.pause() },
                    activity.findViewById(R.id.action_pause),
                    Player.COMMAND_PLAY_PAUSE
                ),
                Control(
                    { controller: MediaController -> controller.stop() },
                    activity.findViewById(R.id.action_stop),
                    Player.COMMAND_STOP
                ),
                Control(
                    { controller: MediaController -> controller.seekToNext() },
                    activity.findViewById(R.id.action_skip_next),
                    Player.COMMAND_SEEK_TO_NEXT_MEDIA_ITEM
                ),
                Control(
                    { controller: MediaController -> controller.seekToPrevious() },
                    activity.findViewById(R.id.action_skip_previous),
                    Player.COMMAND_SEEK_TO_PREVIOUS_MEDIA_ITEM
                ),
                Control(
                    { controller: MediaController ->
                        val positionMs: Long = controller.currentPosition
                        controller.seekTo(positionMs - 1000 * 30)
                    },
                    activity.findViewById(R.id.action_skip_30s_backward),
                    Player.COMMAND_SEEK_IN_CURRENT_MEDIA_ITEM
                ),
                Control(
                    { controller: MediaController ->
                        val positionMs: Long = controller.currentPosition
                        controller.seekTo(positionMs + 1000 * 30)
                    },
                    activity.findViewById(R.id.action_skip_30s_forward),
                    Player.COMMAND_SEEK_IN_CURRENT_MEDIA_ITEM
                ),
                Control(
                    { controller: MediaController -> controller.seekForward() },
                    activity.findViewById(R.id.action_fast_forward),
                    Player.COMMAND_SEEK_IN_CURRENT_MEDIA_ITEM
                ),
                Control(
                    { controller: MediaController -> controller.seekBack() },
                    activity.findViewById(R.id.action_fast_rewind),
                    Player.COMMAND_SEEK_IN_CURRENT_MEDIA_ITEM
                )
            )

        for (control in controls) {
            control.button.setOnClickListener { control.action(mediaController) }
        }
        buttonCommandList = controls.map { it.button to it.command }.toList()

        updateBackground(mediaController.availableCommands)

        val listener: Player.Listener =
            object : Player.Listener {
                override fun onAvailableCommandsChanged(availableCommands: Player.Commands) =
                    updateBackground(availableCommands)
            }
        mediaController.addListener(listener)
    }

    private class Control(
        val action: (MediaController) -> Unit,
        val button: ImageButton,
        @Player.Command val command: Int
    )

    fun updateBackground(availableCommands: Player.Commands) =
        buttonCommandList.forEach { (button: ImageButton, command: Int) ->
            if (availableCommands.contains(command)) {
                button.background = null
            } else {
                button.setBackgroundResource(R.drawable.bg_unsupported_action)
            }
        }
}
