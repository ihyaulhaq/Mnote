package com.github.ihyaulhaq.mnote.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.tooling.preview.Preview
import com.github.ihyaulhaq.mnote.ui.components.NeoBrutalButton
import com.github.ihyaulhaq.mnote.ui.theme.NeoBrutalColors
import com.github.ihyaulhaq.mnote.ui.theme.NeoBrutalTheme

@Composable
private fun HomeScreen(){
    NeoBrutalTheme {
        var checked by remember { mutableStateOf(true) }
        var switchOn by remember { mutableStateOf(false) }
        var fieldValue by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(NeoBrutalColors.Background)
        ) {
            NeoBrutalButton(
                text = "1",
                onClick = {}
            )
            NeoBrutalButton(
                text = "1",
                onClick = {}
            )
            NeoBrutalButton(
                text = "1",
                onClick = {}
            )
            NeoBrutalButton(
                text = "1",
                onClick = {}
            )
        }
    }
}

@Preview
@Composable
private fun HomeScreenPrev() {
    HomeScreen()
    
}