package com.github.ihyaulhaq.mnote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.github.ihyaulhaq.mnote.ui.components.NButton
import com.github.ihyaulhaq.mnote.ui.home.HomeScreen
import com.github.ihyaulhaq.mnote.ui.theme.MnoteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MnoteTheme {
                HomeScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MnoteTheme {
        HomeScreen()
    }
}