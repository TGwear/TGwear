/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package androidx.media3.demo.session

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.PendingIntent.getActivity
import android.content.Intent
import android.os.Build
import androidx.core.app.TaskStackBuilder

class PlaybackService : DemoPlaybackService() {

    companion object {
        private val immutableFlag =
            if (Build.VERSION.SDK_INT >= 23) PendingIntent.FLAG_IMMUTABLE else 0
    }

    override fun getSingleTopActivity(): PendingIntent? {
        return getActivity(
            this,
            0,
            Intent(this, PlayerActivity::class.java),
            immutableFlag or FLAG_UPDATE_CURRENT
        )
    }

    override fun getBackStackedActivity(): PendingIntent? {
        return TaskStackBuilder.create(this).run {
            addNextIntent(Intent(this@PlaybackService, MainActivity::class.java))
            addNextIntent(Intent(this@PlaybackService, PlayerActivity::class.java))
            getPendingIntent(0, immutableFlag or FLAG_UPDATE_CURRENT)
        }
    }
}
