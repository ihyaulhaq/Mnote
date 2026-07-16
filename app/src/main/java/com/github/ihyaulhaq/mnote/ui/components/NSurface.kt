package com.github.ihyaulhaq.mnote.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

import com.github.ihyaulhaq.mnote.ui.theme.NColors

/**
 * The building block of the whole kit: a flat surface with a thick
 * border and a hard, un-blurred offset shadow — the "stuck on with a
 * marker" look that defines neo-brutalism.
 *
 * Every other component (button, card, chip, text field...) is just
 * this surface with different content and padding inside it.
 *
 * @param pressed collapses the shadow to 0, reading as the surface
 * being pushed flat against the page. Used for button press states.
 */
@Composable
fun NSurface(
    modifier: Modifier = Modifier,
    backgroundColor: Color = NColors.White,
    borderColor: Color = NColors.Black,
    shadowColor: Color = NColors.Black,
    borderWidth: Dp = 4.dp,
    shadowOffset: Dp = 5.dp,
    shape: Shape = RectangleShape,
    pressed: Boolean = false,
    content: @Composable BoxScope.() -> Unit = {},
) {
    Box(
        modifier = modifier
            .wrapContentSize()
            .padding(end = shadowOffset, bottom = shadowOffset)
    ) {
        // shadow block
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = shadowOffset, y = shadowOffset)
                .background(shadowColor, shape)
        )

        // Front face
        Box(
            modifier = Modifier
                .offset(
                    x = if (pressed) shadowOffset else 0.dp,
                    y = if (pressed) shadowOffset else 0.dp
                )
                .background(backgroundColor, shape)
                .border(borderWidth, borderColor, shape),
            content = content
        )
    }
}