package com.github.ihyaulhaq.mnote.ui.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.github.ihyaulhaq.mnote.ui.components.NDatePickerDialog
import com.github.ihyaulhaq.mnote.ui.theme.NColors
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePicker(
    onRangeSelected: (start: Long, end: Long) -> Unit, onClear: () -> Unit
) {
    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }
    var startDate by remember { mutableStateOf<Long?>(null) }
    var endDate by remember { mutableStateOf<Long?>(null) }

    val dateFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())

    // Row with start date, end date, and clear button
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NButton(
            backgroundColor = if (startDate != null) NColors.Blue else NColors.White,
            modifier = Modifier.weight(1f),
            contentModifier = Modifier.width(150.dp),
            onClick = { showStartPicker = true },
        ) {
            Text(text = startDate?.let { dateFormat.format(Date(it)) } ?: "From",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (startDate != null) NColors.White else NColors.Black)
        }

        NButton(
            backgroundColor = if (endDate != null) NColors.Blue else NColors.White,
            modifier = Modifier.weight(1f),
            contentModifier = Modifier.width(150.dp),
            onClick = { showEndPicker = true }) {
            Text(text = endDate?.let { dateFormat.format(Date(it)) } ?: "To",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (endDate != null) NColors.White else NColors.Black)
        }

        if (startDate != null && endDate != null) {
            NButton(
                backgroundColor = NColors.Red, modifier = Modifier.weight(1f), onClick = {
                    startDate = null
                    endDate = null
                    onClear()
                }) {
                Text(
                    text = "Clear",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = NColors.White
                )
            }
        }
    }

    /*
     * Displays a date picker dialog and converts the selected LocalDate
     * into a Unix timestamp for storage and filtering.
     *
     * The selected date is normalized to the start of the day (00:00)
     * in the device's local time zone
     *
     * Once confirmed, the dialog closes and notifies the parent if a
     * complete date range has been selected.
     */
    // start date
    if (showStartPicker) {
        NDatePickerDialog(
            onDismissRequest = { showStartPicker = false },
            onConfirm = { date ->
                val millis = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                startDate = millis
                showStartPicker = false
                endDate?.let { onRangeSelected(millis, it) }

            },
            initialDate = startDate?.let {
                Date(it).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            } ?: LocalDate.now(),
            maxDate = endDate?.let {
                Date(it).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            },
        )
    }

    // end date
    if (showEndPicker) {
        NDatePickerDialog(
            onDismissRequest = { showEndPicker = false },
            onConfirm = { date ->
                val millis = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                endDate = millis
                showEndPicker = false
                startDate?.let { onRangeSelected(it, millis) }
            },
            initialDate = endDate?.let {
                Date(it).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            } ?: LocalDate.now(),
            minDate = startDate?.let {
                Date(it).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            },
        )
    }

}

@Preview(showBackground = true)
@Composable
fun DateRangePickerPreview() {
    DateRangePicker(onRangeSelected = { _, _ -> }, onClear = {})
}
