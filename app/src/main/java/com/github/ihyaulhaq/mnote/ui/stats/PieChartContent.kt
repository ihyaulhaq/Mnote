package com.github.ihyaulhaq.mnote.ui.stats

import android.graphics.Color
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.ihyaulhaq.mnote.data.local.ExpenseWithCategory
import com.github.ihyaulhaq.mnote.ui.theme.NColors
import com.github.mikephil.charting.charts.PieChart as MPPieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter

internal val chartColors = intArrayOf(
    Color.parseColor("#F7A072"), // Orange
    Color.parseColor("#4ECDC4"), // Green
    Color.parseColor("#5271FF"), // Blue
    Color.parseColor("#FFC900"), // Yellow
    Color.parseColor("#A388EE"), // Purple
    Color.parseColor("#FF90E8"), // Pink
    Color.parseColor("#FF6B6B"), // Red
    Color.parseColor("#6BF3FF"), // Cyan
    Color.parseColor("#A6F24D"), // Lime
)

@Composable
fun PieChartContent(expenses: List<ExpenseWithCategory>) {
    if (expenses.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No expenses yet",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = NColors.Black.copy(alpha = 0.4f)
            )
        }
        return
    }

    val grouped = expenses.groupBy { it.category.name }
        .mapValues { (_, items) -> items.sumOf { it.expense.amount } }
        .toList()
        .sortedByDescending { it.second }

    val entries = grouped.map { (category, total) ->
        PieEntry(total.toFloat(), category)
    }

    AndroidView(
        factory = { context ->
            MPPieChart(context).apply {
                description.isEnabled = false
                isDrawHoleEnabled = true
                setHoleColor(Color.TRANSPARENT)
                holeRadius = 45f
                transparentCircleRadius = 50f
                setDrawEntryLabels(false)
                legend.isEnabled = true
                legend.textColor = Color.BLACK
                legend.textSize = 13f
                setUsePercentValues(true)
                setCenterTextSize(16f)
                setCenterTextColor(Color.BLACK)
                setExtraOffsets(16f, 8f, 16f, 8f)
            }
        },
        update = { chart ->
            val dataSet = PieDataSet(entries, "").apply {
                colors = chartColors.toList()
                sliceSpace = 3f
                selectionShift = 8f
                valueTextSize = 11f
                valueTextColor = Color.BLACK
                valueFormatter = PercentFormatter(chart)
            }
            chart.data = PieData(dataSet)
            chart.invalidate()
        },
        modifier = Modifier.fillMaxSize()
    )
}
