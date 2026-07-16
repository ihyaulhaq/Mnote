package com.github.ihyaulhaq.mnote.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val NeoBrutalColorScheme = lightColorScheme(
    primary = NColors.Black,
    onPrimary = NColors.White,
    background = NColors.Background,
    surface = NColors.Surface,
    onBackground = NColors.Black,
    onSurface = NColors.Black,
)

/**
 * Wrap your screen (or your whole app) in this instead of MaterialTheme.
 * It only sets colors + type — the actual "brutalist" look (borders,
 * hard shadows) lives in the individual components, not here.
 */
@Composable
fun NeoBrutalTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = NeoBrutalColorScheme,
        typography = NeoBrutalTypography,
        content = content
    )
}
