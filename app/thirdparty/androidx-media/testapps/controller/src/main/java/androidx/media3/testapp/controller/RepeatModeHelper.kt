/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.testapp.controller

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Spinner
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.media3.common.Player
import androidx.media3.session.MediaController

/** Helper class which handles repeat mode changes and the UI surrounding this feature. */
class RepeatModeHelper(activity: Activity, mediaController: MediaController) {
    private val container: ViewGroup = activity.findViewById(R.id.group_toggle_repeat)
    private val spinner: Spinner = container.findViewById(R.id.repeat_mode_spinner)
    private val icon: ImageView = container.findViewById(R.id.repeat_mode_icon)

    // LINT.IfChange
    private val modes: List<Int> =
        listOf(Player.REPEAT_MODE_OFF, Player.REPEAT_MODE_ONE, Player.REPEAT_MODE_ALL)
    // LINT.ThenChange(../../../../../res/values/options.xml)

    init {
        spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    mediaController.repeatMode = modes[p2]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        val listener: Player.Listener =
            object : Player.Listener {
                override fun onRepeatModeChanged(repeatMode: Int) {
                    spinner.setSelection(repeatMode)
                    updateColor(repeatMode)
                }

                override fun onAvailableCommandsChanged(availableCommands: Player.Commands) =
                    updateBackground(availableCommands.contains(Player.COMMAND_SET_REPEAT_MODE))
            }
        mediaController.addListener(listener)

        val isSupported: Boolean =
            mediaController.availableCommands.contains(Player.COMMAND_SET_REPEAT_MODE)
        updateBackground(isSupported)
    }

    fun updateBackground(isSupported: Boolean) {
        if (isSupported) {
            container.background = null
            spinner.visibility = View.VISIBLE
        } else {
            container.setBackgroundResource(R.drawable.bg_unsupported_action)
            spinner.visibility = View.GONE
        }
    }

    fun updateColor(mode: Int) {
        val tint: Int =
            if (mode == Player.REPEAT_MODE_ONE || mode == Player.REPEAT_MODE_ALL) {
                R.color.colorPrimary
            } else {
                R.color.colorInactive
            }
        DrawableCompat.setTint(icon.drawable, ContextCompat.getColor(container.context, tint))
    }
}
