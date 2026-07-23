package com.github.ihyaulhaq.mnote.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.github.ihyaulhaq.mnote.ui.theme.MnoteTheme
import com.github.ihyaulhaq.mnote.ui.theme.NColors
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

/**
 * Neobrutalist date picker dialog, styled after neobrutalism.dev's
 * date picker (https://www.neobrutalism.dev/docs/date-picker).
 */
@Composable
fun NDatePickerDialog(
    onDismissRequest: () -> Unit,
    onConfirm: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    initialDate: LocalDate = LocalDate.now(),
    minDate: LocalDate? = null,
    maxDate: LocalDate? = null,
    accentColor: Color = NColors.Yellow,
    firstDayOfWeek: DayOfWeek = DayOfWeek.SUNDAY,
    width: Dp = 320.dp,
) {
    var displayedMonth by remember { mutableStateOf(YearMonth.from(initialDate)) }
    var selectedDate by remember { mutableStateOf(initialDate) }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        NSurface(
            modifier = modifier,
            backgroundColor = NColors.Surface,
            borderWidth = 3.dp,
            shadowOffset = 8.dp,
            cornerRadius = 10.dp,
        ) {
            NDatePickerContent(
                modifier = Modifier.width(width),
                displayedMonth = displayedMonth,
                selectedDate = selectedDate,
                minDate = minDate,
                maxDate = maxDate,
                accentColor = accentColor,
                firstDayOfWeek = firstDayOfWeek,
                onPreviousMonth = { displayedMonth = displayedMonth.minusMonths(1) },
                onNextMonth = { displayedMonth = displayedMonth.plusMonths(1) },
                onDaySelected = { date ->
                    selectedDate = date
                    displayedMonth = YearMonth.from(date)
                },
                onCancel = onDismissRequest,
                onConfirm = { onConfirm(selectedDate) },
            )
        }
    }
}

/*
 * Stateless calendar body — the header, month nav, weekday row, day grid
 * and actions. Split out from [NDatePickerDialog] so it's previewable
 * without a real Dialog window.
 */
@Composable
private fun NDatePickerContent(
    displayedMonth: YearMonth,
    selectedDate: LocalDate,
    minDate: LocalDate?,
    maxDate: LocalDate?,
    accentColor: Color,
    firstDayOfWeek: DayOfWeek,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onDaySelected: (LocalDate) -> Unit,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val today = remember { LocalDate.now() }

    Column(modifier = modifier) {

        // ---- Big colored header showing the current selection ----
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(accentColor)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = "SELECTED DATE",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = NColors.Black.copy(alpha = 0.7f),
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = selectedDate.format(DateTimeFormatter.ofPattern("EEE, MMM d, yyyy")),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = NColors.Black,
            )
        }

        HorizontalDivider(thickness = 3.dp, color = NColors.Black)

        Column(modifier = Modifier.padding(16.dp)) {

            // ---- Month navigation ----
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                NButton(onClick = onPreviousMonth) {
                    Text(text = "‹", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                Text(
                    text = "${
                        displayedMonth.month.getDisplayName(
                            TextStyle.FULL,
                            Locale.getDefault()
                        )
                    } ${displayedMonth.year}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
                NButton(onClick = onNextMonth) {
                    Text(text = "›", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.height(12.dp))

            // ---- Weekday header ----
            Row(modifier = Modifier.fillMaxWidth()) {
                weekdayLabels(firstDayOfWeek).forEach { label ->
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Text(
                            text = label,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = NColors.Black.copy(alpha = 0.5f),
                        )
                    }
                }
            }

            Spacer(Modifier.height(4.dp))

            // ---- Day grid ----
            val weeks = remember(displayedMonth, firstDayOfWeek) {
                calendarDays(displayedMonth, firstDayOfWeek).chunked(7)
            }
            weeks.forEach { week ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    week.forEach { date ->
                        val inRange = (minDate == null || !date.isBefore(minDate)) &&
                                (maxDate == null || !date.isAfter(maxDate))
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            NDayCell(
                                date = date,
                                isSelected = date == selectedDate,
                                isToday = date == today,
                                isCurrentMonth = YearMonth.from(date) == displayedMonth,
                                enabled = inRange,
                                accentColor = accentColor,
                                onClick = { onDaySelected(date) },
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // ---- Actions ----
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                NButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f),
                    contentModifier = modifier
                        .fillMaxWidth(),
                    backgroundColor = NColors.White,
                ) {
                    Text(
                        text = "CANCEL"
                    )
                }
                NButton(
                    onClick = onConfirm,
                    modifier = Modifier.weight(1f),
                    contentModifier = modifier
                        .fillMaxWidth(),
                    backgroundColor = accentColor,
                ) {
                    Text(
                        text = "OK"
                    )
                }
            }

        }
    }
}


/*
*  A single day cell. Selected gets a filled + bordered square; today (unselected) gets just a border.
* */
@Composable
private fun NDayCell(
    date: LocalDate,
    isSelected: Boolean,
    isToday: Boolean,
    isCurrentMonth: Boolean,
    enabled: Boolean,
    accentColor: Color,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(8.dp)
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(shape)
            .background(
                color = if (isSelected) accentColor else Color.Transparent,
                shape = shape,
            )
            .then(
                if (isSelected || isToday) Modifier.border(2.dp, NColors.Black, shape) else Modifier
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            fontSize = 14.sp,
            fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Medium,
            color = when {
                !enabled -> NColors.Black.copy(alpha = 0.15f)
                !isCurrentMonth -> NColors.Black.copy(alpha = 0.25f)
                else -> NColors.Black
            },
        )
    }
}

/* Weekday header labels, starting from [firstDayOfWeek]. */
private fun weekdayLabels(firstDayOfWeek: DayOfWeek): List<String> {
    val labels = listOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa") // index 0 = Sunday
    val offset = firstDayOfWeek.value % 7
    return labels.drop(offset) + labels.take(offset)
}

/* 42 dates (6 full weeks) spanning the grid for [yearMonth], padded with adjacent-month days. */
private fun calendarDays(yearMonth: YearMonth, firstDayOfWeek: DayOfWeek): List<LocalDate> {
    val firstOfMonth = yearMonth.atDay(1)
    val desiredOffset = firstDayOfWeek.value % 7
    val actualOffset = firstOfMonth.dayOfWeek.value % 7
    val daysBefore = (actualOffset - desiredOffset + 7) % 7
    val startDate = firstOfMonth.minusDays(daysBefore.toLong())
    return (0 until 42).map { startDate.plusDays(it.toLong()) }
}


@Preview(showBackground = true)
@Composable
private fun NDatePickerContentPreview() {
    MnoteTheme {
        NSurface(
            backgroundColor = NColors.Surface,
            borderWidth = 3.dp,
            shadowOffset = 8.dp,
            cornerRadius = 10.dp,
        ) {
            NDatePickerContent(
                modifier = Modifier.width(320.dp),
                displayedMonth = YearMonth.now(),
                selectedDate = LocalDate.now(),
                minDate = null,
                maxDate = null,
                accentColor = NColors.Yellow,
                firstDayOfWeek = DayOfWeek.SUNDAY,
                onPreviousMonth = {},
                onNextMonth = {},
                onDaySelected = {},
                onCancel = {},
                onConfirm = {},
            )
        }
    }
}

/**
 * Full usage example: a trigger button (à la neobrutalism.dev's
 * "Pick a date" button) that opens [NDatePickerDialog].
 */

@Preview(showBackground = true)
@Composable
private fun NDatePickerDemo() {
    MnoteTheme {
        var showDialog by remember { mutableStateOf(false) }
        var pickedDate by remember { mutableStateOf<LocalDate?>(null) }

        Box(modifier = Modifier.padding(24.dp)) {
            NButton(
                onClick = { showDialog = true },
                contentModifier  = Modifier.width(220.dp),
            ){
                Text(
                    text = pickedDate
                        ?.format(DateTimeFormatter.ofPattern("EEE, MMM d, yyyy"))
                        ?: "Pick a date",
                )
            }
        }

        if (showDialog) {
            NDatePickerDialog(
                onDismissRequest = { showDialog = false },
                onConfirm = { date ->
                    pickedDate = date
                    showDialog = false
                },
                initialDate = pickedDate ?: LocalDate.now(),
            )
        }
    }
}
