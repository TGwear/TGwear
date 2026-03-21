package com.gohj99.tgwear

import android.app.KeyguardManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gohj99.tgwear.ui.CustomButton
import com.gohj99.tgwear.ui.theme.TGwearTheme

class AppLockActivity : BaseActivity() {
    private var promptStarted = false

    private val unlockLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                AppLockManager.unlock()
                finish()
            } else {
                AppLockManager.cancelPrompt()
                moveTaskToBack(true)
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TGwearTheme {
                AppLockScreen(
                    onUnlock = { requestUnlock() }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!promptStarted) {
            promptStarted = true
            requestUnlock()
        }
    }

    private fun requestUnlock() {
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (!keyguardManager.isDeviceSecure) {
            Toast.makeText(this, getString(R.string.app_lock_requires_device_lock), Toast.LENGTH_SHORT).show()
            val prefs = getSharedPreferences("app_settings", MODE_PRIVATE)
            prefs.edit().putBoolean("app_lock_enabled", false).apply()
            AppLockManager.unlock()
            finish()
            return
        }

        val intent = keyguardManager.createConfirmDeviceCredentialIntent(
            getString(R.string.app_lock_title),
            getString(R.string.app_lock_description)
        )

        if (intent == null) {
            AppLockManager.unlock()
            finish()
            return
        }

        unlockLauncher.launch(intent)
    }
}

@Composable
private fun AppLockScreen(onUnlock: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.app_lock_title),
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.app_lock_description),
            color = Color(0xFF9BA7B4),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 10.dp, bottom = 18.dp)
        )
        CustomButton(
            text = stringResource(R.string.app_lock_unlock),
            onClick = onUnlock
        )
    }
}
