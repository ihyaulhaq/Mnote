package com.github.ihyaulhaq.mnote.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.ihyaulhaq.mnote.MnoteApp
import com.github.ihyaulhaq.mnote.ui.components.NButton
import com.github.ihyaulhaq.mnote.ui.components.NSurface
import com.github.ihyaulhaq.mnote.ui.components.NTextField
import com.github.ihyaulhaq.mnote.ui.theme.MnoteTheme
import com.github.ihyaulhaq.mnote.ui.theme.NColors

@Composable
fun HomeScreen(
    onNavigateToStats: () -> Unit = {},
    viewModel: ExpenseViewModel = viewModel(
        factory = ExpenseViewModelFactory(
            LocalContext.current.applicationContext as MnoteApp
        )
    )
) {
    MnoteTheme {
        var fieldValue by remember { mutableStateOf("") }
        val categories by viewModel.categories.collectAsState()

        // Modal state
        var showModal by remember { mutableStateOf(false) }
        var pendingAmount by remember { mutableDoubleStateOf(0.0) }
        var modalCategoryId by remember { mutableStateOf(0L) }
        var modalDesc by remember { mutableStateOf("") }

        val numbers = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9")

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(NColors.Background),
            contentAlignment = Alignment.Center
        ) {
            // Floating button in upper left corner
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            ) {
                NButton(
                    backgroundColor = NColors.White,
                    contentPadding = PaddingValues(12.dp),
                    contentSize = 48.dp,
                    onClick = onNavigateToStats
                ) {
                    Icon(
                        imageVector = Icons.Default.BarChart,
                        contentDescription = "Statistics",
                        tint = NColors.Black
                    )
                }
            }

            Column(
                modifier = Modifier.width(IntrinsicSize.Max),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // text field
                NTextField(
                    value = fieldValue,
                    modifier = Modifier.height(100.dp),
                    onValueChange = { fieldValue = it },
                    placeholder = "0"
                )

                // keypad
                FlowRow(
                    modifier = Modifier.wrapContentWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    maxItemsInEachRow = 3
                ) {
                    numbers.forEach { number ->
                        NButton(
                            backgroundColor = NColors.Orange,
                            contentPadding = PaddingValues(20.dp),
                            contentSize = 50.dp,
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    NButton(
                        backgroundColor = NColors.Red,
                        contentPadding = PaddingValues(20.dp),
                        contentSize = 50.dp,
                        onClick = {
                            if (fieldValue.isNotEmpty()) fieldValue = fieldValue.dropLast(1)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Backspace,
                            contentDescription = "Delete",
                            tint = NColors.Black
                        )
                    }
                    NButton(
                        backgroundColor = NColors.Green,
                        contentPadding = PaddingValues(20.dp),
                        contentSize = 50.dp,
                        onClick = {
                            val amount = fieldValue.toDoubleOrNull()
                            if (amount != null && amount > 0) {
                                pendingAmount = amount
                                modalCategoryId = categories.firstOrNull()?.id ?: 0L
                                modalDesc = ""
                                showModal = true
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Enter",
                            modifier = Modifier.size(35.dp),
                            tint = NColors.Black
                        )
                    }
                }

            }

            // Modal overlay
            if (showModal) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(NColors.Black.copy(alpha = 0.5f))
                        .clickable(
                            interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                            indication = null,
                            onClick = { showModal = false }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    NSurface(
                        modifier = Modifier
                            .width(IntrinsicSize.Max)
                            .clickable(
                                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                                indication = null,
                                onClick = { }
                            ),
                        shadowOffset = 8.dp,
                        borderWidth = 3.dp,
                        cornerRadius = 6.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(20.dp)
                                .width(IntrinsicSize.Max),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // category picker
                            FlowRow(
                                modifier = Modifier.wrapContentWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                maxItemsInEachRow = 3
                            ) {
                                categories.forEach { category ->
                                    val isSelected = category.id == modalCategoryId
                                    NButton(
                                        backgroundColor = if (isSelected) NColors.Blue else NColors.White,
                                        contentPadding = PaddingValues(
                                            horizontal = 16.dp,
                                            vertical = 10.dp
                                        ),
                                        contentSize = 40.dp,
                                        onClick = { modalCategoryId = category.id }
                                    ) {
                                        Text(
                                            text = category.name,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) NColors.White else NColors.Black
                                        )
                                    }
                                }
                            }

                            // description field
                            NTextField(
                                value = modalDesc,
                                modifier = Modifier.height(60.dp),
                                onValueChange = { modalDesc = it },
                                placeholder = "note (optional)"
                            )

                            // confirm and cancel
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                NButton(
                                    backgroundColor = NColors.Green,
                                    contentPadding = PaddingValues(
                                        horizontal = 20.dp,
                                        vertical = 10.dp
                                    ),
                                    contentSize = 40.dp,
                                    onClick = {
                                        if (modalCategoryId != 0L) {
                                            viewModel.addExpense(
                                                pendingAmount,
                                                modalCategoryId,
                                                modalDesc
                                            )
                                            fieldValue = ""
                                            showModal = false
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Save,
                                        contentDescription = "Save",
                                        modifier = Modifier.size(35.dp),
                                        tint = NColors.Orange
                                    )
                                }
                            }
                        }
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
