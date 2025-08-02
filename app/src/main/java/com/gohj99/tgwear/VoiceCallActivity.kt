/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.gohj99.tgwear

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.gohj99.tgwear.model.Chat
import com.gohj99.tgwear.ui.VoiceCallScreen
import com.gohj99.tgwear.ui.theme.TGwearTheme
import com.gohj99.tgwear.utils.telegram.TgApi
import com.gohj99.tgwear.utils.telegram.acceptCall
import com.gohj99.tgwear.utils.telegram.discardCall
import com.gohj99.tgwear.utils.telegram.getChat
import kotlinx.coroutines.runBlocking
import org.drinkless.tdlib.TdApi

private const val RECORD_AUDIO_PERMISSION = Manifest.permission.RECORD_AUDIO
private const val REQUEST_CODE_AUDIO = 1001

class VoiceCallActivity : BaseActivity() {
    private lateinit var tgApi: TgApi
    private lateinit var callItem: TdApi.Call
    private lateinit var chatItem: Chat
    private val state = mutableStateOf("CallStatePending")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 获取 TgApi 实例
        if (TgApiManager.tgApi == null) {
            finish()
            return
        }
        tgApi = TgApiManager.tgApi!!

        // 获取call对象
        if (tgApi.callItem == null) {
            finish()
            return
        }
        callItem = tgApi.callItem!!

        // 检测权限
        checkAndRequestAudioPermission(this)

        chatItem = tgApi.chatsList.value.firstOrNull { it.id == callItem.userId } ?: runBlocking {
            val chatObject = tgApi.getChat(callItem.userId)  // 在 runBlocking 中赋值
            Chat(
                id = callItem.userId,
                title = chatObject?.title ?: "",
                chatPhoto = chatObject?.photo?.small
            )
        }

        tgApi.onCallback = {update ->
            state.value = when (update.state) {
                is TdApi.CallStatePending -> "CallStatePending"
                is TdApi.CallStateReady -> "CallStateReady"
                is TdApi.CallStateHangingUp -> "CallStateHangingUp"
                is TdApi.CallStateDiscarded -> "CallStateDiscarded"
                is TdApi.CallStateExchangingKeys -> "CallStateExchangingKeys"
                else -> "CallStateWaiting"
            }
        }

        setContent {
            TGwearTheme {
                VoiceCallScreen(
                    chat = chatItem,
                    state = state,
                    acceptCall = {
                        tgApi.acceptCall(callItem.id)
                    },
                    disCall = {
                        tgApi.discardCall(callItem)
                    }
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tgApi.discardCall(callItem)
    }

    fun checkAndRequestAudioPermission(activity: Activity) {
        if (ContextCompat.checkSelfPermission(activity, RECORD_AUDIO_PERMISSION)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                activity,
                arrayOf(RECORD_AUDIO_PERMISSION),
                REQUEST_CODE_AUDIO
            )
        }
    }
}
