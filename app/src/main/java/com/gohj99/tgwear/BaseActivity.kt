/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.gohj99.tgwear

import android.content.Context
import androidx.activity.ComponentActivity

abstract class BaseActivity : ComponentActivity() {
    override fun attachBaseContext(newBase: Context) {
        val installer = newBase.packageManager.getInstallerPackageName(newBase.packageName)
        val prefs = newBase.getSharedPreferences("app_settings", MODE_PRIVATE)
        val lang = prefs.getString("app_lang", null)
        if (installer == "com.android.vending") {
            if (lang == "en") super.attachBaseContext(LocaleHelper.setLocale(newBase, lang))
            else super.attachBaseContext(newBase)
        } else {
            val ctx = if (lang.isNullOrEmpty()) newBase
            else LocaleHelper.setLocale(newBase, lang)
            super.attachBaseContext(ctx)
        }
    }
}
