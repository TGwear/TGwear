/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.gohj99.tgwear

import android.app.Application
import android.os.Bundle

class Application : Application() {

    companion object {
        private lateinit var application: Application
        fun getApplication(): Application = application
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            private var startedActivityCount = 0

            override fun onActivityCreated(activity: android.app.Activity, savedInstanceState: Bundle?) = Unit

            override fun onActivityStarted(activity: android.app.Activity) {
                startedActivityCount += 1
            }

            override fun onActivityResumed(activity: android.app.Activity) = Unit

            override fun onActivityPaused(activity: android.app.Activity) = Unit

            override fun onActivityStopped(activity: android.app.Activity) {
                startedActivityCount = (startedActivityCount - 1).coerceAtLeast(0)
                if (startedActivityCount == 0) {
                    AppLockManager.lock()
                }
            }

            override fun onActivitySaveInstanceState(activity: android.app.Activity, outState: Bundle) = Unit

            override fun onActivityDestroyed(activity: android.app.Activity) = Unit
        })
    }

    init {
        System.loadLibrary("tdjni")
        System.loadLibrary("tgcallsjni")
        System.loadLibrary("leveldbjni")
        System.loadLibrary("tgxjni")

        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            throwable.printStackTrace()
        }
    }
}
