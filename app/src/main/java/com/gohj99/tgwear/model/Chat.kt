/*
 * Copyright (c) 2024-2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.gohj99.tgwear.model

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import org.drinkless.tdlib.TdApi

@SuppressLint("ParcelCreator")
data class Chat(
    val id: Long,
    val title: String,
    val accentColorId: Int = 2,
    val unreadCount: Int = 0,
    val lastMessage: AnnotatedString = buildAnnotatedString {},
    val lastMessageTime: Int = -1,
    val lastMessageDraft: AnnotatedString = buildAnnotatedString {},
    val lastMessageTimeDraft: Int = -1,
    val chatPhoto: TdApi.File? = null,
    val order: Long = -1,
    val orderDraft: Long = -1,
    val needNotification: Boolean = true,
    val isPinned: Boolean = false,
    val isRead: Boolean = false,
    val isBot: Boolean = false,
    val isChannel: Boolean = false,
    val isGroup: Boolean = false,
    val isPrivateChat: Boolean = false,
    val isArchiveChatPin: Boolean? = null
) : Parcelable {

    override fun describeContents(): Int = 0

    // 装箱：明确写出你需要传递的字段
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeString(title)
        dest.writeInt(accentColorId)
        dest.writeInt(unreadCount)
        dest.writeInt(lastMessageTime)
        dest.writeInt(lastMessageTimeDraft)
        dest.writeLong(order)
        dest.writeLong(orderDraft)
        dest.writeByte(if (needNotification) 1 else 0)
        dest.writeByte(if (isPinned) 1 else 0)
        dest.writeByte(if (isRead) 1 else 0)
        dest.writeByte(if (isBot) 1 else 0)
        dest.writeByte(if (isChannel) 1 else 0)
        dest.writeByte(if (isGroup) 1 else 0)
        dest.writeByte(if (isPrivateChat) 1 else 0)
        // 注意：我们没有写入那三个会导致报错的复杂字段
    }

    override fun toString(): String {
        return "Chat(id=$id, title=$title)"
    }

    companion object CREATOR : Parcelable.Creator<Chat> {
        // 拆箱：严格按照装箱的顺序，一个不落地读出来
        override fun createFromParcel(parcel: Parcel): Chat {
            return Chat(
                id = parcel.readLong(),
                title = parcel.readString() ?: "",
                accentColorId = parcel.readInt(),
                unreadCount = parcel.readInt(),

                // 跳过 lastMessage，它会自动使用你的 buildAnnotatedString {} 默认值

                lastMessageTime = parcel.readInt(),

                // 跳过 lastMessageDraft，使用默认值

                lastMessageTimeDraft = parcel.readInt(),

                // 跳过 chatPhoto，使用默认 null

                order = parcel.readLong(),
                orderDraft = parcel.readLong(),
                needNotification = parcel.readByte() != 0.toByte(),
                isPinned = parcel.readByte() != 0.toByte(),
                isRead = parcel.readByte() != 0.toByte(),
                isBot = parcel.readByte() != 0.toByte(),
                isChannel = parcel.readByte() != 0.toByte(),
                isGroup = parcel.readByte() != 0.toByte(),
                isPrivateChat = parcel.readByte() != 0.toByte()

                // isArchiveChatPin 我们没传，它会自动使用 null
            )
        }

        override fun newArray(size: Int): Array<Chat?> {
            return arrayOfNulls(size)
        }
    }
}
