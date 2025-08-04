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
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.gohj99.tgwear.model.Chat
import com.gohj99.tgwear.ui.VoiceCallScreen
import com.gohj99.tgwear.ui.theme.TGwearTheme
import com.gohj99.tgwear.utils.notification.TgApiForPushNotification
import com.gohj99.tgwear.utils.notification.TgApiForPushNotificationManager
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
    private val pushTgApi: TgApiForPushNotification? = TgApiForPushNotificationManager.tgApi
    private lateinit var callItem: TdApi.Call
    private lateinit var chatItem: Chat
    private val state = mutableStateOf("CallStatePending")
    private lateinit var audioManager: AudioManager
    private val emoji = mutableStateOf("")
    private val callDuration = mutableStateOf(0)
    private var isIncomingCall = true
    private var updateCallDurationRunState = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            @Suppress("DEPRECATION")
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
            )
        }
        wakeUpAndUnlock()
        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager

        //println("调试0")

        // 获取 TgApi 实例
        if (tgApi == null && pushTgApi == null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        // 获取call对象
        if (tgApi?.callItem != null) {
            callItem = tgApi.callItem!!
            isIncomingCall = tgApi.isIncomingCall
        }
        else if (pushTgApi?.callItem != null) {
            callItem = pushTgApi.callItem!!
            isIncomingCall = true
        }
        else {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        fun updateState (update: TdApi.Call, str: String?) {
            // 删除通话通知
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(1001)
            state.value = when (val state = update.state) {
                is TdApi.CallStatePending -> {
                    if (isIncomingCall) {
                        "CallStatePending"
                    } else {
                        if (callItem.isOutgoing) {
                            if (str != "first") {
                                "SelfCallStatePending"
                            } else {
                                "SelfCallStatePendingWait"
                            }
                        } else {
                            "SelfCallStatePendingWait"
                        }
                    }
                }
                is TdApi.CallStateReady -> {
                    if (str == null) {
                        // 交换密钥阶段完成
                        updateCallDuration()

                        // 通知系统进入通话状态
                        audioManager.mode = AudioManager.MODE_IN_COMMUNICATION // 设置通话模式
                        audioManager.isMicrophoneMute = false // 打开麦克风
                        if (audioManager.isBluetoothScoAvailableOffCall) {
                            audioManager.startBluetoothSco()
                            audioManager.isBluetoothScoOn = true
                        }

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
                is TdApi.CallStateError -> {
                    destroyMe()
                    if (state.error.code == 403) "CallStateError403"
                    else state.error.message
                }
                is TdApi.CallStateExchangingKeys -> "CallStateExchangingKeys"
                else -> "CallStateWaiting"
            }
        }
        tgApi?.onCallback[callItem.userId] = ::updateState
        pushTgApi?.onCallback[callItem.userId] = ::updateState
        tgApi?.onCallback[callItem.userId]?.invoke(callItem, "first")

        // 检测权限
        checkAndRequestAudioPermission(this)

        //println("调试1")
        chatItem = tgApi?.chatsList?.value?.firstOrNull { it.id == callItem.userId } ?: runBlocking {
            if (tgApi != null) {
                val chatObject = tgApi.getChat(callItem.userId)  // 在 runBlocking 中赋值
                Chat(
                    id = callItem.userId,
                    title = chatObject?.title ?: "",
                    chatPhoto = chatObject?.photo?.small
                )
            } else {
                val chatObject = pushTgApi!!.getChat(callItem.userId)  // 在 runBlocking 中赋值
                Chat(
                    id = callItem.userId,
                    title = chatObject.title ?: "",
                    chatPhoto = chatObject.photo?.small
                )
            }
        }
        //println("调试2")

        setContent {
            TGwearTheme {
                VoiceCallScreen(
                    chat = chatItem,
                    state = state,
                    emoji = emoji,
                    callDuration = callDuration,
                    acceptCall = {
                        pushTgApi?.acceptCall(callItem.id)
                        tgApi?.acceptCall(callItem.id)
                    },
                    disCall = {
                        pushTgApi?.discardCall(callItem)
                        tgApi?.discardCall(callItem)
                    },
                    onMute = {
                        audioManager.isMicrophoneMute = it
                    }
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        audioManager.mode = AudioManager.MODE_NORMAL
        audioManager.stopBluetoothSco()
        tgApi?.onCallback?.remove(callItem.userId)
        tgApi?.discardCall(callItem)
        pushTgApi?.onCallback?.remove(callItem.userId)
        pushTgApi?.discardCall(callItem)
    }

    fun destroyMe() {
        CoroutineScope(Dispatchers.IO).launch {
            Thread.sleep(2000)
            finish()
        }
    }

    fun updateCallDuration() {
        CoroutineScope(Dispatchers.IO).launch {
            if (updateCallDurationRunState) {
                Thread.sleep(600)
                callDuration.value = 0
            } else {
                updateCallDurationRunState = true
                Thread.sleep(600)
                while (true) {
                    Thread.sleep(1000)
                    callDuration.value += 1
                }
            }
        }
    }

    private fun wakeUpAndUnlock() {
        val powerManager = getSystemService(POWER_SERVICE) as PowerManager
        val wakeLock = powerManager.newWakeLock(
            PowerManager.FULL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.ON_AFTER_RELEASE,
            "tgwear:VoiceCallWakeLock"
        )
        wakeLock.acquire(3000) // 保持 3 秒
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
