/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.gohj99.tgwear

import android.app.Application

class Application : Application() {

    companion object {
        private lateinit var application: Application
        fun getApplication(): Application = application
    }

    override fun onCreate() {
        super.onCreate()
        application = this
    }

    init {
        System.loadLibrary("tdjni")
        System.loadLibrary("tgcallsjni")
        System.loadLibrary("leveldbjni")
        System.loadLibrary("tgxjni")
        System.loadLibrary("tgcallsjni") // 重复加载一次可能没必要，可以去掉

        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            throwable.printStackTrace()
        }
    }
}

