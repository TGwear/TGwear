/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.gohj99.tgwear

import android.os.Bundle
import androidx.activity.compose.setContent
import com.gohj99.tgwear.ui.VoiceCallScreen
import com.gohj99.tgwear.ui.theme.TGwearTheme
import com.gohj99.tgwear.utils.telegram.TgApi
import com.gohj99.tgwear.utils.telegram.acceptCall
import org.drinkless.tdlib.TdApi

class VoiceCallActivity : BaseActivity() {
    private lateinit var tgApi: TgApi
    private lateinit var callItem: TdApi.Call

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

        setContent {
            TGwearTheme {
                VoiceCallScreen(
                    acceptCall = {
                        tgApi.acceptCall(callItem.id)
                    }
                )
            }
        }
    }
}
