/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.testapp.controller

import android.app.Activity
import android.media.AudioManager
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.media.AudioFocusRequestCompat
import androidx.media.AudioManagerCompat

/** Helper class to manage audio focus requests and the UI surrounding this feature. */
class AudioFocusHelper(activity: Activity) :
    View.OnClickListener,
    AudioManager.OnAudioFocusChangeListener,
    AdapterView.OnItemSelectedListener {
    private val audioManager: AudioManager =
        activity.getSystemService(AppCompatActivity.AUDIO_SERVICE) as AudioManager
    private val toggleButton: ToggleButton = activity.findViewById(R.id.audio_focus_button)
    private val focusTypeSpinner: Spinner = activity.findViewById(R.id.audio_focus_type)

    private val selectedFocusType: Int
        get() = FOCUS_TYPES[focusTypeSpinner.selectedItemPosition]

    companion object {
        // LINT.IfChange
        private val FOCUS_TYPES =
            intArrayOf(
                AudioManager.AUDIOFOCUS_GAIN,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK
            )
        // LINT.ThenChange(../../../../../res/values/options.xml)
    }

    init {
        toggleButton.setOnClickListener(this)
        this.focusTypeSpinner.onItemSelectedListener = this
    }

    override fun onClick(v: View) =
        if (toggleButton.isChecked) {
            gainAudioFocus()
        } else {
            abandonAudioFocus()
        }

    override fun onAudioFocusChange(focusChange: Int) =
        when (focusChange) {
            AudioManager.AUDIOFOCUS_GAIN,
            AudioManager.AUDIOFOCUS_GAIN_TRANSIENT,
            AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK -> toggleButton.isChecked = true

            else -> toggleButton.isChecked = false
        }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        // If we're holding audio focus and the type should change, automatically
        // request the new type of focus.
        if (toggleButton.isChecked) {
            gainAudioFocus()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // Nothing to do.
    }

    private fun gainAudioFocus() {
        val audioFocusRequest: AudioFocusRequestCompat =
            AudioFocusRequestCompat.Builder(selectedFocusType).setOnAudioFocusChangeListener(this)
                .build()
        AudioManagerCompat.requestAudioFocus(audioManager, audioFocusRequest)
    }

    private fun abandonAudioFocus() {
        val audioFocusRequest: AudioFocusRequestCompat =
            AudioFocusRequestCompat.Builder(selectedFocusType).setOnAudioFocusChangeListener(this)
                .build()
        AudioManagerCompat.abandonAudioFocusRequest(audioManager, audioFocusRequest)
    }
}
