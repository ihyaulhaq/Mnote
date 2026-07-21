package com.github.ihyaulhaq.mnote.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.ihyaulhaq.mnote.ui.theme.NColors

@Composable
fun NButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    contentSize: Dp = 30.dp,
    backgroundColor: Color = NColors.White,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    NSurface(
        modifier = modifier.clickable(
            interactionSource = interactionSource,
            indication = null,
            enabled = enabled,
            onClick = onClick
        ),
        backgroundColor = if (enabled) backgroundColor else NColors.Surface,
        pressed = isPressed,
    ) {
        Row(
            modifier = Modifier
                .padding(contentPadding)
                .size(contentSize),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            content()
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun NButtonPreview() {
    NButton(
        backgroundColor = NColors.Red,
        onClick = {}
    ) {
        Text(
            text = "test"
        )
    }

}
