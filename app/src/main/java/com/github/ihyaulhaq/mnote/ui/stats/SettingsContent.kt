package com.github.ihyaulhaq.mnote.ui.stats

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.ihyaulhaq.mnote.ui.components.NSurface
import com.github.ihyaulhaq.mnote.ui.theme.NColors

@Composable
fun SettingsContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        NSurface(
            backgroundColor = NColors.White,
            borderWidth = 3.dp,
            shadowOffset = 6.dp,
            cornerRadius = 6.dp
        ) {
            Box(
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Settings — Coming soon",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = NColors.Black.copy(alpha = 0.4f)
                )
            }
        }
    }
}
