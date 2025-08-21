/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.demo.shortform

import androidx.media3.common.MediaItem

class MediaItemDatabase {

    private val mediaUris =
        mutableListOf(
            "https://storage.googleapis.com/exoplayer-test-media-0/shortform_1.mp4",
            "https://storage.googleapis.com/exoplayer-test-media-0/shortform_2.mp4",
            "https://storage.googleapis.com/exoplayer-test-media-0/shortform_3.mp4",
            "https://storage.googleapis.com/exoplayer-test-media-0/shortform_4.mp4",
            "https://storage.googleapis.com/exoplayer-test-media-0/shortform_6.mp4",
        )

    fun get(index: Int): MediaItem {
        val uri = mediaUris.get(index.mod(mediaUris.size))
        return MediaItem.Builder().setUri(uri).setMediaId(index.toString()).build()
    }
}
