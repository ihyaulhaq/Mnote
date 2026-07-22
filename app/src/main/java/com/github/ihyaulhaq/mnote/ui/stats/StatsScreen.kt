package com.github.ihyaulhaq.mnote.ui.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.ihyaulhaq.mnote.MnoteApp
import com.github.ihyaulhaq.mnote.ui.components.NButton
import com.github.ihyaulhaq.mnote.ui.components.NSurface
import com.github.ihyaulhaq.mnote.ui.home.ExpenseViewModel
import com.github.ihyaulhaq.mnote.ui.home.ExpenseViewModelFactory
import com.github.ihyaulhaq.mnote.ui.theme.MnoteTheme
import com.github.ihyaulhaq.mnote.ui.theme.NColors

private data class TabItem(
    val icon: ImageVector,
    val label: String
)

private val tabs = listOf(
    TabItem(Icons.Default.BarChart, "Chart"),
    TabItem(Icons.AutoMirrored.Filled.List, "Table"),
    TabItem(Icons.Default.Settings, "Settings")
)

@Composable
fun StatsScreen(
    onNavigateBack: () -> Unit,
    viewModel: ExpenseViewModel = viewModel(
        factory = ExpenseViewModelFactory(
            LocalContext.current.applicationContext as MnoteApp
        )
    )
) {
    MnoteTheme {
        var selectedTab by remember { mutableIntStateOf(0) }
        val filteredExpenses by viewModel.filteredExpenses.collectAsState()
        val categories by viewModel.categories.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(NColors.Background)
        ) {
            // top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                NButton(
                    backgroundColor = NColors.White,
                    contentPadding = PaddingValues(12.dp),
                    contentSize = 40.dp,
                    onClick = onNavigateBack
                ) {
                    Text(
                        text = "<",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = NColors.Black
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Stats",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = NColors.Black
                )
            }

            // date range picker
            DateRangePicker(
                onRangeSelected = { start, end -> viewModel.setDateRange(start, end) },
                onClear = { viewModel.clearDateRange() }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // content area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                when (selectedTab) {
                    0 -> PieChartContent(filteredExpenses)
                    1 -> TableContent(
                        expenses = filteredExpenses,
                        categories = categories,
                        onEdit = { viewModel.updateExpense(it) },
                        onDelete = { viewModel.deleteExpense(it) }
                    )
                    2 -> SettingsContent()
                }
            }

            // bottom tab bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tabs.forEachIndexed { index, tab ->
                    val isSelected = index == selectedTab
                    val interactionSource = remember { MutableInteractionSource() }
                    val isPressed by interactionSource.collectIsPressedAsState()

                    NSurface(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null,
                                onClick = { selectedTab = index }
                            ),
                        backgroundColor = if (isSelected) NColors.Blue else NColors.White,
                        pressed = isPressed,
                        shadowOffset = if (isSelected) 2.dp else 6.dp,
                        borderWidth = 3.dp,
                        cornerRadius = 6.dp
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.label,
                                tint = if (isSelected) NColors.White else NColors.Black,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StatsScreenPrev() {
    StatsScreen(onNavigateBack = {})
}
