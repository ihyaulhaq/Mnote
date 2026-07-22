package com.github.ihyaulhaq.mnote.ui.stats

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
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
import com.github.ihyaulhaq.mnote.data.local.Category
import com.github.ihyaulhaq.mnote.data.local.Expense
import com.github.ihyaulhaq.mnote.data.local.ExpenseWithCategory
import com.github.ihyaulhaq.mnote.ui.components.NButton
import com.github.ihyaulhaq.mnote.ui.components.NSurface
import com.github.ihyaulhaq.mnote.ui.components.NTextField
import com.github.ihyaulhaq.mnote.ui.theme.NColors

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditExpenseDialog(
    expenseWithCategory: ExpenseWithCategory,
    categories: List<Category>,
    onSave: (Expense) -> Unit,
    onDismiss: () -> Unit
) {
    var amountText by remember { mutableStateOf(expenseWithCategory.expense.amount.toLong().toString()) }
    var selectedCategoryId by remember { mutableStateOf(expenseWithCategory.expense.categoryId) }
    var desc by remember { mutableStateOf(expenseWithCategory.expense.desc) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = NColors.Background,
        title = {
            Text(
                text = "Edit Expense",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = NColors.Black
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                NTextField(
                    value = amountText,
                    onValueChange = { amountText = it.filter { c -> c.isDigit() } },
                    placeholder = "Amount"
                )

                Text(
                    text = "Category",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = NColors.Black
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { category ->
                        val isSelected = category.id == selectedCategoryId
                        NSurface(
                            modifier = Modifier.clickable { selectedCategoryId = category.id },
                            backgroundColor = if (isSelected) NColors.Blue else NColors.White,
                            shadowOffset = if (isSelected) 2.dp else 4.dp,
                            borderWidth = 2.dp,
                            cornerRadius = 4.dp
                        ) {
                            Box(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                contentAlignment = Alignment.Center
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
                }

                NTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    placeholder = "Description (optional)"
                )
            }
        },
        confirmButton = {
            NButton(
                backgroundColor = NColors.Green,
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    val amount = amountText.toDoubleOrNull() ?: return@NButton
                    if (amount <= 0) return@NButton
                    onSave(
                        expenseWithCategory.expense.copy(
                            amount = amount,
                            categoryId = selectedCategoryId,
                            desc = desc
                        )
                    )
                }
            ) {
                Text(
                    text = "Save",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = NColors.White
                )
            }
        },
        dismissButton = {
            NButton(
                backgroundColor = NColors.White,
                modifier = Modifier.fillMaxWidth(),
                onClick = onDismiss
            ) {
                Text(
                    text = "Cancel",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = NColors.Black
                )
            }
        }
    )
}
