package com.github.ihyaulhaq.mnote.ui.stats

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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
    // Local editable state, pre-filled from the existing expense
    var amountText by remember {
        mutableStateOf(
            expenseWithCategory.expense.amount.toLong().toString()
        )
    }
    var selectedCategoryId by remember { mutableStateOf(expenseWithCategory.expense.categoryId) }
    var desc by remember { mutableStateOf(expenseWithCategory.expense.desc) }
    val context = LocalContext.current


    // Dark scrim overlay — tapping dismisses the dialog
    Dialog (
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(NColors.Black.copy(alpha = 0.5f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismiss
                ),
            contentAlignment = Alignment.Center
        ) {
            // Neo-brutalist card
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
                    // Title
                    Text(
                        text = "Edit Expense",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = NColors.Black
                    )

                    // Amount input (digits only)
                    NTextField(
                        value = amountText,
                        modifier = Modifier.height(60.dp),
                        onValueChange = { amountText = it.filter { c -> c.isDigit() } },
                        placeholder = "Amount"
                    )

                    // Category chip picker — wraps into rows of 3
                    FlowRow(
                        modifier = Modifier.wrapContentWidth(),
                        horizontalArrangement = Arrangement.spacedBy(
                            8.dp,
                            alignment = Alignment.CenterHorizontally
                        ),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        maxItemsInEachRow = 3
                    ) {
                        categories.forEach { category ->
                            val isSelected = category.id == selectedCategoryId
                            NButton(
                                backgroundColor = if (isSelected) NColors.Blue else NColors.White,
                                contentPadding = PaddingValues(horizontal = 5.dp, vertical = 3.dp),
                                contentModifier = Modifier
                                    .wrapContentWidth()
                                    .height(20.dp),
                                onClick = { selectedCategoryId = category.id }
                            ) {
                                Text(
                                    text = category.name,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) NColors.White else NColors.Black
                                )
                            }
                        }
                    }

                    // Optional description input
                    NTextField(
                        value = desc,
                        modifier = Modifier.height(60.dp),
                        onValueChange = { desc = it },
                        placeholder = "Desc (optional)"
                    )

                    // Save + Cancel buttons
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        NButton(
                            backgroundColor = NColors.Green,
                            contentModifier = Modifier.height(55.dp),
                            contentPadding = PaddingValues(
                                horizontal = 20.dp,
                                vertical = 10.dp
                            ),
                            contentSize = 50.dp,
                            onClick = {
                                val amount = amountText.toDoubleOrNull()
                                if (amount != null && amount > 0) {
                                    onSave(
                                        expenseWithCategory.expense.copy(
                                            amount = amount,
                                            categoryId = selectedCategoryId,
                                            desc = desc
                                        )
                                    )
                                } else {
                                    Toast.makeText(context, "Invalid amount", Toast.LENGTH_SHORT).show()
                                }
                            }
                        ) {
                            Text(
                                text = "Save",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = NColors.White
                            )
                        }
                        NButton(
                            backgroundColor = NColors.White,
                            contentModifier = Modifier.height(55.dp),
                            contentPadding = PaddingValues(
                                horizontal = 20.dp,
                                vertical = 10.dp
                            ),
                            contentSize = 50.dp,
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
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EditExpenseDialogPreview() {
    val categories = listOf(
        Category(id = 1, name = "Food"),
        Category(id = 2, name = "main "),
        Category(id = 3, name = "makanmakanmakan ")
    )
    val expense = ExpenseWithCategory(
        expense = Expense(
            id = 1,
            amount = 100.0,
            categoryId = 1,
            desc = "Lunch with friends"
        ), category = Category(id = 1, name = "Food")
    )
    EditExpenseDialog(
        expenseWithCategory = expense,
        categories = categories,
        onSave = {},
        onDismiss = {}
    )
}