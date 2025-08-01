/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.testapp.controller

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaBrowser
import androidx.media3.session.SessionCommand
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.ListenableFuture

class SearchMediaItemsAdapter(
    private val activity: Activity,
    private val mediaBrowser: MediaBrowser
) : RecyclerView.Adapter<SearchMediaItemsAdapter.ViewHolder>() {
    private var items: List<MediaItem> = emptyList()

    init {
        val searchItemsList: RecyclerView = activity.findViewById(R.id.search_items_list)
        searchItemsList.layoutManager = LinearLayoutManager(activity)
        searchItemsList.setHasFixedSize(true)
        searchItemsList.adapter = this

        val searchButton: Button = activity.findViewById(R.id.search_button)
        val queryTextView: TextView = activity.findViewById(R.id.search_query)

        searchButton.setOnClickListener {
            if (!supportSearch()) {
                Toast.makeText(activity, R.string.command_not_supported_msg, Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            val query: String = queryTextView.text.toString()
            if (query.isEmpty()) {
                Toast.makeText(activity, R.string.search_query_empty_msg, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val future: ListenableFuture<LibraryResult<Void>> = mediaBrowser.search(query, null)
            future.addListener(
                {
                    if (future.get().resultCode == LibraryResult.RESULT_SUCCESS) {
                        val searchFuture: ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> =
                            mediaBrowser.getSearchResult(
                                query,
                                /* page= */ 0,
                                /* pageSize= */ Int.MAX_VALUE,
                                /* params= */ null
                            )
                        searchFuture.addListener(
                            {
                                val mediaItems: List<MediaItem>? = searchFuture.get().value
                                updateItems(mediaItems ?: emptyList())
                            },
                            ContextCompat.getMainExecutor(activity)
                        )
                    } else {
                        updateItems(emptyList())
                    }
                },
                ContextCompat.getMainExecutor(activity)
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.media_browse_item, parent, false)
        )

    @SuppressWarnings("FutureReturnValueIgnored")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (items.isEmpty()) {
            if (!supportSearch()) {
                setMessageForEmptyList(
                    holder,
                    activity.getString(R.string.command_not_supported_msg)
                )
            } else {
                setMessageForEmptyList(holder, activity.getString(R.string.search_result_empty))
            }
            return
        }

        val mediaMetadata: MediaMetadata = items[position].mediaMetadata
        holder.name.text = mediaMetadata.title ?: "Title metadata empty"
        holder.subtitle.text = mediaMetadata.subtitle ?: "Subtitle metadata empty"
        holder.subtitle.visibility = View.VISIBLE
        holder.icon.visibility = View.VISIBLE

        when {
            mediaMetadata.artworkUri != null -> {
                holder.icon.setImageURI(mediaMetadata.artworkUri)
            }

            mediaMetadata.artworkData != null -> {
                val bitmap: Bitmap =
                    BitmapFactory.decodeByteArray(
                        mediaMetadata.artworkData,
                        0,
                        mediaMetadata.artworkData!!.size
                    )
                holder.icon.setImageBitmap(bitmap)
            }

            else -> {
                holder.icon.setImageResource(R.drawable.ic_album_black_24dp)
            }
        }

        val item: MediaItem = items[position]
        holder.itemView.setOnClickListener {
            if (mediaMetadata.isPlayable == true) {
                mediaBrowser.setMediaItem(MediaItem.Builder().setMediaId(item.mediaId).build())
                mediaBrowser.prepare()
                mediaBrowser.play()
            }
        }
    }

    override fun getItemCount(): Int {
        if (items.isEmpty()) return 1
        return items.size
    }

    private fun supportSearch(): Boolean =
        mediaBrowser.availableSessionCommands.contains(SessionCommand.COMMAND_CODE_LIBRARY_SEARCH)

    fun updateItems(newItems: List<MediaItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    private fun setMessageForEmptyList(holder: ViewHolder, message: String) {
        holder.name.text = message
        holder.subtitle.visibility = View.GONE
        holder.icon.visibility = View.GONE
        holder.itemView.setOnClickListener {}
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.item_name)
        val subtitle: TextView = itemView.findViewById(R.id.item_subtitle)
        val icon: ImageView = itemView.findViewById(R.id.item_icon)
    }
}
