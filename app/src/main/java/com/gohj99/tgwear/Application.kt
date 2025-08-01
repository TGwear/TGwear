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
    init {
        System.loadLibrary("tdjni")
        System.loadLibrary("tgcallsjni")
        System.loadLibrary("leveldbjni")
        System.loadLibrary("tgxjni")
        System.loadLibrary("tgcallsjni")

        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->

        }
    }

    companion object {
        private lateinit var mApplication: com.gohj99.tgwear.Application
        fun getApplication(): com.gohj99.tgwear.Application = mApplication
    }
}
