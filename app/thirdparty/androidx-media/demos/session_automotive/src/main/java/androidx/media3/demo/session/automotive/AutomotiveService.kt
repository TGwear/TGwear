/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.demo.session.automotive

import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.demo.session.DemoMediaLibrarySessionCallback
import androidx.media3.demo.session.DemoPlaybackService
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaConstants
import androidx.media3.session.MediaSession.ControllerInfo
import com.google.common.util.concurrent.ListenableFuture

class AutomotiveService : DemoPlaybackService() {

    override fun createLibrarySessionCallback(): MediaLibrarySession.Callback {
        return object : DemoMediaLibrarySessionCallback(this@AutomotiveService) {

            @OptIn(UnstableApi::class)
            override fun onGetLibraryRoot(
                session: MediaLibrarySession,
                browser: ControllerInfo,
                params: LibraryParams?
            ): ListenableFuture<LibraryResult<MediaItem>> {
                var responseParams = params
                if (session.isAutomotiveController(browser)) {
                    // See https://developer.android.com/training/cars/media#apply_content_style
                    val rootHintParams = params ?: LibraryParams.Builder().build()
                    rootHintParams.extras.putInt(
                        MediaConstants.EXTRAS_KEY_CONTENT_STYLE_BROWSABLE,
                        MediaConstants.EXTRAS_VALUE_CONTENT_STYLE_GRID_ITEM
                    )
                    rootHintParams.extras.putInt(
                        MediaConstants.EXTRAS_KEY_CONTENT_STYLE_PLAYABLE,
                        MediaConstants.EXTRAS_VALUE_CONTENT_STYLE_LIST_ITEM
                    )
                    // Tweaked params are propagated to Automotive browsers as root hints.
                    responseParams = rootHintParams
                }
                // Use super to return the common library root with the tweaked params sent to the browser.
                return super.onGetLibraryRoot(session, browser, responseParams)
            }
        }
    }
}
