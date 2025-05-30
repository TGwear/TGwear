/*
 * Copyright (c) 2024-2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.gohj99.tgwear

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gohj99.tgwear.ui.theme.TGwearTheme
import com.gohj99.tgwear.ui.verticalRotaryScroll

class AllowDataCollectionActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val settingsSharedPref = getSharedPreferences("app_settings", MODE_PRIVATE)

        setContent {
            TGwearTheme {
                SplashAllowDataCollectionScreen { dataCollection ->
                    with(settingsSharedPref.edit()) {
                        putBoolean("Data_Collection", dataCollection)
                        commit()
                        startActivity(
                            Intent(
                                this@AllowDataCollectionActivity,
                                MainActivity::class.java
                            )
                        )
                        finish()
                    }
                }
            }
        }
    }
}

@Composable
fun SplashAllowDataCollectionScreen(set: (Boolean) -> Unit) {
    val scrollState = rememberScrollState()
    LaunchedEffect(Unit) {
        scrollState.scrollTo(80)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .verticalRotaryScroll(scrollState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 标题
        Text(
            text = stringResource(id = R.string.data_collection_title),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 86.dp)
        )

        // 主要说明部分
        Text(
            text = stringResource(id = R.string.data_collection_description),
            fontSize = 16.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 不同意和同意按钮
        Box(
            modifier = Modifier
                .padding(bottom = 4.dp, start = 16.dp, end = 16.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = { set(true) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3A7FBE),  // 按钮背景颜色
                    contentColor = Color.White   // 按钮文字颜色
                )
            ) {
                Text(text = stringResource(id = R.string.agree))
            }
        }
        Box(
            modifier = Modifier
                .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = { set(false) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3E4D58),  // 按钮背景颜色
                    contentColor = Color.White   // 按钮文字颜色
                )
            ) {
                Text(text = stringResource(id = R.string.disagree))
            }
        }

        Spacer(modifier = Modifier.height(42.dp))
//        Row(
//            horizontalArrangement = Arrangement.Center,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(bottom = 64.dp, start = 16.dp, end = 16.dp)
//        ) {
//            Button(
//                onClick = { set(false) },
//                modifier = Modifier.weight(1f),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color(0xFF3E4D58),  // 按钮背景颜色
//                    contentColor = Color.White   // 按钮文字颜色
//                )
//            ) {
//                Text(text = stringResource(id = R.string.disagree))
//            }
//
//            Spacer(modifier = Modifier.width(8.dp))
//
//            Button(
//                onClick = { set(true) },
//                modifier = Modifier.weight(1f),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color(0xFF3A7FBE),  // 按钮背景颜色
//                    contentColor = Color.White   // 按钮文字颜色
//                )
//            ) {
//                Text(text = stringResource(id = R.string.agree))
//            }
//        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashAllowDataCollectionScreenPreview() {
    TGwearTheme {
        SplashAllowDataCollectionScreen { }
    }
}
