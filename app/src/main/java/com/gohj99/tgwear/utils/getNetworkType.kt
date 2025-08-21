/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.gohj99.tgwear.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import org.thunderdog.challegram.voip.annotation.CallNetworkType

fun getNetworkType(context: Context): Int {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return CallNetworkType.UNKNOWN
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return CallNetworkType.UNKNOWN

    return when {
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> CallNetworkType.WIFI
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
            // 对于蜂窝网络，需要进一步判断具体的类型
            // 注意：这里无法直接获取 VoIPController.h 中的具体类型
            // 只能大致判断为蜂窝网络
            CallNetworkType.OTHER_MOBILE
        }
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> CallNetworkType.ETHERNET
        else -> CallNetworkType.UNKNOWN
    }
}
