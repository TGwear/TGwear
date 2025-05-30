/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.gohj99.tgwear

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

// 语言切换帮助类
object LocaleHelper {

    /**
     * 解析各种中文（以及其他语言）代码：
     * - zh, zh-CN, zh-rCN, zh-Hans → 简体
     * - zh-TW, zh-rTW, zh-Hant → 繁體
     * - 其它 BCP-47 标签直接通过 forLanguageTag
     */
    private fun parseLocale(code: String): Locale {
        return when (code.lowercase(Locale.ROOT)) {
            "zh", "zh-cn", "zh-rcn", "zh-hans" -> Locale.SIMPLIFIED_CHINESE
            "zh-tw", "zh-rtw", "zh-hant"        -> Locale.TRADITIONAL_CHINESE
            else -> {
                // 先尝试 BCP-47
                val tag = code.replace("r", "-")
                Locale.forLanguageTag(tag)
            }
        }
    }

    /**
     * 强制应用指定语言，返回带有新 Configuration 的 Context。
     */
    fun setLocale(context: Context, languageCode: String): Context {
        val locale = parseLocale(languageCode)

        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        return context.createConfigurationContext(config)
    }
}
