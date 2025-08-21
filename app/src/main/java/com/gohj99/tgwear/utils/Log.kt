/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.gohj99.tgwear.utils

import android.util.Log
import com.gohj99.tgwear.Application.Companion.getApplication
import java.io.File

const val TAG_VOIP = "VOIP"
var logDir = File(getApplication().filesDir, "tglogs")
object Log {
    fun info(prompt: String, content: String) {
        Log.d("TGwear", buildString {
            append(prompt)
            if (content.isNotEmpty()) {
                append(": $content")
            }
        })
    }

    fun error(prompt: String, content: String) {
        Log.e("TGwear", buildString {
            append(prompt)
            if (content.isNotEmpty()) {
                append(": $content")
            }
        })
    }
}