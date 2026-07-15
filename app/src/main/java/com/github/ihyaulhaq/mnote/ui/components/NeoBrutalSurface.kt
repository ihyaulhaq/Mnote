package com.github.ihyaulhaq.mnote.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

import com.github.ihyaulhaq.mnote.ui.theme.NeoBrutalColors

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
fun NeoBrutalSurface(
    modifier: Modifier = Modifier,
    backgroundColor: Color = NeoBrutalColors.White,
    borderColor: Color = NeoBrutalColors.Black,
    shadowColor: Color = NeoBrutalColors.Black,
    borderWidth: Dp = 3.dp,
    shadowOffset: Dp = 6.dp,
    shape: Shape = RectangleShape,
    pressed: Boolean = false,
    content: @Composable BoxScope.() -> Unit = {},
) {
    val liveOffset = if (pressed) 0.dp else shadowOffset

    // Reserve extra space at the end/bottom so the shadow has room to
    // render without overlapping whatever sits next to this component.
    Box(modifier = modifier.padding(end = shadowOffset, bottom = shadowOffset)) {
        // Hard shadow block, sized to match the front face, offset
        // down-and-right from it.
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = liveOffset, y = liveOffset)
                .background(shadowColor, shape)
        )

        // Front face — this is the child that determines the overall
        // size of the surface, based on its content.
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