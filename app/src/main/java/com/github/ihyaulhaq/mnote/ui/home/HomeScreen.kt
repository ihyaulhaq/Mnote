package com.github.ihyaulhaq.mnote.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.LaunchedEffect
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
import android.widget.Toast
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.ihyaulhaq.mnote.MnoteApp
import com.github.ihyaulhaq.mnote.ui.components.NButton
import com.github.ihyaulhaq.mnote.ui.components.NSurface
import com.github.ihyaulhaq.mnote.ui.components.NTextField
import com.github.ihyaulhaq.mnote.viewmodel.CategoryViewModel
import com.github.ihyaulhaq.mnote.viewmodel.ExpenseViewModel
import com.github.ihyaulhaq.mnote.viewmodel.sharedCategoryViewModel
import com.github.ihyaulhaq.mnote.ui.theme.MnoteTheme
import com.github.ihyaulhaq.mnote.ui.theme.NColors

@Composable
fun HomeScreen(
    onNavigateToStats: () -> Unit = {},
    expenseViewModel: ExpenseViewModel = viewModel(
        factory = (LocalContext.current.applicationContext as MnoteApp).appFactory
    ),
    categoryViewModel: CategoryViewModel = sharedCategoryViewModel()
) {
    MnoteTheme {
        var fieldValue by remember { mutableStateOf("") }
        val categories by categoryViewModel.categories.collectAsState()
        val context = LocalContext.current
        val error by expenseViewModel.error.collectAsState()

        LaunchedEffect(error) {
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                expenseViewModel.clearError()
            }
        }

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
                NTextField(
                    value = fieldValue,
                    modifier = Modifier.height(100.dp),
                    onValueChange = { fieldValue = it },
                    placeholder = "0"
                )

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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    NButton(
                        backgroundColor = NColors.Red,
                        contentPadding = PaddingValues(15.dp),
                        contentSize = 60.dp,
                        onClick = {
                            if (fieldValue.isNotEmpty()) fieldValue = fieldValue.dropLast(1)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Backspace,
                            modifier = Modifier.size(35.dp),
                            contentDescription = "Delete",
                            tint = NColors.Black
                        )
                    }
                    NButton(
                        backgroundColor = NColors.Green,
                        contentPadding = PaddingValues(15.dp),
                        contentSize = 60.dp,
                        onClick = {
                            val amount = fieldValue.toDoubleOrNull()
                            if (amount != null && amount > 0) {
                                pendingAmount = amount
                                modalCategoryId = categories.firstOrNull()?.id ?: 0L
                                modalDesc = ""
                                showModal = true
                            } else {
                                Toast.makeText(context, "Invalid amount", Toast.LENGTH_SHORT).show()
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

            if (showModal) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(NColors.Black.copy(alpha = 0.5f))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { showModal = false }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    NSurface(
                        modifier = Modifier
                            .width(IntrinsicSize.Max)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
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
                                            horizontal = 5.dp,
                                            vertical = 3.dp
                                        ),
                                        contentSize = 20.dp,
                                        onClick = { modalCategoryId = category.id }
                                    ) {
                                        Text(
                                            text = category.name,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) NColors.White else NColors.Black
                                        )
                                    }
                                }
                            }

                            NTextField(
                                value = modalDesc,
                                modifier = Modifier.height(60.dp),
                                onValueChange = { modalDesc = it },
                                placeholder = "note (optional)"
                            )

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
                                            expenseViewModel.addExpense(
                                                pendingAmount,
                                                modalCategoryId,
                                                modalDesc
                                            )
                                            fieldValue = ""
                                            showModal = false
                                        } else {
                                            Toast.makeText(context, "Categories not loaded yet", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Save,
                                        contentDescription = "Save",
                                        modifier = Modifier.size(35.dp),
                                        tint = NColors.Surface
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
