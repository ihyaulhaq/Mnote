package com.github.ihyaulhaq.mnote.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.ihyaulhaq.mnote.ui.theme.NeoBrutalColors

@Composable
fun NeoBrutalButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = NeoBrutalColors.Yellow,
    contentColor: Color = NeoBrutalColors.Black,
    enabled: Boolean = true,
) {

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    NeoBrutalSurface(
        modifier = modifier.clickable(
            interactionSource = interactionSource,
            indication = null,
            enabled = enabled,
            onClick = onClick
        ),
        backgroundColor = if (enabled) backgroundColor else NeoBrutalColors.Surface,
        pressed = isPressed,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                color = if (enabled) contentColor else NeoBrutalColors.Black.copy(alpha = 0.35f),
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
private fun NeoBrutalButtonPreview() {
    NeoBrutalButton(text = "PRIMARY", onClick = {})
    
}
