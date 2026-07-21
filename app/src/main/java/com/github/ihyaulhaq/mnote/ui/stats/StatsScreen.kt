package com.github.ihyaulhaq.mnote.ui.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.github.ihyaulhaq.mnote.ui.theme.NColors

@Composable
fun StatsScreen(onNavigateBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NColors.Background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Stats Screen (Placeholder)",
            color = NColors.Black
        )
    }
}
