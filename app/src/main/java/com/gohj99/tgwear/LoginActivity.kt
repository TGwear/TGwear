/*
 * Copyright (c) 2024-2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.gohj99.tgwear

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.gohj99.tgwear.ui.login.SplashLoginQRScreen
import com.gohj99.tgwear.ui.login.SplashLoginScreen
import com.gohj99.tgwear.ui.login.SplashPasswordScreen
import com.gohj99.tgwear.ui.main.SplashLoadingScreen
import com.gohj99.tgwear.ui.theme.TGwearTheme
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import kotlinx.coroutines.runBlocking
import org.drinkless.tdlib.Client
import org.drinkless.tdlib.TdApi
import java.io.File
import java.io.IOException
import java.security.SecureRandom
import java.util.Properties

class LoginActivity : BaseActivity() {
    private lateinit var client: Client
    private lateinit var languageCode: String
    private lateinit var appVersion: String
    private var qrCodeLink by mutableStateOf<String?>(null)
    private var showPasswordScreen = mutableStateOf(false)
    private var passwordHint by mutableStateOf("")
    private var doneStr = mutableStateOf("")
    private var loginWay = mutableStateOf("PhoneNumber")
    private var showSendCode by mutableStateOf(true)
    private var showLoading by mutableStateOf(true)
    private var phoneNumber = mutableStateOf("")
    private var verifyCode = mutableStateOf("")
    private var password = mutableStateOf("")

    override fun onDestroy() {
        super.onDestroy()
        // 在这里释放 TDLib 资源
        runBlocking {
            if (!showPasswordScreen.value && loginWay.value == "QrCode") {
                client.send(TdApi.LogOut()) {}
            }
            client.send(TdApi.Close()) {}
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        languageCode = this.resources.configuration.locales[0].language
        appVersion = getAppVersion(this)

        setContent {
            TGwearTheme {
                if (showLoading) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        SplashLoadingScreen()
                        Text(
                            text = stringResource(R.string.Connecting_Telegram_Sever),
                            color = Color.White
                        )
                    }
                } else {
                    if (showPasswordScreen.value) {
                        SplashPasswordScreen(
                            onDoneClick = { password ->
                                client.send(TdApi.CheckAuthenticationPassword(password)) {
                                    authRequestHandler(
                                        it
                                    )
                                }
                            },
                            passwordHint = passwordHint,
                            doneStr = doneStr,
                            password = password,
                        )
                    } else {
                        if (loginWay.value == "PhoneNumber") {
                            SplashLoginScreen(
                                showSendCode = showSendCode,
                                onLoginWithQR = {
                                    loginWay.value = "QrCode"
                                    client.send(TdApi.RequestQrCodeAuthentication(LongArray(0))) {
                                        if (!(it is TdApi.Ok)) {
                                            loginWay.value = "PhoneNumber"
                                        }
                                        authRequestHandler(
                                            it
                                        )
                                    }
                                },
                                onSendVerifyCode = { phoneNumber ->
                                    showSendCode = false
                                    client.send(TdApi.SetAuthenticationPhoneNumber(phoneNumber, null)) {
                                        if (it is TdApi.Ok) {
                                            // 发送验证码成功
                                            runOnUiThread {
                                                Toast.makeText(this@LoginActivity, getString(R.string.Successful), Toast.LENGTH_SHORT).show()
                                            }
                                        } else {
                                            showSendCode = true
                                        }
                                        authRequestHandler(
                                            it
                                        )
                                    }
                                },
                                onDone = { code ->
                                    client.send(TdApi.CheckAuthenticationCode(code)) {
                                        authRequestHandler(
                                            it
                                        )
                                    }
                                },
                                phoneNumber = phoneNumber,
                                verifyCode = verifyCode
                            )
                        } else if (loginWay.value == "QrCode") {
                            SplashLoginQRScreen(qrCodeLink = qrCodeLink)
                        }
                    }
                }
            }
        }

        client = Client.create({ update -> handleUpdate(update) }, null, null)
        doneStr.value = getString(R.string.Done) // 初始化 doneStr
    }

    // 加载配置文件
    private fun loadConfig(context: Context): Properties {
        val properties = Properties()
        try {
            val inputStream = context.assets.open("config.properties")
            inputStream.use { properties.load(it) }
        } catch (e: IOException) {
            e.printStackTrace()
            // 处理异常，例如返回默认配置或通知用户
        }
        return properties
    }

    private fun getAppVersion(context: Context): String {
        return try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            pInfo.versionName
        } catch (e: Exception) {
            "1.0.0"
        }.toString()
    }

    // 处理 TDLib 更新的函数
    @SuppressLint("CommitPrefEdits")
    private fun handleUpdate(update: TdApi.Object) {
        when (update.constructor) {
            TdApi.UpdateAuthorizationState.CONSTRUCTOR -> {
                val sharedPref = getSharedPreferences("LoginPref", MODE_PRIVATE)
                val encryptionKeyString = sharedPref.getString("encryption_key", null)
                val authorizationState = (update as TdApi.UpdateAuthorizationState).authorizationState
                when (authorizationState.constructor) {
                    TdApi.AuthorizationStateWaitTdlibParameters.CONSTRUCTOR -> {
                        // 获取应用外部数据目录
                        val externalDir: File = getExternalFilesDir(null)
                            ?: throw IllegalStateException("Failed to get external directory.")
                        //获取API ID和API Hash
                        val config = loadConfig(this) // 传递 context
                        val tdapiId = config.getProperty("api_id").toInt()
                        val tdapiHash = config.getProperty("api_hash")
                        // 设置 TDLib 参数
                        client.send(TdApi.SetTdlibParameters().apply {
                            databaseDirectory = externalDir.absolutePath + "/tdlib"
                            useMessageDatabase = true
                            useSecretChats = true
                            apiId = tdapiId
                            apiHash = tdapiHash
                            systemLanguageCode = languageCode
                            deviceModel = Build.MODEL
                            systemVersion = Build.VERSION.RELEASE
                            applicationVersion = appVersion
                            useSecretChats = false
                            useMessageDatabase = true
                            databaseEncryptionKey = if (encryptionKeyString != null) {
                                // 检查本地是否有加密密钥
                                encryptionKeyString.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
                            } else {
                                // 生成一个新的随机加密密钥并保存
                                val newKeyBytes = ByteArray(32).also {
                                    SecureRandom().nextBytes(it)
                                }
                                val newKeyString = newKeyBytes.joinToString("") { "%02x".format(it) }
                                with(sharedPref.edit()) {
                                    putString("encryption_key", newKeyString)
                                    apply()
                                }
                                newKeyBytes
                            }
                        }) { result ->
                            println("SetTdlibParameters result: $result")
                        }
                    }

                    TdApi.AuthorizationStateWaitPhoneNumber.CONSTRUCTOR -> {
                        loginWay.value = "PhoneNumber"
                        // 请求二维码认证
                        /*client.send(TdApi.RequestQrCodeAuthentication(LongArray(0))) {
                            authRequestHandler(
                                it
                            )
                        }*/
                    }
                    TdApi.AuthorizationStateWaitOtherDeviceConfirmation.CONSTRUCTOR -> {
                        val link = (authorizationState as TdApi.AuthorizationStateWaitOtherDeviceConfirmation).link
                        println(link)
                        loginWay.value = "QrCode"
                        // 展示二维码
                        runOnUiThread {
                            qrCodeLink = link
                        }
                    }
                    TdApi.AuthorizationStateReady.CONSTRUCTOR -> {
                        // 登录成功
                        println("Login Successful")
                        doneStr.value = getString(R.string.Login_Successful)
                        // 发送广播通知 WelcomeActivity 销毁自己
                        /*LocalBroadcastManager.getInstance(this).sendBroadcast(
                            Intent("ACTION_DESTROY_WELCOME_ACTIVITY")
                        )*/
                        // 存储登录成功信息
                        val sharedPref = getSharedPreferences("LoginPref", MODE_PRIVATE)
                        TdApi.User().let {
                            client.send(TdApi.GetMe()) {
                                if (it is TdApi.User) {
                                    // 移动文件夹
                                    // 获取私有外部存储的根目录
                                    val externalDir: File = getExternalFilesDir(null)
                                        ?: throw IllegalStateException("Failed to get external directory.")
                                    // 删除可能存在的文件夹
                                    val dir =
                                        File(externalDir.absolutePath + "/" + it.id.toString())
                                    dir.listFiles()?.find { it.name == "tdlib" && it.isDirectory }
                                        ?.deleteRecursively()
                                    cacheDir.deleteRecursively()
                                    // 定义源文件夹路径 (tdlib)
                                    val sourceDir = File(externalDir, "tdlib")
                                    if (!sourceDir.exists()) {
                                        throw IOException("Source folder does not exist: ${sourceDir.absolutePath}")
                                    }
                                    // 定义目标文件夹路径 (/id/tdlib)
                                    val targetParentDir = File(externalDir, it.id.toString())
                                    if (!targetParentDir.exists()) {
                                        // 如果目标父目录不存在，则创建
                                        if (!targetParentDir.mkdirs()) {
                                            throw IOException("Failed to create target folder: ${targetParentDir.absolutePath}")
                                        }
                                    }
                                    // 定义目标路径
                                    val targetDir = File(targetParentDir, "tdlib")
                                    // 移动文件夹
                                    val success = sourceDir.renameTo(targetDir)
                                    if (success) {
                                        println("Folder moved successfully: ${targetDir.absolutePath}")
                                    } else {
                                        throw IOException("Failed to move folder to: ${targetDir.absolutePath}")
                                    }

                                    // 存储账号数据
                                    val gson = Gson()
                                    var userList: String
                                    if (sharedPref.getBoolean("isLoggedIn", false)) {
                                        // 非首次登录
                                        userList = sharedPref.getString("userList", "").toString()
                                        val jsonObject: JsonObject =
                                            gson.fromJson(userList, JsonObject::class.java)
                                        // 如果登录重复账号
                                        if (jsonObject.has(it.id.toString())) {
                                            jsonObject.remove(it.id.toString())
                                        } else {
                                            jsonObject.firstAdd(
                                                it.id.toString(),
                                                "${it.firstName} ${it.lastName}"
                                            )
                                            userList = jsonObject.toString()
                                        }
                                    } else {
                                        // 首次登录
                                        val jsonObject = JsonObject()
                                        jsonObject.addProperty(
                                            it.id.toString(),
                                            "${it.firstName} ${it.lastName}"
                                        )
                                        userList = jsonObject.toString()
                                    }
                                    with(sharedPref.edit()) {
                                        putBoolean("isLoggedIn", true)
                                        putString("userList", userList)
                                        apply()
                                    }
                                }
                            }
                        }
                        runOnUiThread {
                            // 重启软件
                            resetSelf()
                            //finish()
                        }
                    }
                    TdApi.AuthorizationStateWaitPassword.CONSTRUCTOR -> {
                        //当需要输入密码时执行
                        val passwordState = authorizationState as TdApi.AuthorizationStateWaitPassword
                        passwordHint = passwordState.passwordHint

                        // 进入密码输入处理
                        runOnUiThread {
                            showPasswordScreen.value = true
                        }
                    }
                    // 处理其他授权状态...
                    TdApi.AuthorizationStateClosed.CONSTRUCTOR -> {
                        // TDLib 已关闭
                        println("TDLib 已关闭")
                    }

                    TdApi.AuthorizationStateClosing.CONSTRUCTOR -> {
                        // TDLib 正在关闭
                        println("TDLib 正在关闭")
                    }

                    TdApi.AuthorizationStateLoggingOut.CONSTRUCTOR -> {
                        // 正在退出登录
                        println("正在退出登录")
                    }

                    TdApi.AuthorizationStateWaitCode.CONSTRUCTOR -> {
                        // 请求验证码
                        println("请求验证码")
                    }

                    TdApi.AuthorizationStateWaitRegistration.CONSTRUCTOR -> {
                        // 请求注册
                        println("请求注册")
                    }
                }
            }

            TdApi.UpdateConnectionState.CONSTRUCTOR -> {
                when ((update as TdApi.UpdateConnectionState).state.constructor) {
                    TdApi.ConnectionStateReady.CONSTRUCTOR -> {
                        // 已经成功连接到 Telegram 服务器
                        showLoading = false
                    }

                    TdApi.ConnectionStateConnecting.CONSTRUCTOR -> {
                        // 正在尝试连接到 Telegram 服务器
                        showLoading = true
                    }

                    TdApi.ConnectionStateConnectingToProxy.CONSTRUCTOR -> {
                        // 正在尝试通过代理连接到 Telegram 服务器
                        showLoading = true
                    }

                    TdApi.ConnectionStateUpdating.CONSTRUCTOR -> {
                        // 正在更新 Telegram 数据库
                        showLoading = false
                    }

                    TdApi.ConnectionStateWaitingForNetwork.CONSTRUCTOR -> {
                        // 正在等待网络连接
                        showLoading = true
                    }
                    else -> {
                        // 其他网络状态处理
                        showLoading = false
                    }
                }
            }
            // 处理其他更新...
        }
    }

    // 处理认证请求的函数
    private fun authRequestHandler(result: TdApi.Object) {
        // 处理认证请求结果
        when (result.constructor) {
            TdApi.Error.CONSTRUCTOR -> {
                val error = result as TdApi.Error
                println("${getString(R.string.Request_error)} : ${error.message}")
                when (error.message) {
                    "PASSWORD_HASH_INVALID" -> {
                        doneStr.value = getString(R.string.Password_Error)
                    }

                    else -> runOnUiThread {
                        doneStr.value = getString(R.string.Done)
                        if (!this.isFinishing && !this.isDestroyed) {
                            AlertDialog.Builder(this)
                                .setMessage("${getString(R.string.Request_error)}\ncode:${error.code}\n${error.message}")
                                .setPositiveButton(getString(R.string.OK)) { dialog, which -> }
                                .show()
                        }

                    }
                }
            }
            // 处理其他结果...
        }
    }

    // 重启软件
    private fun resetSelf(){
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = packageManager.getLaunchIntentForPackage(packageName)
            intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            android.os.Process.killProcess(android.os.Process.myPid())
        }, 1000)
    }
}

// 创建一个函数来生成二维码的 Bitmap
fun generateQrCode(context: Context, qrCodeLink: String, sizeInDp: Int): Bitmap? {
    val sizeInPx = context.dpToPx(sizeInDp)
    val hints = hashMapOf<EncodeHintType, Any>()
    hints[EncodeHintType.MARGIN] = 0 // 设置边距为0

    return try {
        val bitMatrix = MultiFormatWriter().encode(qrCodeLink, BarcodeFormat.QR_CODE, sizeInPx, sizeInPx, hints)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bmp.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }
        bmp
    } catch (e: WriterException) {
        e.printStackTrace()
        null
    }
}

//dp值转换为像素值
fun Context.dpToPx(dp: Int): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        this.resources.displayMetrics
    ).toInt()
}
