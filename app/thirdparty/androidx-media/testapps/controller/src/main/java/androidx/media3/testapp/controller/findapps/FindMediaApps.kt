/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.testapp.controller.findapps

import androidx.media3.testapp.controller.MediaAppDetails

/** Base class that fetches a list of media apps. */
abstract class FindMediaApps(private val callback: AppListUpdatedCallback) {

    /** Callback used by [FindMediaApps]. */
    interface AppListUpdatedCallback {
        fun onAppListUpdated(mediaAppEntries: List<MediaAppDetails>)
    }

    protected abstract val mediaApps: List<MediaAppDetails>

    fun execute() {
        callback.onAppListUpdated(mediaApps)
    }
}
