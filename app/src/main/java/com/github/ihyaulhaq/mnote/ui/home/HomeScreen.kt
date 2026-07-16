package com.github.ihyaulhaq.mnote.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.ihyaulhaq.mnote.ui.components.NButton
import com.github.ihyaulhaq.mnote.ui.components.NTextField
import com.github.ihyaulhaq.mnote.ui.theme.NColors
import com.github.ihyaulhaq.mnote.ui.theme.NeoBrutalTheme

@Composable
fun HomeScreen(){
    NeoBrutalTheme {
        var checked by remember { mutableStateOf(true) }
        var switchOn by remember { mutableStateOf(false) }
        var fieldValue by remember { mutableStateOf("") }

        val numbers = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9")

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(NColors.Background),
                contentAlignment = Alignment.Center
            ){
                Column(
                    modifier = Modifier.width(IntrinsicSize.Max),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // text field
                    NTextField(
                        value = fieldValue,
                        onValueChange = { fieldValue = it },
                        placeholder = "0"
                    )
                    // keypad
                    FlowRow (
                        modifier = Modifier.wrapContentWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        maxItemsInEachRow = 3
                    ) {
                        numbers.forEach { number ->
                            NButton(
                                backgroundColor = NColors.Orange,
                                onClick = { fieldValue += number }
                            ) {
                                Text(
                                    text = number,
                                    fontSize = 30.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )

                            }
                        }
                    }
                    // delete and enter
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement  = Arrangement.SpaceBetween,

                    ) {
                        NButton(
                            backgroundColor = NColors.Red,
                            onClick = {
                                if (fieldValue.isNotEmpty()) fieldValue = fieldValue.dropLast(1)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Backspace,
                                contentDescription = "Delete",
                                tint = NColors.Black
                            )
                        }
                        NButton(
                            backgroundColor = NColors.Green,
                            onClick = {
                                // TODO: "enter"
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Enter",
                                tint = NColors.Black
                            )
                        }
                    }

                }

            }
        }
    }



@Preview
@Composable
private fun HomeScreenPrev() {
    HomeScreen()
    
}