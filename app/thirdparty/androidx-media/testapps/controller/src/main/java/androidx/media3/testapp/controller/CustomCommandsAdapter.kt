/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.testapp.controller

import android.app.Activity
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.media3.session.CommandButton
import androidx.media3.session.MediaController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/** Helper class that displays and handles custom commands. */
class CustomCommandsAdapter(
    activity: Activity,
    private val mediaController: MediaController,
    packageName: String,
) : RecyclerView.Adapter<CustomCommandsAdapter.ViewHolder>() {
    private var commands: List<CommandButton> = emptyList()
    private val resources: Resources =
        activity.packageManager.getResourcesForApplication(packageName)

    init {
        val customCommandsList: RecyclerView = activity.findViewById(R.id.custom_commands_list)
        customCommandsList.layoutManager = LinearLayoutManager(activity)
        customCommandsList.setHasFixedSize(true)
        customCommandsList.adapter = this
        setCommands(mediaController.customLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.media_custom_command, parent, false)
        )

    @SuppressWarnings("FutureReturnValueIgnored")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val commandButton: CommandButton = commands[position]
        holder.name.text = commandButton.displayName
        holder.description.text = commandButton.sessionCommand?.customAction
        if (commandButton.iconResId != 0) {
            val iconDrawable: Drawable? =
                ResourcesCompat.getDrawable(resources, commandButton.iconResId, null)
            holder.icon.setImageDrawable(iconDrawable)
        }
        holder.itemView.setOnClickListener {
            commandButton.sessionCommand?.let {
                mediaController.sendCustomCommand(
                    it,
                    Bundle.EMPTY
                )
            }
        }
    }

    override fun getItemCount(): Int = commands.size

    fun setCommands(newCommands: List<CommandButton>) {
        val diffResult: DiffUtil.DiffResult =
            DiffUtil.calculateDiff(
                object : DiffUtil.Callback() {
                    override fun getOldListSize(): Int = commands.size

                    override fun getNewListSize(): Int = newCommands.size

                    override fun areItemsTheSame(
                        oldItemPosition: Int,
                        newItemPosition: Int
                    ): Boolean =
                        commands.size == newCommands.size &&
                                commands[oldItemPosition] == newCommands[newItemPosition]

                    override fun areContentsTheSame(
                        oldItemPosition: Int,
                        newItemPosition: Int
                    ): Boolean =
                        commands[oldItemPosition] == newCommands[newItemPosition]
                }
            )
        commands = newCommands
        diffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.action_name)
        val description: TextView = itemView.findViewById(R.id.action_description)
        val icon: ImageView = itemView.findViewById(R.id.action_icon)
    }
}
