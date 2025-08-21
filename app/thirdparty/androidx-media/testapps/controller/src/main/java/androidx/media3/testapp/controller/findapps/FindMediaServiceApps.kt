/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.testapp.controller.findapps

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import androidx.media3.session.SessionToken
import androidx.media3.testapp.controller.MediaAppDetails

/**
 * Implementation of [FindMediaApps] that uses [MediaSessionManager] to populate the list of media
 * service apps.
 */
class FindMediaServiceApps
constructor(
    private val context: Context,
    private val packageManager: PackageManager,
    private val resources: Resources,
    callback: AppListUpdatedCallback
) : FindMediaApps(callback) {

    override val mediaApps: List<MediaAppDetails>
        get() {
            return getMediaAppsFromSessionTokens(
                SessionToken.getAllServiceTokens(context),
                packageManager,
                resources
            )
        }

    private fun getMediaAppsFromSessionTokens(
        sessionTokens: Set<SessionToken>,
        packageManager: PackageManager,
        resources: Resources
    ): List<MediaAppDetails> {
        return sessionTokens.map {
            MediaAppDetails.create(packageManager, resources, sessionToken = it)
        }
    }
}
