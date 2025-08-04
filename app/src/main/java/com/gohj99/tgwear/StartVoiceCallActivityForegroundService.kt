/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.gohj99.tgwear

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.graphics.Bitmap
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import com.gohj99.tgwear.utils.notification.drawableToBitmap
import com.gohj99.tgwear.utils.notification.showIncomingCallNotification

class StartVoiceCallActivityForegroundService : Service() {

    override fun onStartCommand(intent: Intent?, flag: Int, startId: Int): Int {
        /*val firstNotification = NotificationCompat.Builder(this, "voip_channel_id")
            .setContentTitle("Call incoming...")
            .setSmallIcon(android.R.drawable.sym_call_incoming)
            .build()
        startForeground(1001, firstNotification)*/

        // 发送通知
        val callerName = intent?.getStringExtra("callerName") ?: "unknown"
        val callId = intent?.getIntExtra("callId", 0)
        val bmp = intent?.getParcelableExtra<Bitmap>("chatIconBitmap")
        val notification = showIncomingCallNotification(
            callerName = callerName,
            targetActivity = VoiceCallActivity::class.java,
            callId = callId ?: 0,
            chatIconBitmap = bmp ?: drawableToBitmap(this, R.mipmap.ic_launcher)!!
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            startForeground(1001, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE)
        } else {
            startForeground(1001, notification)
        }

        // 唤醒屏幕
        wakeUpAndUnlock()

        //println("StartVoiceCallActivityForegroundService: $callerName")
        // 启动目标 Activity
        val activityIntent = Intent(this, VoiceCallActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        startActivity(activityIntent)

        // 启动后结束服务，释放资源
        stopSelf()

        return START_NOT_STICKY
    }

    private fun wakeUpAndUnlock() {
        val pm = getSystemService(POWER_SERVICE) as PowerManager
        val wakeLock = pm.newWakeLock(
            PowerManager.SCREEN_BRIGHT_WAKE_LOCK or
                    PowerManager.ACQUIRE_CAUSES_WAKEUP or
                    PowerManager.ON_AFTER_RELEASE,
            "TGwearApp:WakeLockTag"
        )
        wakeLock.acquire(3000L)
    }


    override fun onBind(intent: Intent?): IBinder? = null
}
