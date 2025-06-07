/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.gohj99.tgwear.ui.chat

import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.drinkless.tdlib.TdApi

@Composable
fun MessageReactions(
    reactions: TdApi.MessageReactions?,
    isCurrentUser: Boolean
) {
    reactions?.let {
        val messageReactions = it.reactions

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(if (isCurrentUser) Alignment.End else Alignment.Start)
                .padding(top = 1.dp, bottom = 2.5.dp, start = 3.5.dp, end = 3.5.dp),
            maxItemsInEachRow = 4    // 每行之间的垂直间距
        ) {
            messageReactions.forEach { unreadReaction ->
                when (val type = unreadReaction.type) {
                    is TdApi.ReactionTypeEmoji -> {
                        Surface(
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(1.dp),
                            color = if (unreadReaction.isChosen) Color(0xFF3F95CF) else Color(0xFF1C3345),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = "${type.emoji} ${unreadReaction.totalCount}",
                                color = Color.White,
                                modifier = Modifier
                                    .padding(horizontal = 5.dp, vertical = 2.dp)
                            )
                        }
                    }
                    is TdApi.ReactionTypePaid -> {
                        // TODO: 付费表情
                    }
                    is TdApi.ReactionTypeCustomEmoji -> {
                        // TODO: 自定义表情
                    }
                }
            }
        }
    }
}
