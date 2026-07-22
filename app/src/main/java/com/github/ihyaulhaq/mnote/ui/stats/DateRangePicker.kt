package com.github.ihyaulhaq.mnote.ui.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.ihyaulhaq.mnote.ui.components.NButton
import com.github.ihyaulhaq.mnote.ui.theme.NColors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePicker(
    onRangeSelected: (start: Long, end: Long) -> Unit,
    onClear: () -> Unit
) {
    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }
    var startDate by remember { mutableStateOf<Long?>(null) }
    var endDate by remember { mutableStateOf<Long?>(null) }

    val dateFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NButton(
            backgroundColor = if (startDate != null) NColors.Blue else NColors.White,
            modifier = Modifier.weight(1f),
            onClick = { showStartPicker = true }
        ) {
            Text(
                text = startDate?.let { dateFormat.format(Date(it)) } ?: "From",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (startDate != null) NColors.White else NColors.Black
            )
        }

        NButton(
            backgroundColor = if (endDate != null) NColors.Blue else NColors.White,
            modifier = Modifier.weight(1f),
            onClick = { showEndPicker = true }
        ) {
            Text(
                text = endDate?.let { dateFormat.format(Date(it)) } ?: "To",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (endDate != null) NColors.White else NColors.Black
            )
        }

        if (startDate != null && endDate != null) {
            NButton(
                backgroundColor = NColors.Red,
                modifier = Modifier.weight(0.6f),
                onClick = {
                    startDate = null
                    endDate = null
                    onClear()
                }
            ) {
                Text(
                    text = "Clear",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = NColors.White
                )
            }
        }
    }

    if (showStartPicker) {
        val state = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showStartPicker = false },
            confirmButton = {
                NButton(
                    backgroundColor = NColors.Green,
                    onClick = {
                        state.selectedDateMillis?.let {
                            startDate = it
                            showStartPicker = false
                            if (endDate != null) {
                                onRangeSelected(it, endDate!!)
                            }
                        }
                    }
                ) {
                    Text("OK", fontWeight = FontWeight.Bold, color = NColors.White)
                }
            },
            dismissButton = {
                NButton(
                    backgroundColor = NColors.White,
                    onClick = { showStartPicker = false }
                ) {
                    Text("Cancel", fontWeight = FontWeight.Bold, color = NColors.Black)
                }
            }
        ) {
            DatePicker(state = state)
        }
    }

    if (showEndPicker) {
        val state = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showEndPicker = false },
            confirmButton = {
                NButton(
                    backgroundColor = NColors.Green,
                    onClick = {
                        state.selectedDateMillis?.let {
                            endDate = it + 86_399_999L
                            showEndPicker = false
                            if (startDate != null) {
                                onRangeSelected(startDate!!, it + 86_399_999L)
                            }
                        }
                    }
                ) {
                    Text("OK", fontWeight = FontWeight.Bold, color = NColors.White)
                }
            },
            dismissButton = {
                NButton(
                    backgroundColor = NColors.White,
                    onClick = { showEndPicker = false }
                ) {
                    Text("Cancel", fontWeight = FontWeight.Bold, color = NColors.Black)
                }
            }
        ) {
            DatePicker(state = state)
        }
    }
}
