/*
 * Copyright (c) 2024-2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.gohj99.tgwear

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.Keep
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.gohj99.tgwear.ui.SplashAnnouncementScreen
import com.gohj99.tgwear.ui.main.ErrorScreen
import com.gohj99.tgwear.ui.main.SplashLoadingScreen
import com.gohj99.tgwear.ui.theme.TGwearTheme
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class AnnouncementActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TGwearTheme {
                SplashLoadingScreen(modifier = Modifier.fillMaxSize())
            }
        }

        // 接收传递的 id
        val parcelableId = intent.getStringExtra("id")

        if (parcelableId != null) {
            // 启动主逻辑
            initIdPage(parcelableId)
        } else {
            // 启动主逻辑
            initPage()
        }
    }

    private fun initIdPage(id: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val domain = getDomain(this@AnnouncementActivity)
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("https://$domain/announcement/?id=${id}")
                    .build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.e(
                            "com.gohj99.TGwear.AnnouncementActivity",
                            "Request failed: ${e.message}"
                        )
                    }

                    override fun onResponse(call: Call, response: Response) {
                        response.use {
                            if (!response.isSuccessful) throw IOException("Unexpected code $response")

                            val responseData = response.body?.string()
                            if (responseData != null) {
                                val gson = Gson()
                                val jsonObject: JsonObject = gson.fromJson(responseData, JsonObject::class.java)

                                // 在主线程上更新 UI
                                runOnUiThread {
                                    setContent {
                                        TGwearTheme {
                                            SplashAnnouncementScreen(
                                                jsonObject = jsonObject,
                                                callback = {}
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                })
            } catch (e: Exception) {
                Log.e("com.gohj99.TGwear.CheckUpdateActivity", "Error: ${e.message}")
                runOnUiThread {
                    setContent {
                        TGwearTheme {
                            ErrorScreen(
                                onRetry = { initIdPage(id)},
                                cause = e.message ?: ""
                            )
                        }
                    }
                }
            }
        }
    }

    @Keep
    private fun initPage() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("https://TGwear.gohj99.site/announcement/")
                    .build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.e(
                            "com.gohj99.TGwear.AnnouncementActivity",
                            "Request failed: ${e.message}"
                        )
                    }

                    override fun onResponse(call: Call, response: Response) {
                        response.use {
                            if (!response.isSuccessful) throw IOException("Unexpected code $response")

                            val responseData = response.body?.string()
                            if (responseData != null) {
                                val gson = Gson()
                                val announcementList: List<Map<String, Any>> = gson.fromJson(responseData, object : TypeToken<List<Map<String, Any>>>() {}.type)

                                // 在主线程上更新 UI
                                runOnUiThread {
                                    setContent {
                                        TGwearTheme {
                                            SplashAnnouncementScreen(
                                                announcementList = announcementList,
                                                callback = { id ->
                                                    startActivity(
                                                        Intent(
                                                            this@AnnouncementActivity,
                                                            AnnouncementActivity::class.java
                                                        ).apply {
                                                            putExtra("id", id)
                                                        }
                                                    )
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                })
            } catch (e: Exception) {
                Log.e("com.gohj99.TGwear.CheckUpdateActivity", "Error: ${e.message}")
                runOnUiThread {
                    setContent {
                        TGwearTheme {
                            ErrorScreen(
                                onRetry = { initPage()},
                                cause = e.message ?: ""
                            )
                        }
                    }
                }
            }
        }
    }
}
fun getDomain(context: Context) : String {
    val domainParts = listOf("site", context.packageName.split(".")[1], context.packageName.split(".")[2])
    return domainParts.reversed().joinToString(".")
}
