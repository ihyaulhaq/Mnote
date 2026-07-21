//package com.github.ihyaulhaq.mnote.ui.theme
//
//import androidx.compose.material3.Typography
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.sp
//
//// Set of Material typography styles to start with
//val Typography = Typography(
//    bodyLarge = TextStyle(
//        fontFamily = FontFamily.Default,
//        fontWeight = FontWeight.Normal,
//        fontSize = 16.sp,
//        lineHeight = 24.sp,
//        letterSpacing = 0.5.sp
//    )
//    /* Other default text styles to override
//    titleLarge = TextStyle(
//        fontFamily = FontFamily.Default,
//        fontWeight = FontWeight.Normal,
//        fontSize = 22.sp,
//        lineHeight = 28.sp,
//        letterSpacing = 0.sp
//    ),
//    labelSmall = TextStyle(
//        fontFamily = FontFamily.Default,
//        fontWeight = FontWeight.Medium,
//        fontSize = 11.sp,
//        lineHeight = 16.sp,
//        letterSpacing = 0.5.sp
//    )
//    */
//)

package com.github.ihyaulhaq.mnote.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Neo-brutalism leans on heavy, chunky display type.
 *
 * This uses the system default font at heavy weights so the kit works
 * with zero extra setup. For the full effect, swap [family] for a
 * downloaded Google Font like "Archivo Black" or "Space Grotesk" —
 * see the README for how to wire up a downloadable font.
 */
object NeoBrutalFont {
    val family = FontFamily.Default
}

val NeoBrutalTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = NeoBrutalFont.family,
        fontWeight = FontWeight.Black,
        fontSize = 40.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = NeoBrutalFont.family,
        fontWeight = FontWeight.Black,
        fontSize = 28.sp
    ),
    titleLarge = TextStyle(
        fontFamily = NeoBrutalFont.family,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 22.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = NeoBrutalFont.family,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),
    labelLarge = TextStyle(
        fontFamily = NeoBrutalFont.family,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        letterSpacing = 0.5.sp
    ),
)
