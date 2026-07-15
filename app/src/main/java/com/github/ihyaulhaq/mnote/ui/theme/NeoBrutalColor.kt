package com.github.ihyaulhaq.mnote.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Signature neo-brutalist palette: raw, high-contrast, zero gradients.
 * Swap these values to re-skin every component in the kit at once.
 */
object NeoBrutalColors {
    val Background = Color(0xFFFDF6EC) // warm off-white canvas
    val Surface = Color(0xFFFFFFFF)
    val Black = Color(0xFF111111)      // borders / shadows / text — not pure #000
    val White = Color(0xFFFFFFFF)

    val Yellow = Color(0xFFFFC900)
    val Pink = Color(0xFFFF90E8)
    val Blue = Color(0xFF5271FF)
    val Cyan = Color(0xFF6BF3FF)
    val Lime = Color(0xFFA6F24D)
    val Orange = Color(0xFFFF5C38)

    val TextPrimary = Black
    val TextOnDark = White
}
