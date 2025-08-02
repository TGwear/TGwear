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
import android.media.AudioManager
import android.os.Build
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.drinkless.tdlib.TdApi

private const val RECORD_AUDIO_PERMISSION = Manifest.permission.RECORD_AUDIO
private const val REQUEST_CODE_AUDIO = 1001

class VoiceCallActivity : BaseActivity() {
    private val tgApi: TgApi? = TgApiManager.tgApi
    private lateinit var callItem: TdApi.Call
    private lateinit var chatItem: Chat
    private val state = mutableStateOf("CallStatePending")
    private lateinit var audioManager: AudioManager
    private val emoji = mutableStateOf("")
    private val callDuration = mutableStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        }

        // 获取 TgApi 实例
        if (tgApi == null) {
            finish()
            return
        }

        // 获取call对象
        if (tgApi.callItem == null) {
            finish()
            return
        }
        callItem = tgApi.callItem!!

        tgApi.onCallback[callItem.userId] = {update, str ->
            state.value = when (update.state) {
                is TdApi.CallStatePending -> {
                    if (callItem.isOutgoing) {
                        "SelfCallStatePending"
                    } else {
                        "CallStatePending"
                    }
                }
                is TdApi.CallStateReady -> {
                    if (str == null) {
                        // 交换密钥阶段完成
                        updateCallDuration()
                        "CallStateReady"
                    } else {
                        // 交换密钥中
                        emoji.value = str
                        "CallStateExchangingKeys"
                    }
                }
                is TdApi.CallStateHangingUp -> {
                    destroyMe()
                    "CallStateHangingUp"
                }
                is TdApi.CallStateDiscarded -> {
                    destroyMe()
                    "CallStateDiscarded"
                }
                is TdApi.CallStateExchangingKeys -> "CallStateExchangingKeys"
                else -> "CallStateWaiting"
            }
        }

        // 检测权限
        checkAndRequestAudioPermission(this)

        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager

        chatItem = tgApi.chatsList.value.firstOrNull { it.id == callItem.userId } ?: runBlocking {
            val chatObject = tgApi.getChat(callItem.userId)  // 在 runBlocking 中赋值
            Chat(
                id = callItem.userId,
                title = chatObject?.title ?: "",
                chatPhoto = chatObject?.photo?.small
            )
        }

        audioManager.mode = AudioManager.MODE_IN_COMMUNICATION // 设置通话模式
        audioManager.isMicrophoneMute = false                  // 打开麦克风
        audioManager.isSpeakerphoneOn = true                   // 使用扬声器

        setContent {
            TGwearTheme {
                VoiceCallScreen(
                    chat = chatItem,
                    state = state,
                    emoji = emoji,
                    callDuration = callDuration,
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
        audioManager.mode = AudioManager.MODE_NORMAL
        audioManager.abandonAudioFocus(null)
        tgApi?.onCallback?.remove(callItem.userId)
        tgApi?.discardCall(callItem)
    }

    fun destroyMe() {
        CoroutineScope(Dispatchers.IO).launch {
            Thread.sleep(2000)
            finish()
        }
    }

    fun updateCallDuration() {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                Thread.sleep(1000)
                callDuration.value += 1
            }
        }
    }
    // 检查并请求录音权限
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
