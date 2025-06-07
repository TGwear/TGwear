/*
 * Copyright (c) 2024-2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.gohj99.tgwear

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import com.gohj99.tgwear.model.Chat
import com.gohj99.tgwear.model.SettingItem
import com.gohj99.tgwear.ui.main.ErrorScreen
import com.gohj99.tgwear.ui.main.MainScreen
import com.gohj99.tgwear.ui.main.SplashLoadingScreen
import com.gohj99.tgwear.ui.theme.TGwearTheme
import com.gohj99.tgwear.utils.notification.TgApiForPushNotificationManager
import com.gohj99.tgwear.utils.telegram.TgApi
import com.gohj99.tgwear.utils.telegram.getArchiveChats
import com.gohj99.tgwear.utils.telegram.getContacts
import com.gohj99.tgwear.utils.telegram.getCurrentUser
import com.gohj99.tgwear.utils.telegram.loadChats
import com.gohj99.tgwear.utils.telegram.setFCMToken
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.drinkless.tdlib.TdApi
import java.io.File


object TgApiManager {
    @SuppressLint("StaticFieldLeak")
    var tgApi: TgApi? = null
}

object ChatsListManager {
    @SuppressLint("StaticFieldLeak")
    var chatsList: MutableState<List<Chat>> = mutableStateOf(listOf())
}

class MainActivity : BaseActivity() {
    private var isLoggedIn: Boolean = false
    private var exceptionState by mutableStateOf<Exception?>(null)
    private var chatsList = mutableStateOf(listOf<Chat>())
    private var chatsFoldersList = mutableStateOf(listOf<TdApi.ChatFolder>())
    private var mainChatListPosition = mutableStateOf(0)
    private var settingList = mutableStateOf(listOf<SettingItem>())
    private var topTitle = mutableStateOf("")
    private var onPaused = mutableStateOf(false)
    private val contacts = mutableStateOf(listOf<Chat>())
    private val currentUserId = mutableStateOf(-1L)
    private val settingsSharedPref: SharedPreferences by lazy {
        getSharedPreferences("app_settings", MODE_PRIVATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        TgApiManager.tgApi?.close()
        TgApiManager.tgApi = null
    }

    override fun onPause() {
        super.onPause()
        onPaused.value = true
    }

    override fun onResume() {
        super.onResume()
        onPaused.value = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 获取数据收集配置
        if (!settingsSharedPref.contains("Data_Collection")) {
            startActivity(
                Intent(
                    this,
                    AllowDataCollectionActivity::class.java
                )
            )
            finish()
        } else {
            // 获取是否同意获取数据
            if (settingsSharedPref.getBoolean("Data_Collection", false)) {
                Firebase.crashlytics.setCrashlyticsCollectionEnabled(true)
            }

            initializeApp()
            /*
            // 显示启动页面
            setContent {
                TGwearTheme {
                    SplashScreen()
                }
            }

            // 使用 Handler 延迟启动主逻辑
            Handler(Looper.getMainLooper()).postDelayed({
                initializeApp()
            }, 600) // 延迟
            */
        }
    }

    private fun initializeApp() {
        topTitle.value = getString(R.string.HOME)

        val loginSharedPref = getSharedPreferences("LoginPref", MODE_PRIVATE)
        isLoggedIn = loginSharedPref.getBoolean("isLoggedIn", false)

        if (!isLoggedIn) {
            startWelcomeActivity()
        } else {
            // 显示加载页面
            setContent {
                TGwearTheme {
                    SplashLoadingScreen(modifier = Modifier.fillMaxSize())
                }
            }

            if (!settingsSharedPref.getBoolean("Remind5_read", false)) {
                startActivity(
                    Intent(
                        this,
                        RemindActivity::class.java
                    )
                )
            }

            initMain()
        }
    }

    private fun startWelcomeActivity() {
        startActivity(Intent(this, WelcomeActivity::class.java))
        finish()
    }

    private fun initMain() {
        val config = loadConfig(this)
        if (config.getProperty("BETA") == "true") {
            startActivity(
                Intent(
                    this,
                    IsBetaActivity::class.java
                )
            )
        }
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                if (TgApiForPushNotificationManager.tgApi != null) {
                    println("MainActivity close TgApiForPushNotification sever")
                    TgApiForPushNotificationManager.tgApi?.closeSuspend()
                    TgApiForPushNotificationManager.tgApi = null
                }
                val gson = Gson()
                val sharedPref = getSharedPreferences("LoginPref", MODE_PRIVATE)
                var userList = sharedPref.getString("userList", "")
                if (userList == "") {
                    val tempChatsList = mutableStateOf(listOf<Chat>())
                    val tempTgApi = TgApi(
                        this@MainActivity,
                        chatsList = tempChatsList,
                        topTitle = topTitle,
                        chatsFoldersList = chatsFoldersList,
                        mainChatListPosition = mainChatListPosition,
                        onPaused = onPaused
                    )

                    // 调用重试机制来获取用户信息
                    var currentUser = fetchCurrentUserWithRetries(
                        tgApi = tempTgApi,
                        repeatTimes = 10  // 最多重试10次
                        // 每次失败后等待1秒
                    )

                    while (currentUser == null) {
                        currentUser = tempTgApi.getCurrentUser()
                    }

                    val jsonObject = JsonObject()
                    jsonObject.addProperty(
                        currentUser[0],
                        currentUser[1]
                    )
                    userList = jsonObject.toString()
                    with(sharedPref.edit()) {
                        putString("userList", userList)
                        apply()
                    }
                    tempTgApi.close()
                }

                val installer = packageManager.getInstallerPackageName(packageName)
                val jsonObject: JsonObject = gson.fromJson(userList, JsonObject::class.java)
                val accounts = mutableListOf<SettingItem>()
                var a = 0
                for (account in jsonObject.keySet()) {
                    if (a == 0) {
                        accounts.add(
                            SettingItem.Click(
                                itemName = jsonObject.get(account.toString()).asString,
                                onClick = {},
                                color = Color(0xFF2C323A)
                            )
                        )
                    } else {
                        accounts.add(
                            SettingItem.Click(
                                itemName = jsonObject.get(account.toString()).asString,
                                onClick = {
                                    startActivity(
                                        Intent(
                                            this@MainActivity,
                                            SwitchAccountActivity::class.java
                                        ).putExtra("account", account)
                                    )
                                }
                            )
                        )
                    }
                    a += 1
                }
                accounts.addAll(listOf<SettingItem>(
                    SettingItem.Click(
                        itemName = getString(R.string.Add_Account),
                        onClick = {
                            startActivity(
                                Intent(
                                    this@MainActivity,
                                    LoginActivity::class.java
                                )
                            )
                        }
                    ),
                    SettingItem.Click(
                        itemName = getString(R.string.Log_out),
                        onClick = {
                            startActivity(
                                Intent(
                                    this@MainActivity,
                                    ConfirmLogoutActivity::class.java
                                )
                            )
                        }
                    ),
                    // 捐赠
                    if (installer != "com.android.vending") {
                        SettingItem.Click(
                            itemName = getString(R.string.Donate),
                            onClick = {
                                startActivity(
                                    Intent(
                                        this@MainActivity,
                                        DonateActivity::class.java
                                    )
                                )
                            }
                        )
                    } else SettingItem.None(),

                    // 设置代理
                    SettingItem.Click(
                        itemName = getString(R.string.Proxy),
                        onClick = {
                            startActivity(
                                Intent(
                                    this@MainActivity,
                                    SetProxyActivity::class.java
                                )
                            )
                        }
                    ),
                    SettingItem.Click(
                        itemName = getString(R.string.Check_Update),
                        onClick = {
                            startActivity(
                                Intent(
                                    this@MainActivity,
                                    GoToCheckUpdateActivity::class.java
                                )
                            )
                        }
                    ),
                    SettingItem.Click(
                        itemName = getString(R.string.About),
                        onClick = {
                            startActivity(
                                Intent(
                                    this@MainActivity,
                                    AboutActivity::class.java
                                )
                            )
                        }
                    ),
                    SettingItem.Click(
                        itemName = getString(R.string.setting_all),
                        onClick = {
                            startActivity(
                                Intent(
                                    this@MainActivity,
                                    SettingActivity::class.java
                                )
                            )
                        }
                    )
                )
                )
                settingList.value = accounts

                TgApiManager.tgApi = TgApi(
                    this@MainActivity,
                    chatsList = chatsList,
                    userId = jsonObject.keySet().firstOrNull().toString(),
                    topTitle = topTitle,
                    chatsFoldersList = chatsFoldersList,
                    mainChatListPosition = mainChatListPosition,
                    onPaused = onPaused
                )
                ChatsListManager.chatsList = chatsList
                TgApiManager.tgApi?.loadChats(15)
                TgApiManager.tgApi?.getContacts(contacts)
                // 异步获取当前用户 ID
                lifecycleScope.launch {
                    while (currentUserId.value == -1L) {
                        TgApiManager.tgApi?.getCurrentUser() ?.let {
                            currentUserId.value = it[0].toLong()
                        }
                    }
                    TgApiManager.tgApi?.getArchiveChats()
                }
                // 检查是否切换账号和是否打开消息推送
                if (settingsSharedPref.getBoolean("Change_account", false)) {
                    with(sharedPref.edit()) {
                        putBoolean("Change_account", false)
                    }
                    if (settingsSharedPref.getBoolean("Use_Notification", false)) {
                        FirebaseMessaging.getInstance().token
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val token = task.result
                                    //println(token)
                                    settingsSharedPref.edit(commit = false) {
                                        putString("Token_Notification", token)
                                    }
                                    TgApiManager.tgApi?.setFCMToken(token) { id ->
                                        settingsSharedPref.edit(commit = false) {
                                            putLong("Id_Notification", id)
                                        }
                                        //println(id)
                                    }
                                }
                            }
                    }
                }
                launch(Dispatchers.Main) {
                    setContent {
                        TGwearTheme {
                            MainScreen(
                                chats = chatsList,
                                chatPage = { chat ->
                                    startActivity(
                                        Intent(this@MainActivity, ChatActivity::class.java).apply {
                                            putExtra("chat", chat)
                                        }
                                    )
                                },
                                settingList = settingList,
                                contacts = contacts,
                                topTitle = topTitle,
                                chatsFoldersList = chatsFoldersList,
                                mainChatListPosition = mainChatListPosition,
                                currentUserId = currentUserId
                            )
                        }
                    }
                }

                // 调整聊天页面
                intent.getLongExtra("chatId", 0L).let { chatId ->
                    if (chatId != 0L) {
                        startActivity(
                            Intent(this@MainActivity, ChatActivity::class.java).apply {
                                putExtra("chat", Chat(chatId, title = ""))
                            }
                        )
                    }
                }
            } catch (e: Exception) {
                exceptionState = e
                Log.e("MainActivity", "Error initializing app: ${e.message}")
                if (e.message == "Failed to authorize") {
                    val externalDir: File = getExternalFilesDir(null)
                        ?: throw IllegalStateException("Failed to get external directory.")

                    val gson = Gson()
                    val sharedPref = getSharedPreferences("LoginPref", MODE_PRIVATE)
                    var userList = sharedPref.getString("userList", "")
                    if (userList == "") throw Exception("No user data found")
                    val jsonObject: JsonObject = gson.fromJson(userList, JsonObject::class.java)
                    if (jsonObject.entrySet().size <= 1) {
                        // 清除缓存
                        cacheDir.deleteRecursively()
                        // 清空软件文件
                        filesDir.deleteRecursively()
                        val dir = externalDir.listFiles()
                        dir?.forEach { file ->
                            if (!file.deleteRecursively()) {
                                // 如果某个文件或文件夹无法删除，可以记录日志或采取其他处理方式
                                println("Failed to delete: ${file.absolutePath}")
                            }
                        }
                        cacheDir.deleteRecursively()
                        // 清空 SharedPreferences
                        getSharedPreferences("LoginPref", MODE_PRIVATE).edit().clear()
                            .apply()
                        // 重启软件
                        Handler(Looper.getMainLooper()).postDelayed({
                            val intent = packageManager.getLaunchIntentForPackage(packageName)
                            intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                            android.os.Process.killProcess(android.os.Process.myPid())
                        }, 1000)
                    } else {
                        val account = jsonObject.keySet().firstOrNull().toString()
                        jsonObject.remove(account)
                        userList = jsonObject.toString()
                        val dir = File(externalDir.absolutePath + "/" + account)
                        dir.listFiles()?.find { it.name == "tdlib" && it.isDirectory }
                            ?.deleteRecursively()
                        cacheDir.deleteRecursively()
                        with(sharedPref.edit()) {
                            putString("userList", userList)
                            commit()
                        }
                        retryInitialization()
                    }
                } else {
                    launch(Dispatchers.Main) {
                        setContent {
                            TGwearTheme {
                                ErrorScreen(
                                    onRetry = { retryInitialization() },
                                    onSetting = {
                                        startActivity(
                                            Intent(this@MainActivity, SettingActivity::class.java)
                                        )
                                    },
                                    cause = e.message ?: ""
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private suspend fun fetchCurrentUserWithRetries(
        tgApi: TgApi,
        repeatTimes: Int
    ): List<String>? {
        repeat(repeatTimes) {
            val currentUser = tgApi.getCurrentUser()  // 假设这个是挂起函数
            // 成功获取到用户
            //println("成功获取用户: $currentUser，提前退出循环。")
            return currentUser
        }
        return null // 尝试多次失败，返回 null
    }

    private fun retryInitialization() {
        exceptionState = null
        initMain()
    }
}

// 扩展函数，用于在 JsonObject 前添加新的键值对
fun JsonObject.firstAdd(key: String, value: String) {
    // 创建临时的 JsonObject 来保存当前的数据
    val tempJsonObject = JsonObject()

    // 将当前 JsonObject 的键值对复制到临时对象中
    for (entry in this.entrySet()) {
        tempJsonObject.add(entry.key, entry.value)
    }

    // 清空当前 JsonObject
    this.entrySet().clear()

    // 先添加新的键值对到当前对象
    this.addProperty(key, value)

    // 将临时对象的数据重新添加到当前对象
    for (entry in tempJsonObject.entrySet()) {
        this.add(entry.key, entry.value)
    }
}

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center // 居中对齐
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Splash Icon"
        )
    }
}
