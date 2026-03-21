package com.gohj99.tgwear

import android.content.Context

object AppLockManager {
    private var locked = false
    private var promptShowing = false

    fun isEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        return prefs.getBoolean("app_lock_enabled", false)
    }

    fun lock() {
        locked = true
    }

    fun unlock() {
        locked = false
        promptShowing = false
    }

    fun cancelPrompt() {
        promptShowing = false
    }

    fun markPromptShowing() {
        promptShowing = true
    }

    fun shouldPrompt(context: Context): Boolean {
        if (!isEnabled(context)) {
            locked = false
            promptShowing = false
            return false
        }

        val currentActivityName = context::class.java.simpleName
        if (currentActivityName == AppLockActivity::class.java.simpleName ||
            currentActivityName == VoiceCallActivity::class.java.simpleName) {
            return false
        }

        return locked && !promptShowing
    }
}
