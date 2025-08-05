/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.gohj99.tgwear.ui

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gohj99.tgwear.R
import com.gohj99.tgwear.model.Chat
import com.gohj99.tgwear.utils.getColorById

const val VOIP_CONNECTION_MIN_LAYER = 65

@Composable
fun VoiceCallScreen(
    chat: Chat,
    state: MutableState<String>,
    emoji: MutableState<String>,
    callDuration: MutableState<Int>,
    acceptCall: () -> Unit,
    disCall: () -> Unit,
    onMute: (Boolean) -> Unit,
) {
    val context = LocalContext.current
    var isMute by remember { mutableStateOf(false) }

    LaunchedEffect(state.value) {
        when (state.value) {
            "CallStatePending" -> context.startVibration()
            else -> context.stopVibration()
        }
    }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        // 包含 Row 的 Box
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 48.dp, end = 48.dp, top = 14.dp) // 添加顶部填充
        ) {
            AutoScrollingText(
                text = stringResource(id = R.string.Calling),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp)) // 添加间距

        Row (
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (chat.chatPhoto != null) {
                ThumbnailChatPhoto(chat.chatPhoto, 35, chat.title)
                Spacer(Modifier.width(6.dp))
            } else {
                Surface(
                    modifier = Modifier
                        .size(35.dp), // 固定宽高为35dp
                    color = getColorById(chat.accentColorId),
                    shape = CircleShape
                ) {
                    Box(contentAlignment = Alignment.Center) { // 居中显示文本
                        Text(
                            text = chat.title[0].toString().uppercase(),
                            color = Color.White,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Spacer(Modifier.width(6.dp))
            }

            Spacer(modifier = Modifier.width(4.dp)) // 添加间距

            Text(
                text = chat.title,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp)) // 添加间距

        if (emoji.value.isNotBlank()) {
            Text(
                text = emoji.value,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        when (state.value) {
            // 对方来电等待接听
            "CallStatePending" -> {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    CustomButton(
                        onClick = acceptCall,
                        text = stringResource(id = R.string.Accept_Call)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp)) // 添加间距
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    CustomButton(
                        onClick = disCall,
                        color = Color(0xFFF44336),
                        text = stringResource(id = R.string.Declined)
                    )
                }
            }

            // 去电等待对方接听
            "SelfCallStatePending" -> {
                Text(
                    text = stringResource(id = R.string.Ringing),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(4.dp)
                )
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    CustomButton(
                        onClick = disCall,
                        color = Color(0xFFF44336),
                        text = stringResource(id = R.string.End_call)
                    )
                }
            }

            // 通话中
            "CallStateReady" -> {
                if (isMute) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        CustomButton(
                            onClick = {
                                isMute = false
                                onMute(false)
                            },
                            text = stringResource(id = R.string.Unmute)
                        )
                    }
                } else {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        CustomButton(
                            onClick = {
                                isMute = true
                                onMute(true)
                            },
                            text = stringResource(id = R.string.Mute)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Box(
                    contentAlignment = Alignment.Center
                ) {
                    CustomButton(
                        onClick = disCall,
                        color = Color(0xFFF44336),
                        text = stringResource(id = R.string.Hang_Up)
                    )
                }
                Text(
                    text = formatTime(callDuration.value),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
            }

            // 交换密钥
            "CallStateExchangingKeys" -> {
                Text(
                    text = stringResource(id = R.string.Exchanging_Encryption_Keys),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
            }

            "CallStateError403" -> {
                Text(
                    text = stringResource(id = R.string.User_Privacy_Restricted),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(4.dp)
                )
            }

            "SelfCallStatePendingWait" -> {
                Text(
                    text = stringResource(id = R.string.Waiting),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(4.dp)
                )
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    CustomButton(
                        onClick = disCall,
                        color = Color(0xFFF44336),
                        text = stringResource(id = R.string.End_call)
                    )
                }
            }

            "CallStateDiscarded", "CallStateHangingUp" -> {
                Text(
                    text = stringResource(id = R.string.Call_Ended),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(4.dp)
                )
            }

            // 未知错误
            else -> {
                Text(
                    text = state.value,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}

private fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return String.format("%02d:%02d", minutes, secs)
}

private fun Context.startVibration() {
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vm = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vm.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    val effect = VibrationEffect.createWaveform(longArrayOf(0, 500, 1000), 0)
    vibrator.vibrate(effect)
}

private fun Context.stopVibration() {
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vm = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vm.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }
    vibrator.cancel()
}
