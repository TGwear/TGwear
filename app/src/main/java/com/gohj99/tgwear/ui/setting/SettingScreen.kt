/*
 * Copyright (c) 2024-2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.gohj99.tgwear.ui.setting

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material3.ScrollIndicator
import com.gohj99.tgwear.R
import com.gohj99.tgwear.model.SettingItem
import com.gohj99.tgwear.ui.AutoScrollingText
import com.gohj99.tgwear.ui.theme.TGwearTheme

@Composable
fun SplashSettingScreen(
    title: String,
    settings: MutableState<List<SettingItem>>
) {
    val listState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // 包含 Row 的 Box
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 48.dp, end = 48.dp, top = 14.dp) // 添加顶部填充
        ) {
            AutoScrollingText(
                text = title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp)) // 添加间距

        SettingLazyColumn(settings, getListState = listState)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        ScrollIndicator(
            state = listState,
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashSettingScreenPreview() {
    TGwearTheme {
        val settings = remember {
            mutableStateOf(
                listOf(
                    SettingItem.Click(
                        itemName = "设置1",
                        onClick = {}
                    ),
                    SettingItem.Switch(
                        itemName = "设置2",
                        isSelected = true,
                        onSelect = {}
                    ),
                    SettingItem.Switch(
                        itemName = "设置3",
                        isSelected = true,
                        onSelect = {}
                    ),
                    SettingItem.ProgressBar(
                        itemName = "设置4",
                        progress = 0f,
                        maxValue = 100f,
                        minValue = 0f,
                        base = 1f,
                        onProgressChange = {}
                    )
                )
            )
        }
        SplashSettingScreen(
            title = stringResource(id = R.string.Settings),
            settings = settings
        )
    }
}
