/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.gohj99.tgwear.ui.chat

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaRecorder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.wear.compose.foundation.pager.PagerState
import com.gohj99.tgwear.R
import com.gohj99.tgwear.TgApiManager.tgApi
import com.gohj99.tgwear.ui.AutoScrollingText
import com.gohj99.tgwear.ui.InputBar
import com.gohj99.tgwear.ui.TextDropdown
import com.gohj99.tgwear.ui.main.MainCard
import com.gohj99.tgwear.ui.main.MessageView
import com.gohj99.tgwear.utils.telegram.editMessageText
import com.gohj99.tgwear.utils.telegram.handleAllMessages
import com.gohj99.tgwear.utils.telegram.sendMessage
import com.gohj99.tgwear.utils.waveformTo5bit
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.drinkless.tdlib.TdApi
import java.io.File

@Composable
fun SendMessageCompose(
    chatId: Long,
    inputText: MutableState<String>,
    planReplyMessage: MutableState<TdApi.Message?>,
    planReplyMessageSenderName: String,
    currentUserId: MutableState<Long>,
    planEditMessage: MutableState<TdApi.Message?>,
    planEditMessageText: MutableState<String>,
    listState: LazyListState,
    pagerState: PagerState,
    showUnknownMessageType: Boolean,
    chatTopics: Map<Long, String>,
    onLinkClick: (String) -> Unit,
    selectTopicId: MutableState<Long>
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var audioWave: ByteArray? = null

    // --- 状态变量定义 ---
    var recordMode by remember { mutableStateOf(false) } // 是否进入了录音/预览界面
    var recordingFile by remember { mutableStateOf<File?>(null) }

    // 录音状态
    var isRecording by remember { mutableStateOf(false) } // 正在录音(包括暂停)
    var isRecordingPaused by remember { mutableStateOf(false) } // 录音暂停中

    // 预览状态 (点击停止录制后)
    var isPreviewMode by remember { mutableStateOf(false) } // 录制完成，等待发送或播放
    var isPlayingPreview by remember { mutableStateOf(false) } // 正在播放录音预览

    // 时间相关
    var recordDurationMs by remember { mutableLongStateOf(0L) } // 录制总时长(毫秒)
    var playbackPositionMs by remember { mutableLongStateOf(0L) } // 播放进度(毫秒)
    var timerText by remember { mutableStateOf("00:00") }

    // --- MediaRecorder (录音) ---
    val recorderRef = remember { mutableStateOf<MediaRecorder?>(null) }

    // --- ExoPlayer (播放预览) ---
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            // 添加监听器，处理播放结束逻辑
            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    if (state == Player.STATE_ENDED) {
                        // 播放结束
                        isPlayingPreview = false
                        seekTo(0)
                        pause()
                        playbackPositionMs = 0
                    }
                }
            })
        }
    }

    // 辅助格式化时间函数
    fun formatTime(ms: Long): String {
        val seconds = ms / 1000
        return String.format("%02d:%02d", seconds / 60, seconds % 60)
    }

    // --- 计时器逻辑 (录音时) ---
    // 核心思路：startTime 记录当前片段开始时间
    var startTime by remember { mutableLongStateOf(0L) }

    LaunchedEffect(isRecording, isRecordingPaused) {
        if (isRecording && !isRecordingPaused) {
            // 如果刚开始或者刚从暂停恢复，校准 startTime
            startTime = System.currentTimeMillis() - recordDurationMs
            while (isRecording && !isRecordingPaused) {
                recordDurationMs = System.currentTimeMillis() - startTime
                timerText = formatTime(recordDurationMs)
                delay(100)
            }
        }
    }

    // --- 计时器逻辑 (预览播放时) ---
    LaunchedEffect(isPlayingPreview) {
        if (isPlayingPreview) {
            while (isPlayingPreview) {
                playbackPositionMs = exoPlayer.currentPosition
                timerText = "${formatTime(playbackPositionMs)} | ${formatTime(recordDurationMs)}"
                delay(100)
            }
        } else if (isPreviewMode) {
            // 停止播放时，重置显示为 "00:00 | 总时长"
            // 注意：当 seekTo(0) 后 currentPosition 也会变回 0
            timerText = "${formatTime(playbackPositionMs)} | ${formatTime(recordDurationMs)}"
        }
    }

    // --- 资源清理 ---
    DisposableEffect(Unit) {
        onDispose {
            try {
                recorderRef.value?.stop()
                recorderRef.value?.release()
            } catch (e: Exception) {}
            recorderRef.value = null

            // 释放 ExoPlayer
            exoPlayer.release()
        }
    }

    // --- 功能函数 ---

    fun startRecording() {
        val cacheDir = context.externalCacheDir ?: context.cacheDir
        val file = File(cacheDir, "Record_${System.currentTimeMillis()}.m4a")
        recordingFile = file

        try {
            val recorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(file.absolutePath)
                setAudioEncodingBitRate(128000) // 128kbps
                setAudioSamplingRate(44100)     // 44100Hz
                prepare()
                start()
            }
            recorderRef.value = recorder

            // 重置状态
            recordDurationMs = 0L
            startTime = System.currentTimeMillis()
            isRecording = true
            isRecordingPaused = false
            isPreviewMode = false
            recordMode = true
        } catch (e: Exception) {
            e.printStackTrace()
            isRecording = false
            recordMode = false
        }
    }

    fun pauseRecording() {
        try {
            recorderRef.value?.pause()
            isRecordingPaused = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun resumeRecording() {
        try {
            recorderRef.value?.resume()
            isRecordingPaused = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stopRecordingAndPreview() {
        try {
            recorderRef.value?.stop()
        } catch (e: Exception) {
            // 防止录音时间太短崩溃
        } finally {
            recorderRef.value?.release()
            recorderRef.value = null
            isRecording = false
            isRecordingPaused = false
            isPreviewMode = true // 进入预览模式
            timerText = "00:00 | ${formatTime(recordDurationMs)}"

            // 准备 ExoPlayer
            recordingFile?.let { file ->
                if (file.exists()) {
                    exoPlayer.setMediaItem(MediaItem.fromUri(file.toUri()))
                    exoPlayer.prepare()
                }

                // 进入协程计算音频时长和波形比特信息
                coroutineScope.launch {
                    //recordDurationMs = exoPlayer.duration
                    audioWave = waveformTo5bit(file)
                }
            }
        }
    }

    fun togglePreviewPlayback() {
        if (recordingFile == null || !recordingFile!!.exists()) return

        if (isPlayingPreview) {
            // 暂停播放
            exoPlayer.pause()
            isPlayingPreview = false
        } else {
            // 开始/继续播放
            if (exoPlayer.playbackState == Player.STATE_ENDED) {
                exoPlayer.seekTo(0)
            }
            exoPlayer.play()
            isPlayingPreview = true
        }
    }

    fun cancelAll() {
        // 停止录音
        try { recorderRef.value?.stop(); recorderRef.value?.release() } catch (e: Exception){}
        recorderRef.value = null

        // 停止播放
        exoPlayer.stop()
        exoPlayer.clearMediaItems()

        isRecording = false
        isPreviewMode = false
        isPlayingPreview = false
        recordMode = false
        //recordingFile?.delete()
        timerText = "00:00"
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // 授权后逻辑，可选：直接开始录音
        }
    }

    // --- UI渲染 ---

    if (planEditMessage.value != null) {
        // ... (保持原样: 编辑模式UI)
        Box (
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    onClick = {
                        planReplyMessage.value = null
                        tgApi!!.replyMessage.value = null
                    }
                )
        )
        Text(
            text = stringResource(R.string.Edit),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier
                .padding(
                    start = 10.dp,
                    end = 5.dp,
                    top = 5.dp
                )
                .fillMaxWidth()
                .clickable(
                    onClick = {
                        planEditMessage.value = null
                    }
                )
        )
    } else if (planReplyMessage.value != null) {
        // ... (保持原样: 回复模式UI)
        // 将回复消息显示
        Box (
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    onClick = {
                        planReplyMessage.value = null
                        tgApi!!.replyMessage.value = null
                    }
                )
        )
        Text(
            text = stringResource(R.string.Reply),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier
                .padding(
                    start = 10.dp,
                    end = 5.dp,
                    top = 5.dp
                )
                .fillMaxWidth()
        )
        var parentHeight by remember { mutableIntStateOf(0) }
        val stateDownloadDone = rememberSaveable { mutableStateOf(false) }
        val stateDownload = rememberSaveable { mutableStateOf(false) }

        Row(
            modifier = Modifier
                .padding(
                    start = 5.dp,
                    end = 5.dp,
                    top = 5.dp
                )
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Row(
                modifier = Modifier
                    .background(
                        Color(0xFF3A4048),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Box(
                    modifier = Modifier
                        .background(Color(0xFF397DBC))
                        .width(8.dp) // 指定左边颜色宽度为 10.dp
                ) {
                    Spacer(Modifier.height((parentHeight/2).dp))
                }
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .onSizeChanged { size ->
                            parentHeight =
                                size.height // 获取父容器的高度
                        }
                ) {
                    Column(
                        modifier = Modifier
                            .padding(
                                bottom = 5.dp,
                                start = 5.dp,
                                end = 5.dp
                            )
                    ) {
                        if (planReplyMessageSenderName == "") {
                            messageDrawer(
                                content = planReplyMessage.value!!.content,
                                onLinkClick = onLinkClick,
                                textColor = Color(0xFFFEFEFE),
                                stateDownload = stateDownload,
                                stateDownloadDone = stateDownloadDone,
                                showUnknownMessageType = showUnknownMessageType
                            )
                        } else {
                            // 用户名
                            AutoScrollingText(
                                text = planReplyMessageSenderName,
                                color = Color(0xFF66D3FE),
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier
                            )
                            messageDrawer(
                                content = planReplyMessage.value!!.content,
                                onLinkClick = onLinkClick,
                                textColor = Color(0xFFFEFEFE),
                                stateDownload = stateDownload,
                                stateDownloadDone = stateDownloadDone,
                                showUnknownMessageType = showUnknownMessageType
                            )
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }

    if (planEditMessage.value != null) {
        // ... (保持原样: 编辑输入框)
        InputBar(
            query = planEditMessageText.value,
            onQueryChange = { planEditMessageText.value = it },
            placeholder = stringResource(id = R.string.Write_message),
        )
        Spacer(modifier = Modifier.height(8.dp))
        // 完成编辑消息按钮
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .padding(end = 10.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween // 左右对齐
            ) {
                // 换行按钮
                IconButton(
                    onClick = {
                        planEditMessageText.value += "\n"
                    },
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .size(45.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.enter_icon),
                        contentDescription = null,
                        modifier = Modifier.size(45.dp)
                    )
                }

                // 完成编辑按钮
                IconButton(
                    onClick = {
                        if (planEditMessage.value != null) {
                            tgApi?.editMessageText(
                                chatId = chatId,
                                messageId = planEditMessage.value!!.id,
                                message = TdApi.InputMessageText().apply {
                                    text = TdApi.FormattedText().apply {
                                        this.text = planEditMessageText.value
                                    }
                                }
                            )
                        }
                        planEditMessage.value = null
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(0)
                            listState.animateScrollToItem(0)
                        }
                    },
                    modifier = Modifier
                        .size(45.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.done_icon),
                        contentDescription = null,
                        modifier = Modifier.size(45.dp)
                    )
                }
            }
        }
    } else if (recordMode) {
        // --- 录音/预览模式 UI ---

        // 1. 时长显示 (居中)
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = timerText,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 2. 按钮区域
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .padding(end = 10.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 左按钮：取消/删除
                IconButton(
                    onClick = {
                        cancelAll()
                    },
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .size(45.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_cancel),
                        contentDescription = null,
                        modifier = Modifier.size(45.dp)
                    )
                }

                // 中按钮：录制时(暂停/继续)，预览时(播放/暂停)
                IconButton(
                    onClick = {
                        if (isRecording) {
                            // 录制阶段：控制暂停
                            if (isRecordingPaused) resumeRecording() else pauseRecording()
                        } else if (isPreviewMode) {
                            // 预览阶段：控制播放 (使用 ExoPlayer)
                            togglePreviewPlayback()
                        }
                    },
                    modifier = Modifier.size(50.dp)
                ) {
                    val iconId = if (isRecording) {
                        // 录制阶段
                        if (isRecordingPaused) R.drawable.ic_play else R.drawable.ic_playing
                    } else {
                        // 预览阶段
                        if (isPlayingPreview) R.drawable.ic_playing else R.drawable.ic_play
                    }

                    Image(
                        painter = painterResource(id = iconId),
                        contentDescription = null,
                        modifier = Modifier.size(50.dp)
                    )
                }

                // 右按钮：录制时(完成/停止)，预览时(发送)
                IconButton(
                    onClick = {
                        if (isRecording) {
                            // 正在录制 -> 点击变停止(进入预览)
                            stopRecordingAndPreview()
                        } else if (isPreviewMode) {
                            // 正在预览 -> 点击发送
                            exoPlayer.pause() // 发送前停止播放

                            val finalDuration = if (recordDurationMs < 1000) 1 else (recordDurationMs / 1000).toInt()

                            if (planReplyMessage.value == null) {
                                tgApi?.sendMessage(
                                    chatId = chatId,
                                    message = TdApi.InputMessageVoiceNote().apply {
                                        duration = finalDuration
                                        voiceNote = TdApi.InputFileLocal().apply {
                                            this.path = recordingFile?.absolutePath
                                        }
                                        waveform = audioWave
                                    },
                                    messageThreadId = selectTopicId.value
                                )
                            } else {
                                val replyToMsg = planReplyMessage.value!!
                                val replyToInput = if (replyToMsg.chatId != chatId) {
                                    TdApi.InputMessageReplyToExternalMessage(
                                        replyToMsg.chatId, replyToMsg.id, null
                                    )
                                } else {
                                    TdApi.InputMessageReplyToMessage(replyToMsg.id, null)
                                }

                                tgApi?.sendMessage(
                                    chatId = chatId,
                                    message = TdApi.InputMessageVoiceNote().apply {
                                        duration = finalDuration
                                        voiceNote = TdApi.InputFileLocal().apply {
                                            this.path = recordingFile?.absolutePath
                                        }
                                        waveform = audioWave
                                    },
                                    replyTo = replyToInput,
                                    messageThreadId = selectTopicId.value
                                )
                                planReplyMessage.value = null
                                tgApi!!.replyMessage.value = null
                            }

                            inputText.value = ""
                            cancelAll() // 重置所有状态

                            coroutineScope.launch {
                                pagerState.animateScrollToPage(0)
                                listState.animateScrollToItem(0)
                            }
                        }
                    },
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(45.dp)
                ) {
                    // 录制阶段显示"停止录制"图标，预览阶段显示"发送"图标
                    val iconId = if (isRecording) R.drawable.ic_record_stop else R.drawable.ic_custom_send
                    Image(
                        painter = painterResource(id = iconId),
                        contentDescription = null,
                        modifier = Modifier.size(45.dp)
                    )
                }
            }
        }
    } else {
        // ... (保持原样: 普通输入栏 UI)
        // 消息主题选择
        if (planReplyMessage.value == null) {
            if (chatTopics.keys.isNotEmpty()) {
                TextDropdown(
                    options = chatTopics,
                    onItemSelected = { select ->
                        selectTopicId.value = select
                    },
                    title = stringResource(R.string.Topic),
                    select = selectTopicId,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }

        InputBar(
            query = inputText.value,
            onQueryChange = { inputText.value = it },
            placeholder = stringResource(id = R.string.Write_message),
        )
        Spacer(modifier = Modifier.height(8.dp))
        // 换行和发送消息按钮
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .padding(end = 10.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                // 左：换行按钮
                IconButton(
                    onClick = {
                        inputText.value += "\n"
                    },
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .size(45.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.enter_icon),
                        contentDescription = null,
                        modifier = Modifier.size(45.dp)
                    )
                }

                // 中：录制按钮
                IconButton(
                    onClick = {
                        val hasPermission = ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.RECORD_AUDIO
                        ) == PackageManager.PERMISSION_GRANTED

                        if (hasPermission) {
                            startRecording()
                        } else {
                            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                        }
                    },
                    modifier = Modifier.size(50.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_record),
                        contentDescription = null,
                        modifier = Modifier.size(50.dp)
                    )
                }

                // 右：发送按钮
                IconButton(
                    onClick = {
                        if (planReplyMessage.value == null) {
                            tgApi?.sendMessage(
                                chatId = chatId,
                                message = TdApi.InputMessageText().apply {
                                    text = TdApi.FormattedText().apply {
                                        this.text = inputText.value
                                    }
                                },
                                messageThreadId = selectTopicId.value
                            )
                        } else {
                            if (planReplyMessage.value!!.chatId != chatId) {
                                tgApi?.sendMessage(
                                    chatId = chatId,
                                    message = TdApi.InputMessageText().apply {
                                        text = TdApi.FormattedText().apply {
                                            this.text = inputText.value
                                        }
                                    },
                                    replyTo = TdApi.InputMessageReplyToExternalMessage(
                                        planReplyMessage.value!!.chatId,
                                        planReplyMessage.value!!.id,
                                        null
                                    ),
                                    messageThreadId = selectTopicId.value
                                )
                            } else {
                                tgApi?.sendMessage(
                                    chatId = chatId,
                                    message = TdApi.InputMessageText().apply {
                                        text = TdApi.FormattedText().apply {
                                            this.text = inputText.value
                                        }
                                    },
                                    replyTo = TdApi.InputMessageReplyToMessage(
                                        planReplyMessage.value!!.id,
                                        null
                                    )
                                )
                            }
                            planReplyMessage.value = null
                            tgApi!!.replyMessage.value = null
                        }

                        inputText.value = ""

                        coroutineScope.launch {
                            pagerState.animateScrollToPage(0)
                            listState.animateScrollToItem(0)
                        }
                    },
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(45.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_custom_send),
                        contentDescription = null,
                        modifier = Modifier.size(45.dp)
                    )
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
    // ... (保持原样: 转发消息部分)
    val forwardMessage = tgApi!!.forwardMessage
    if (forwardMessage.value != null) {
        val messageText =
            tgApi!!.handleAllMessages(message = forwardMessage.value, maxText = 100)
        val targetTitle =
            if (forwardMessage.value!!.chatId == currentUserId.value) stringResource(R.string.Saved_Messages) else
                tgApi!!.chatsList.value
                    .find { it.id == forwardMessage.value!!.chatId }
                    ?.title ?: stringResource(R.string.Unknown_chat)

        Text(
            text = stringResource(R.string.Forward),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.clickable(
                onClick = {
                    tgApi!!.forwardMessage.value = null
                }
            )
        )
        MainCard(
            column = {
                Text(
                    text = targetTitle,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
                MessageView(message = messageText)
            },
            item = forwardMessage.value
        )
        // 转发消息部分发送按钮
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .padding(end = 10.dp)
                .fillMaxWidth()
        ) {
            IconButton(
                onClick = {
                    tgApi?.sendMessage(
                        chatId = chatId,
                        message = TdApi.InputMessageForwarded().apply {
                            copyOptions = null
                            fromChatId = forwardMessage.value!!.chatId
                            inGameShare = false
                            messageId = forwardMessage.value!!.id
                        }
                    )
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(0)
                        listState.animateScrollToItem(0)
                    }
                },
                modifier = Modifier
                    .size(45.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_custom_send),
                    contentDescription = null,
                    modifier = Modifier.size(45.dp)
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(80.dp))
}
