/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.gohj99.tgwear.utils.notification

import android.content.ContentResolver
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import com.gohj99.tgwear.R
import com.gohj99.tgwear.StartVoiceCallActivityForegroundService
import com.gohj99.tgwear.TgApiManager.tgApi
import com.gohj99.tgwear.VoiceCallActivity
import com.gohj99.tgwear.getAppVersion
import com.gohj99.tgwear.loadConfig
import com.gohj99.tgwear.utils.generateChatTitleIconBitmap
import com.gohj99.tgwear.utils.getNetworkType
import com.gohj99.tgwear.utils.telegram.getChat
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.drinkless.tdlib.Client
import org.drinkless.tdlib.TdApi
import org.thunderdog.challegram.voip.ConnectionStateListener
import org.thunderdog.challegram.voip.NetworkStats
import org.thunderdog.challegram.voip.VoIP
import org.thunderdog.challegram.voip.VoIPInstance
import org.thunderdog.challegram.voip.annotation.AudioState
import org.thunderdog.challegram.voip.annotation.CallState
import org.thunderdog.challegram.voip.annotation.VideoState
import java.io.File
import java.io.IOException
import java.util.concurrent.CountDownLatch

class TgApiForPushNotification(private val context: Context) {
    private val sharedPref = context.getSharedPreferences("LoginPref", MODE_PRIVATE)
    private val client: Client = Client.create({ update -> handleUpdate(update) }, null, null)
    private val userList = sharedPref.getString("userList", "")
    private val gson = Gson()
    @Volatile private var isAuthorized: Boolean = false
    private val authLatch = CountDownLatch(1)
    private var currentUser: List<String> = emptyList()
    private var userId = ""
    val settingsSharedPref: SharedPreferences = context.getSharedPreferences("app_settings", MODE_PRIVATE)
    var callItem: TdApi.Call? = null
    var voipItem: VoIPInstance? = null
    var onCallback = mutableMapOf<Long, (TdApi.Call, String?) -> Unit>()
    private val chatCache = mutableMapOf<Long, TdApi.Chat>()

    init {
        // 获取用户ID
        userId = if (userList != null && userList.isNotEmpty()) {
            gson.fromJson(userList, JsonObject::class.java)
                .keySet().firstOrNull().toString()
        } else throw IllegalStateException("User list is empty or null")
        // 获取应用外部数据目录
        val externalDir: File = context.getExternalFilesDir(null)
            ?: throw IllegalStateException("Failed to get external directory.")
        // 获取API ID和API Hash
        val config = loadConfig(context)
        val tdapiId = config.getProperty("api_id").toInt()
        val tdapiHash = config.getProperty("api_hash")
        val encryptionKeyString = sharedPref.getString("encryption_key", null)
        val isUseTestDc = settingsSharedPref.getBoolean("useTestDc", false)
        client.send(TdApi.SetTdlibParameters().apply {
            databaseDirectory = externalDir.absolutePath + (if (userId == "") "/tdlib" else {
                "/$userId/tdlib"
            })
            apiId = tdapiId
            apiHash = tdapiHash
            systemLanguageCode = context.resources.configuration.locales[0].language
            deviceModel = Build.MODEL
            systemVersion = Build.VERSION.RELEASE
            applicationVersion = getAppVersion(context)
            useSecretChats = true
            useMessageDatabase = true
            useChatInfoDatabase = true
            useFileDatabase = false
            useTestDc = isUseTestDc
            filesDirectory = externalDir.absolutePath
            databaseEncryptionKey = encryptionKeyString?.chunked(2)?.map { it.toInt(16).toByte() }
                ?.toByteArray()
                ?: throw IllegalStateException("Encryption key not found")
        }) { result ->
            println("SetTdlibParameters result: $result")
            if (result is TdApi.Error) {
                throw IllegalStateException(result.message)
            }
        }

        // 等待授权状态更新
        try {
            authLatch.await()
        } catch (e: InterruptedException) {
            close()
            throw IllegalStateException("Interrupted while waiting for authorization", e)
        }

        if (!isAuthorized) {
            close()
            throw IllegalStateException("Failed to authorize")
        }

        client.send(TdApi.GetMe()) {
            if (it is TdApi.User) {
                val user = it
                currentUser = listOf(user.id.toString(), "${user.firstName} ${user.lastName}")
            }
        }
    }

    // 处理 TDLib 更新的函数
    private fun handleUpdate(update: TdApi.Object) {
        when (update.constructor) {
            TdApi.UpdateAuthorizationState.CONSTRUCTOR -> handleAuthorizationState(update as TdApi.UpdateAuthorizationState)
            TdApi.UpdateNewMessage.CONSTRUCTOR -> handleNewMessage(update as TdApi.UpdateNewMessage)
            TdApi.UpdateCall.CONSTRUCTOR -> handleCallUpdate(update as TdApi.UpdateCall)
            //TdApi.UpdateNotification.CONSTRUCTOR -> handleNotification(update as TdApi.UpdateNotification)
            TdApi.UpdateNewCallSignalingData.CONSTRUCTOR -> handleNewCallSignalingDataUpdate(update as TdApi.UpdateNewCallSignalingData)
            TdApi.UpdateNotificationGroup.CONSTRUCTOR -> handleNotificationGroupUpdate(update as TdApi.UpdateNotificationGroup)
            else -> {
                Log.d("TdApiUpdate","Received update: $update")
            }
        }
    }

    fun handleNotificationGroupUpdate(update: TdApi.UpdateNotificationGroup) {
        //println("Received notification group update: $update")
        val addedNotifications = update.addedNotifications
        val chatId = update.chatId
        addedNotifications.forEach { notification ->
            when (val type = notification.type) {
                is TdApi.NotificationTypeNewMessage -> {
                    val message = type.message
                    //val chatId = message.chatId
                    // 异步获取聊天标题和聊天信息
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val chatResult = getChat(chatId)
                            if (chatResult.constructor == TdApi.Chat.CONSTRUCTOR) {

                                // 判断是否是群组
                                var isGroup = false
                                when (chatResult.type) {
                                    is TdApi.ChatTypeSupergroup -> {
                                        isGroup = true
                                    }
                                    is TdApi.ChatTypeBasicGroup -> {
                                        isGroup = true
                                    }
                                }

                                // 获取聊天图片
                                var bmp = drawableToBitmap(context, R.mipmap.ic_launcher)!!
                                val photoFile = chatResult.photo?.small
                                if (photoFile?.local?.isDownloadingCompleted == true) {
                                    val filePath = photoFile.local.path
                                    val file = File(filePath)
                                    if (file.exists()) {
                                        // 这里可以处理图片文件，例如显示或使用
                                        loadBitmapFromUri(context.contentResolver, Uri.fromFile(file))?.let {
                                            bmp = it
                                        }
                                    }
                                } else {
                                    // 使用默认图标
                                    bmp = generateChatTitleIconBitmap(
                                        context,
                                        chatResult.title,
                                        chatResult.accentColorId
                                    )
                                }

                                //val accentColorId = chatResult.accentColorId
                                val needNotification = chatResult.notificationSettings.muteFor == 0
                                val chatTitle = chatResult.title

                                // 获取发送者名称
                                var senderName = chatTitle
                                if (isGroup) {
                                    when (val senderId = message.senderId) {
                                        is TdApi.MessageSenderUser -> {
                                            val userId = senderId.userId
                                            val userResult = sendRequest(TdApi.GetUser(userId))
                                            if (userResult is TdApi.User) {
                                                senderName = "${userResult.firstName} ${userResult.lastName}"
                                            }
                                        }
                                        is TdApi.MessageSenderChat -> {
                                            // 处理群组消息的发送者
                                            if (senderId.chatId == chatId) {
                                                senderName = chatTitle
                                            } else {
                                                val itChat = tgApi?.getChat(senderId.chatId)
                                                itChat.let {
                                                    senderName = it!!.title
                                                }
                                            }
                                        }
                                    }
                                }

                                if (needNotification) {
                                    context.sendChatMessageNotification(
                                        title = chatTitle,
                                        message = handleAllMessages(message),
                                        senderName = senderName,
                                        conversationId = chatId.toString(),
                                        timestamp = message.date * 1000L,
                                        isGroupChat = isGroup,
                                        chatIconBitmap = bmp // 这里可以传入群组图标的 Uri
                                    )
                                }
                            }
                        } catch (e: Exception) {
                            println("handleNotification failed: ${e.message}")
                        }
                    }
                }
                is TdApi.NotificationTypeNewPushMessage -> {
                    //println("Received push message: $type")
                    val content = type.content
                    val senderName = type.senderName
                    val senderId = when (type.senderId) {
                        is TdApi.MessageSenderUser -> (type.senderId as TdApi.MessageSenderUser).userId
                        is TdApi.MessageSenderChat -> (type.senderId as TdApi.MessageSenderChat).chatId
                        else -> 0
                    }

                    if (!type.isOutgoing) {
                        // 异步获取聊天标题和聊天信息
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                val chatResult = getChat(chatId)
                                if (chatResult.constructor == TdApi.Chat.CONSTRUCTOR) {
                                    // 获取群组名字
                                    val chatTitle = chatResult.title

                                    // 判断是否是群组
                                    var isGroup = false
                                    when (chatResult.type) {
                                        is TdApi.ChatTypeSupergroup -> {
                                            isGroup = true
                                        }
                                        is TdApi.ChatTypeBasicGroup -> {
                                            isGroup = true
                                        }
                                    }

                                    // 获取聊天图片
                                    var bmp = drawableToBitmap(context, R.mipmap.ic_launcher)!!
                                    val photoFile = chatResult.photo?.small
                                    if (photoFile?.local?.isDownloadingCompleted == true) {
                                        val filePath = photoFile.local.path
                                        val file = File(filePath)
                                        if (file.exists()) {
                                            // 这里可以处理图片文件，例如显示或使用
                                            loadBitmapFromUri(context.contentResolver, Uri.fromFile(file))?.let {
                                                bmp = it
                                            }
                                        }
                                    } else {
                                        // 使用默认图标
                                        bmp = generateChatTitleIconBitmap(
                                            context,
                                            chatResult.title,
                                            chatResult.accentColorId
                                        )
                                    }
                                    context.sendChatMessageNotification(
                                        title = chatTitle,
                                        message = handleAllPushMessages(content),
                                        senderName = senderName,
                                        conversationId = chatId.toString(),
                                        timestamp = notification.date * 1000L,
                                        isGroupChat = isGroup,
                                        chatIconBitmap = bmp // 这里可以传入群组图标的 Uri
                                    )
                                } else {
                                    val bmp = generateChatTitleIconBitmap(
                                        context,
                                        chatResult.title,
                                        chatResult.accentColorId
                                    )
                                    val isGroup = senderId == chatId
                                    context.sendChatMessageNotification(
                                        title = senderName,
                                        message = handleAllPushMessages(content),
                                        senderName = senderName,
                                        conversationId = chatId.toString(),
                                        timestamp = notification.date * 1000L,
                                        isGroupChat = isGroup,
                                        chatIconBitmap = bmp // 这里可以传入群组图标的 Uri
                                    )
                                }
                            } catch (e: Exception) {
                                println("handleNotification failed: ${e.message}")
                            }
                        }
                    }
                }
            }
        }
    }

    // 处理消息变动通知
    /*fun handleNotification(update: TdApi.UpdateNotification) {
        println("Received notification: $update")
        val notification = update.notification
        when (val type = notification.type) {
            is TdApi.NotificationTypeNewMessage -> {
                val message = type.message
                val chatId = message.chatId
                // 异步获取聊天标题和聊天信息
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val chatResult = getChat(chatId)
                        if (chatResult.constructor == TdApi.Chat.CONSTRUCTOR) {
                            // 判断是否是群组
                            var isGroup = false
                            when (chatResult.type) {
                                is TdApi.ChatTypeSupergroup -> {
                                    isGroup = true
                                }
                                is TdApi.ChatTypeBasicGroup -> {
                                    isGroup = true
                                }
                            }

                            // 获取聊天图片
                            var bmp = drawableToBitmap(context, R.mipmap.ic_launcher)!!
                            val photoFile = chatResult.photo?.small
                            if (photoFile?.local?.isDownloadingCompleted == true) {
                                val filePath = photoFile.local.path
                                val file = File(filePath)
                                if (file.exists()) {
                                    // 这里可以处理图片文件，例如显示或使用
                                    loadBitmapFromUri(context.contentResolver, Uri.fromFile(file))?.let {
                                        bmp = it
                                    }
                                }
                            } else {
                                // 使用默认图标
                                bmp = generateChatTitleIconBitmap(
                                    context,
                                    chatResult.title,
                                    chatResult.accentColorId
                                )
                            }

                            //val accentColorId = chatResult.accentColorId
                            val needNotification = chatResult.notificationSettings.muteFor == 0
                            val chatTitle = chatResult.title

                            // 获取发送者名称
                            var senderName = chatTitle
                            if (isGroup) {
                                when (val senderId = message.senderId) {
                                    is TdApi.MessageSenderUser -> {
                                        val userId = senderId.userId
                                        val userResult = sendRequest(TdApi.GetUser(userId))
                                        if (userResult is TdApi.User) {
                                            senderName = "${userResult.firstName} ${userResult.lastName}"
                                        }
                                    }
                                    is TdApi.MessageSenderChat -> {
                                        // 处理群组消息的发送者
                                        if (senderId.chatId == chatId) {
                                            senderName = chatTitle
                                        } else {
                                            val itChat = tgApi?.getChat(senderId.chatId)
                                            itChat.let {
                                                senderName = it!!.title
                                            }
                                        }
                                    }
                                }
                            }

                            if (needNotification) {
                                context.sendChatMessageNotification(
                                    title = chatTitle,
                                    message = handleAllMessages(message),
                                    senderName = senderName,
                                    conversationId = chatId.toString(),
                                    timestamp = message.date * 1000L,
                                    isGroupChat = isGroup,
                                    chatIconBitmap = bmp // 这里可以传入群组图标的 Uri
                                )
                            }
                        }
                    } catch (e: Exception) {
                        println("handleNotification failed: ${e.message}")
                    }
                }
            }
            is TdApi.NotificationTypeNewPushMessage -> {
                println("Received push message: $type")
                val content = type.content
                val senderName = type.senderName
                val senderId = type.senderId
                val chatId = when (senderId) {
                    is TdApi.MessageSenderUser -> senderId.userId
                    is TdApi.MessageSenderChat -> senderId.chatId
                    else -> 0
                }

                if (!type.isOutgoing) {
                    // 异步获取聊天标题和聊天信息
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val chatResult = getChat(chatId)
                            if (chatResult.constructor == TdApi.Chat.CONSTRUCTOR) {
                                // 获取群组名字
                                val chatTitle = chatResult.title

                                // 判断是否是群组
                                var isGroup = false
                                when (chatResult.type) {
                                    is TdApi.ChatTypeSupergroup -> {
                                        isGroup = true
                                    }
                                    is TdApi.ChatTypeBasicGroup -> {
                                        isGroup = true
                                    }
                                }

                                // 获取聊天图片
                                var bmp = drawableToBitmap(context, R.mipmap.ic_launcher)!!
                                val photoFile = chatResult.photo?.small
                                if (photoFile?.local?.isDownloadingCompleted == true) {
                                    val filePath = photoFile.local.path
                                    val file = File(filePath)
                                    if (file.exists()) {
                                        // 这里可以处理图片文件，例如显示或使用
                                        loadBitmapFromUri(context.contentResolver, Uri.fromFile(file))?.let {
                                            bmp = it
                                        }
                                    }
                                } else {
                                    // 使用默认图标
                                    bmp = generateChatTitleIconBitmap(
                                        context,
                                        chatResult.title,
                                        chatResult.accentColorId
                                    )
                                }
                                context.sendChatMessageNotification(
                                    title = chatTitle,
                                    message = handleAllPushMessages(content),
                                    senderName = senderName,
                                    conversationId = chatId.toString(),
                                    timestamp = notification.date * 1000L,
                                    isGroupChat = isGroup,
                                    chatIconBitmap = bmp // 这里可以传入群组图标的 Uri
                                )
                            } else {
                                val bmp = generateChatTitleIconBitmap(
                                    context,
                                    chatResult.title,
                                    chatResult.accentColorId
                                )
                                context.sendChatMessageNotification(
                                    title = senderName,
                                    message = handleAllPushMessages(content),
                                    senderName = senderName,
                                    conversationId = chatId.toString(),
                                    timestamp = notification.date * 1000L,
                                    chatIconBitmap = bmp // 这里可以传入群组图标的 Uri
                                )
                            }
                        } catch (e: Exception) {
                            println("handleNotification failed: ${e.message}")
                        }
                    }
                }
            }
        }
    }*/

    // 获取聊天信息
    suspend fun getChat(chatId: Long): TdApi.Chat {
        // 先从缓存中取
        chatCache[chatId]?.let {
            return it
        }
        // 缓存没有，再发请求
        val chatResult = sendRequest(TdApi.GetChat(chatId))
        // 放入缓存
        chatCache[chatId] = chatResult
        return chatResult
    }

    // 接受通话
    fun acceptCall(callId: Int) {
        client.send(TdApi.AcceptCall(callId, VoIP.getProtocol())) { result ->
            if (result is TdApi.Ok) {
                println("Call accepted successfully")
            } else {
                println("Failed to accept call: $result")
            }
        }
    }

    // 拒绝通话
    fun discardCall(nowCallItem: TdApi.Call, isDisconnected: Boolean = false) {
        if (callItem != null) {
            client.send(TdApi.DiscardCall(nowCallItem.id, isDisconnected, voipItem?.callDuration?.toInt() ?: 0, false, voipItem?.connectionId ?: 0)) { result ->
                if (result is TdApi.Ok) {
                    voipItem?.performDestroy()
                    println("Call discarded successfully")
                } else {
                    println("Failed to discard call: $result")
                }
            }
        }
    }

    // 处理电话通知
    fun handleCallUpdate(update: TdApi.UpdateCall) {
        val call = update.call
        callItem = call
        println("Call update: $call")
        when (val state = call.state) {
            is TdApi.CallStateReady -> {
                // 呼叫已就绪
                println("Call is ready")
                onCallback[call.userId]?.invoke(call, state.emojis.joinToString(""))
                val stateListener = object : ConnectionStateListener {
                    override fun onSignallingDataEmitted(data: ByteArray?) {
                        if (data == null) return
                        println("Send signaling packet, length ${data.size}")
                        client.send(
                            TdApi.SendCallSignalingData(call.id, data)
                        ) { result -> println("SendCallSignalingData: $result") }
                    }

                    override fun onConnectionStateChanged(context: VoIPInstance, @CallState newState: Int) {
                        // newState 会是下面这些常量之一：
                        //  CallState.PENDING, EXCHANGING_KEYS, READY, HANGING_UP, DISCARDED, ERROR
                        if (newState == CallState.RECONNECTING) onCallback[call.userId]?.invoke(callItem!!, null)
                        println("VoIP connection status changes: $newState")
                    }

                    override fun onRemoteMediaStateChanged(
                        context: VoIPInstance,
                        @AudioState audioState: Int,
                        @VideoState videoState: Int
                    ) {
                        // audioState: 0=ENDED, 1=PAUSED, 2=PLAYING
                        println("Remote media status: audio=$audioState, video=$videoState")
                    }

                    override fun onStopped(
                        releasedContext: VoIPInstance,
                        finalStats: NetworkStats,
                        debugLog: String?
                    ) {
                        println("VoIP stopped：$finalStats")
                        debugLog?.let { Log.d("VoIP", "VoIP debug log: $it") }
                    }
                }

                voipItem = VoIP.instantiateAndConnect(
                    call,
                    call.state as TdApi.CallStateReady?,
                    stateListener,
                    false,
                    null,
                    getNetworkType(context),
                    true,
                    1,
                    false
                )
            }
            is TdApi.CallStatePending -> {
                // 被呼叫在等待
                println("Call is pending")
                onCallback[call.userId]?.invoke(call, null)
                // 异步获取聊天标题和聊天信息
                CoroutineScope(Dispatchers.IO).launch {
                    val chatResult = getChat(call.userId)

                    // 获取聊天头像
                    var bmp = drawableToBitmap(context, R.mipmap.ic_launcher)!!
                    val photoFile = chatResult.photo?.small
                    if (photoFile?.local?.isDownloadingCompleted == true) {
                        val filePath = photoFile.local.path
                        val file = File(filePath)
                        if (file.exists()) {
                            // 这里可以处理图片文件，例如显示或使用
                            loadBitmapFromUri(context.contentResolver, Uri.fromFile(file))?.let {
                                bmp = it
                            }
                        }
                    } else {
                        // 使用默认图标
                        bmp = generateChatTitleIconBitmap(
                            context,
                            chatResult.title,
                            chatResult.accentColorId
                        )
                    }

                    context.showIncomingCallNotification(
                        callerName = chatResult.title ?: "Unknown",
                        targetActivity = VoiceCallActivity::class.java,
                        notificationId = 1001,
                        callId = call.id,
                        chatIconBitmap = bmp
                    )

                    withContext(Dispatchers.Main) {
                        val serviceIntent = Intent(context, StartVoiceCallActivityForegroundService::class.java)
                        serviceIntent.putExtra("callerName", chatResult.title ?: "Unknown")
                        serviceIntent.putExtra("callId", call.id)
                        serviceIntent.putExtra("chatIconBitmap", bmp)
                        ContextCompat.startForegroundService(context, serviceIntent)
                    }
                }
            }
            is TdApi.CallStateExchangingKeys -> {
                // 正在交换密钥
                println("Exchanging keys")
                onCallback[call.userId]?.invoke(call, null)
                //voipItem?.initializeAndConnect()
            }
            is TdApi.CallStateHangingUp -> {
                // 呼叫已挂断
                println("Call is hanging up")
                onCallback[call.userId]?.invoke(call, null)
                voipItem?.performDestroy()
                tgApi?.onCallback?.remove(callItem?.userId)
                voipItem = null
                callItem = null
                close()
            }
            is TdApi.CallStateDiscarded -> {
                // 呼叫已结束
                println("Call is discarded")
                onCallback[call.userId]?.invoke(call, null)
                tgApi?.onCallback?.remove(callItem?.userId)
                voipItem?.performDestroy()
                voipItem = null
                callItem = null
                close()
            }
            else -> {
                // 其他状态
                onCallback[call.userId]?.invoke(call, null)
                println("Call state: ${call.state}")
            }
        }
    }

    // 接收电话信令包
    fun handleNewCallSignalingDataUpdate(update: TdApi.UpdateNewCallSignalingData) {
        val data = update.data
        //val call = update.callId
        println("New call signaling data: $data")

        voipItem?.handleIncomingSignalingData(data)
        /*client.send(
            TdApi.SendCallSignalingData(call, data)
        ) { result -> println("SendCallSignalingData: $result") }*/
    }

    // 处理授权状态更新
    private fun handleAuthorizationState(update: TdApi.UpdateAuthorizationState) {
        val authorizationState = update.authorizationState
        when (authorizationState.constructor) {
            TdApi.AuthorizationStateReady.CONSTRUCTOR -> {
                println("TgApi: Authorization Ready")
                isAuthorized = true
                authLatch.countDown()
            }

            TdApi.AuthorizationStateClosed.CONSTRUCTOR -> {
                println("TgApi: Authorization Closed")
                isAuthorized = false
                authLatch.countDown()
            }

            TdApi.AuthorizationStateWaitPhoneNumber.CONSTRUCTOR -> {
                println("TgApi: Waiting for Phone Number")
                isAuthorized = false
                authLatch.countDown()
            }

            else -> {
                // 其他授权状态处理
            }
        }
    }

    // 处理获取到的新消息
    private fun handleNewMessage(update: TdApi.UpdateNewMessage) {
        val message = update.message
        val chatId = message.chatId

        if ((message.senderId as TdApi.MessageSenderUser).userId == userId.toLong()) {
            return // 如果消息是自己发送的，则不处理
        }
        // 异步获取聊天标题和聊天信息
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val chatResult = getChat(chatId)
                if (chatResult.constructor == TdApi.Chat.CONSTRUCTOR) {

                    // 判断是否是群组
                    var isGroup = false
                    when (chatResult.type) {
                        is TdApi.ChatTypeSupergroup -> {
                            isGroup = true
                        }
                        is TdApi.ChatTypeBasicGroup -> {
                            isGroup = true
                        }
                    }

                    // 获取聊天图片
                    var bmp = drawableToBitmap(context, R.mipmap.ic_launcher)!!
                    val photoFile = chatResult.photo?.small
                    if (photoFile?.local?.isDownloadingCompleted == true) {
                        val filePath = photoFile.local.path
                        val file = File(filePath)
                        if (file.exists()) {
                            // 这里可以处理图片文件，例如显示或使用
                            loadBitmapFromUri(context.contentResolver, Uri.fromFile(file))?.let {
                                bmp = it
                            }
                        }
                    } else {
                        // 使用默认图标
                        bmp = generateChatTitleIconBitmap(
                            context,
                            chatResult.title,
                            chatResult.accentColorId
                        )
                    }

                    //val accentColorId = chatResult.accentColorId
                    val needNotification = chatResult.notificationSettings.muteFor == 0
                    val chatTitle = chatResult.title

                    // 获取发送者名称
                    var senderName = chatTitle
                    if (isGroup) {
                        when (val senderId = message.senderId) {
                            is TdApi.MessageSenderUser -> {
                                val userId = senderId.userId
                                val userResult = sendRequest(TdApi.GetUser(userId))
                                if (userResult is TdApi.User) {
                                    senderName = "${userResult.firstName} ${userResult.lastName}"
                                }
                            }
                            is TdApi.MessageSenderChat -> {
                                // 处理群组消息的发送者
                                if (senderId.chatId == chatId) {
                                    senderName = chatTitle
                                } else {
                                    val itChat = tgApi?.getChat(senderId.chatId)
                                    itChat.let {
                                        senderName = it!!.title
                                    }
                                }
                            }
                        }
                    }

                    if (needNotification) {
                        context.sendChatMessageNotification(
                            title = chatTitle,
                            message = handleAllMessages(message),
                            senderName = senderName,
                            conversationId = chatId.toString(),
                            timestamp = message.date * 1000L,
                            isGroupChat = isGroup,
                            chatIconBitmap = bmp // 这里可以传入群组图标的 Uri
                        )
                    }
                }
            } catch (e: Exception) {
                println("HandleNewChat failed: ${e.message}")
            }
        }
    }

    private fun loadBitmapFromUri(contentResolver: ContentResolver, uri: Uri): Bitmap? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else {
                contentResolver.openInputStream(uri)?.use { inputStream ->
                    BitmapFactory.decodeStream(inputStream)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    // 标记已读
    fun markMessagesAsRead(chatId: Long, forceRead: Boolean = true) {
        // 异步执行
        CoroutineScope(Dispatchers.IO).launch {
            // 获取消息 ID
            try {
                val chatResult = getChat(chatId)
                val messageId = chatResult.lastMessage?.id

                if (chatResult.constructor == TdApi.Chat.CONSTRUCTOR) {
                    // 创建 ViewMessages 请求
                    val viewMessagesRequest = messageId?.let {
                        TdApi.ViewMessages(
                            chatId,
                            longArrayOf(it),
                            null,
                            forceRead
                        )
                    }

                    // 发送 ViewMessages 请求
                    client.send(viewMessagesRequest) { response ->
                        if (response is TdApi.Ok) {
                            println("Messages successfully marked as read in chat ID $chatId")
                        } else {
                            println("Failed to mark messages as read: $response")
                        }
                    }
                }
            } catch (e: Exception) {
                println("HandleNewChat failed: ${e.message}")
            }
        }
    }

    // 发送消息
    fun sendMessage(chatId: Long, message: TdApi.InputMessageContent, replyTo: TdApi.InputMessageReplyTo? = null) {
        val message = TdApi.SendMessage().apply {
            this.chatId = chatId
            this.replyTo = replyTo
            inputMessageContent = message
        }
        client.send(message) { result ->
            //println("SendMessage result: $result")
            if (result.constructor == TdApi.Error.CONSTRUCTOR) {
                val error = result as TdApi.Error
                println("Send Message Error: ${error.message}")
            } else {
                println("Message sent successfully")
            }
        }
    }

    // 处理和简化消息
    fun handleAllMessages(
        message: TdApi.Message? = null,
        messageContext: TdApi.MessageContent? = null,
        maxText: Int = 64
    ): String {
        val content: TdApi.MessageContent = messageContext ?: message?.content
        ?: return context.getString(R.string.Unknown_Message)

        return when (content) {
            is TdApi.MessageText -> {
                val text = content.text.text.replace('\n', ' ')
                if (text.length > maxText) text.take(maxText) + "..." else text
            }
            is TdApi.MessagePhoto -> {
                val caption = content.caption.text.replace('\n', ' ')
                val text = context.getString(R.string.Photo) + " " + caption
                if (text.length > maxText) text.take(maxText) + "..." else text
            }
            is TdApi.MessageVideo -> {
                val caption = content.caption.text.replace('\n', ' ')
                val text = context.getString(R.string.Video) + " " + caption
                if (text.length > maxText) text.take(maxText) + "..." else text
            }
            is TdApi.MessageVoiceNote -> {
                val caption = content.caption.text.replace('\n', ' ')
                val text = context.getString(R.string.Voice) + " " + caption
                if (text.length > maxText) text.take(maxText) + "..." else text
            }
            is TdApi.MessageAudio -> {
                val audioName = content.audio.fileName
                    .ifBlank {
                        listOf(content.audio.performer, content.audio.title)
                            .filter { it.isNotBlank() }
                            .joinToString(" ")
                    }
                    .replace('\n', ' ')
                val text = context.getString(R.string.Audio) + " " + audioName
                if (text.length > maxText) text.take(maxText) + "..." else text
            }
            is TdApi.MessageAnimation -> {
                val caption = content.caption.text.replace('\n', ' ')
                val text = context.getString(R.string.Animation) + " " + caption
                if (text.length > maxText) text.take(maxText) + "..." else text
            }
            is TdApi.MessageDocument -> {
                val caption = content.document.fileName.replace('\n', ' ') + content.caption.text.replace('\n', ' ')
                val text = context.getString(R.string.File) + " " + caption
                if (text.length > maxText) text.take(maxText) + "..." else text
            }
            is TdApi.MessageAnimatedEmoji -> {
                content.emoji.ifEmpty { context.getString(R.string.Unknown_Message) }
            }
            is TdApi.MessageSticker -> {
                content.sticker.emoji.ifEmpty { context.getString(R.string.Unknown_Message) }
            }
            is TdApi.MessageCall -> {
                 when (content.discardReason) {
                    is TdApi.CallDiscardReasonMissed -> context.getString(R.string.Missed_call)
                    is TdApi.CallDiscardReasonDeclined -> context.getString(R.string.Declined_call)
                    is TdApi.CallDiscardReasonDisconnected -> context.getString(R.string.Disconnected_client)
                    is TdApi.CallDiscardReasonEmpty -> context.getString(R.string.Failed_call)
                    is TdApi.CallDiscardReasonHungUp -> context.getString(R.string.Hung_up)
                    else -> context.getString(R.string.Call)
                }
            }
            else -> context.getString(R.string.Unknown_Message)
        }
    }

    fun handleAllPushMessages(content: TdApi.PushMessageContent): String {
        fun limit(text: String, max: Int = 64): String {
            val singleLine = text.replace('\n', ' ')
            return if (singleLine.length > max) singleLine.take(max) + "..." else singleLine
        }

        return when (content) {
            is TdApi.PushMessageContentText -> limit(content.text)
            is TdApi.PushMessageContentPhoto -> context.getString(R.string.Photo) + " " + limit(content.caption)
            is TdApi.PushMessageContentVoiceNote -> context.getString(R.string.Voice)
            is TdApi.PushMessageContentVideo -> context.getString(R.string.Video) + " " + limit(content.caption)
            is TdApi.PushMessageContentAnimation -> context.getString(R.string.Animation) + " " + limit(content.caption)
            is TdApi.PushMessageContentSticker -> content.emoji.ifEmpty { context.getString(R.string.Unknown_Message) }
            is TdApi.PushMessageContentDocument -> context.getString(R.string.File)
            else -> context.getString(R.string.Unknown_Message)
        }
    }

    // 获取FCM接受到的消息的相应账号
    fun getPushReceiverId(payload: String, callback: (Long) -> Unit) {
        client.send(TdApi.GetPushReceiverId(payload)) { receiverId ->
            if (receiverId is TdApi.PushReceiverId) {
                callback(receiverId.id)
            }
        }
    }

    // 处理加密消息
    fun processPushNotification(payload: String) {
        client.send(TdApi.ProcessPushNotification(payload)) {
            println("ProcessPushNotification result: $it")
        }
    }

    // 发送请求并返回结果
    private suspend fun <R : TdApi.Object> sendRequest(
        request: TdApi.Function<R>,
        retryCount: Int = 3 // 重试次数限制
    ): R = withContext(Dispatchers.IO) {
        val result = CompletableDeferred<R>()
        client.send(request) { response ->
            when (response) {
                is TdApi.Error -> {
                    if (response.code == 404) {
                        // 错误码是 404，直接抛出异常
                        result.completeExceptionally(
                            Exception("TDLib error 404: ${response.message}")
                        )
                    } else if (retryCount > 0) {
                        // 错误码不是 404，并且还可以重试，递归调用 sendRequest
                        launch {
                            try {
                                val retryResult = sendRequest(request, retryCount - 1)
                                result.complete(retryResult)
                            } catch (e: Exception) {
                                result.completeExceptionally(e)
                            }
                        }
                    } else {
                        // 超过重试次数，抛出异常
                        result.completeExceptionally(
                            Exception("TDLib error: ${response.message}")
                        )
                    }
                }

                else -> {
                    // 成功时，完成请求
                    @Suppress("UNCHECKED_CAST")
                    result.complete(response as R)
                }
            }
        }
        return@withContext result.await()
    }

    // 关闭连接
    fun close(): Boolean {
        if (callItem == null) {
            println("Closing TgApiForPushNotification client")
            client.send(TdApi.Close()) {}
            return true
        } else {
            return false
        }
    }

    // 协程-关闭连接
    suspend fun closeSuspend() {
        println("Closing TgApiForPushNotification client on suspend")
        sendRequest(TdApi.Close())
    }
}
