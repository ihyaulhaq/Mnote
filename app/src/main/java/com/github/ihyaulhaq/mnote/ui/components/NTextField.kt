package com.github.ihyaulhaq.mnote.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.ihyaulhaq.mnote.ui.theme.NColors

@Composable
fun NTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    backgroundColor: Color = NColors.White,
) {
    NSurface(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = backgroundColor,
        shadowOffset = 4.dp,
        borderWidth = 3.dp,
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    color = NColors.Black.copy(alpha = 0.4f),
                    fontWeight = FontWeight.Medium
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = LocalTextStyle.current.copy(
                    color = NColors.Black,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .fillMaxWidth(),
                cursorBrush = SolidColor(NColors.Black),
            )
        }
    }
}