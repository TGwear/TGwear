/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.demo.effect

import android.content.Context
import android.net.Uri
import android.util.JsonReader
import android.util.Log
import androidx.media3.common.MediaItem
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal suspend fun loadPlaylistsFromJson(
    jsonFilename: String,
    context: Context,
    tag: String,
): List<PlaylistHolder> =
    withContext(Dispatchers.IO) {
        try {
            context.assets.open(jsonFilename).use { inputStream ->
                val reader = JsonReader(InputStreamReader(inputStream, StandardCharsets.UTF_8))
                val playlistHolders = buildList {
                    reader.beginArray()
                    while (reader.hasNext()) {
                        readPlaylist(reader)?.let { add(it) }
                    }
                    reader.endArray()
                }
                playlistHolders
            }
        } catch (e: IOException) {
            Log.e(tag, context.getString(R.string.playlist_loading_error, jsonFilename, e))
            emptyList()
        }
    }

private fun readPlaylist(reader: JsonReader): PlaylistHolder? {
    val playlistHolder = PlaylistHolder("", emptyList())
    reader.beginObject()
    while (reader.hasNext()) {
        val name = reader.nextName()
        if (name.equals("name")) {
            playlistHolder.title = reader.nextString()
        } else if (name.equals("playlist")) {
            playlistHolder.mediaItems = buildList {
                reader.beginArray()
                while (reader.hasNext()) {
                    reader.beginObject()
                    reader.nextName()
                    add(MediaItem.fromUri(Uri.parse(reader.nextString())))
                    reader.endObject()
                }
                reader.endArray()
            }
        }
    }
    reader.endObject()
    // Only return the playlistHolder object if it has media items
    return if (playlistHolder.mediaItems.isNotEmpty()) playlistHolder else null
}

internal data class PlaylistHolder(var title: String, var mediaItems: List<MediaItem>)
