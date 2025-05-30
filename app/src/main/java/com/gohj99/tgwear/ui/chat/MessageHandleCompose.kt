/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.gohj99.tgwear.ui.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.gohj99.tgwear.R
import com.gohj99.tgwear.model.Chat
import com.gohj99.tgwear.ui.AutoScrollingText
import com.gohj99.tgwear.utils.formatTimestampToDate
import org.drinkless.tdlib.TdApi

@Composable
fun MessageHandleCompose(
    message: TdApi.Message,
    chatList: MutableState<List<TdApi.Message>>,
    index: Int,
    chatId: Long,
    chatTitle: String,
    isCurrentUser: Boolean,
    modifier: Modifier = Modifier,
    alignment: Arrangement.Horizontal = Arrangement.End,
    textColor: Color = Color.White,
    backgroundColor: Color = Color(0xFF3A4048),
    stateDownload: MutableState<Boolean>,
    stateDownloadDone: MutableState<Boolean>,
    showUnknownMessageType: Boolean,
    selectMessage: MutableState<TdApi.Message>,
    isLongPressed: MutableState<Boolean>,
    senderNameMap: MutableState<MutableMap<Long, String?>>,
    listState: LazyListState = rememberLazyListState(),
    lastReadOutboxMessageId: MutableState<Long>,
    lastReadInboxMessageId: MutableState<Long>,
    press: (TdApi.Message) -> Unit,
    onLinkClick: (String) -> Unit,
    goToChat: (Chat) -> Unit,
    chatTopics: Map<Long, String>,
) {
    Column {
        // 绘制日期
        val nextItem = chatList.value.getOrNull(index + 1)
        if (nextItem == null){
            DateText(formatTimestampToDate(message.date))
        } else {
            val currentDate = formatTimestampToDate(message.date)
            if (formatTimestampToDate(nextItem.date) != currentDate) {
                DateText(currentDate)
            }
        }

        when(message.content) {
            is TdApi.MessageChatAddMembers -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    NotificationMessageCompose(
                        {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                // 渲染用户名字
                                UserNameCompose(
                                    message = message,
                                    chatId = chatId,
                                    chatTitle = chatTitle,
                                    isCurrentUser = isCurrentUser,
                                    selectMessage = selectMessage,
                                    isLongPressed = isLongPressed,
                                    senderNameMap = senderNameMap,
                                    goToChat = goToChat
                                )
                                AutoScrollingText(
                                    text = stringResource(R.string.joined_the_group),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    )
                }
            }

            else -> {
                // 渲染用户名字
                UserNameCompose(
                    message = message,
                    chatId = chatId,
                    chatTitle = chatTitle,
                    isCurrentUser = isCurrentUser,
                    selectMessage = selectMessage,
                    isLongPressed = isLongPressed,
                    senderNameMap = senderNameMap,
                    goToChat = goToChat
                )

                // 回复
                MessageReplyCompose(
                    message = message,
                    chatId = chatId,
                    isCurrentUser = isCurrentUser,
                    alignment = alignment,
                    textColor = textColor,
                    stateDownload = stateDownload,
                    stateDownloadDone = stateDownloadDone,
                    showUnknownMessageType = showUnknownMessageType,
                    senderNameMap = senderNameMap,
                    listState = listState,
                    chatList = chatList,
                    chatTitle = chatTitle,
                    onLinkClick = onLinkClick
                )

                // 正文
                MessageMainBodyCompose(
                    message = message,
                    editDate = message.editDate,
                    modifier = modifier,
                    alignment = alignment,
                    textColor = textColor,
                    backgroundColor = backgroundColor,
                    stateDownload = stateDownload,
                    stateDownloadDone = stateDownloadDone,
                    showUnknownMessageType = showUnknownMessageType,
                    selectMessage = selectMessage,
                    isLongPressed = isLongPressed,
                    lastReadOutboxMessageId = lastReadOutboxMessageId,
                    lastReadInboxMessageId = lastReadInboxMessageId,
                    onLinkClick = onLinkClick,
                    press = press,
                    chatTopics = chatTopics,
                    isCurrentUser = isCurrentUser
                )
            }
        }
    }
}
