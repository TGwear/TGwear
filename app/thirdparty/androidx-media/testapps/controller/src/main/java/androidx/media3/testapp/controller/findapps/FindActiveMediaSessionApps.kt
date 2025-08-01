/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.testapp.controller.findapps

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.media.session.MediaController
import android.media.session.MediaSessionManager
import androidx.media3.testapp.controller.MediaAppDetails

/**
 * Implementation of [FindMediaApps] that uses [MediaSessionManager] to populate the list of active
 * media session apps.
 */
class FindActiveMediaSessionApps
constructor(
    private val mediaSessionManager: MediaSessionManager,
    private val componentName: ComponentName,
    private val packageManager: PackageManager,
    private val resources: Resources,
    private val context: Context,
    callback: AppListUpdatedCallback
) : FindMediaApps(callback) {
    override val mediaApps: List<MediaAppDetails>
        get() {
            return getMediaAppsFromMediaControllers(
                mediaSessionManager.getActiveSessions(componentName),
                packageManager,
                resources
            )
        }

    private fun getMediaAppsFromMediaControllers(
        sessionTokens: List<MediaController>,
        packageManager: PackageManager,
        resources: Resources
    ): List<MediaAppDetails> {
        return sessionTokens.map {
            MediaAppDetails.create(packageManager, resources, controller = it, context)
        }
    }
}
