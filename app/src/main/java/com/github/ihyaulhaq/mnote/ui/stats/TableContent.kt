package com.github.ihyaulhaq.mnote.ui.stats

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.ihyaulhaq.mnote.data.local.Category
import com.github.ihyaulhaq.mnote.data.local.Expense
import com.github.ihyaulhaq.mnote.data.local.ExpenseWithCategory
import com.github.ihyaulhaq.mnote.ui.components.NSurface
import com.github.ihyaulhaq.mnote.ui.theme.NColors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TableContent(
    expenses: List<ExpenseWithCategory>,
    categories: List<Category>,
    onEdit: (Expense) -> Unit,
    onDelete: (Long) -> Unit
) {
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

    var editingExpense by remember { mutableStateOf<ExpenseWithCategory?>(null) }

    val dateFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Date",
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = NColors.Black,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "Category",
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = NColors.Black,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Amount",
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = NColors.Black,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End
            )
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(expenses, key = { it.expense.id }) { item ->
                val dismissState = rememberSwipeToDismissBoxState(
                    confirmValueChange = { value ->
                        if (value == SwipeToDismissBoxValue.EndToStart) {
                            onDelete(item.expense.id)
                            true
                        } else false
                    }
                )

                SwipeToDismissBox(
                    state = dismissState,
                    backgroundContent = {
                        val color by animateColorAsState(
                            if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart)
                                NColors.Red else Color.Transparent,
                            label = "swipe_bg"
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color)
                                .padding(end = 16.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = NColors.White
                            )
                        }
                    },
                    enableDismissFromStartToEnd = false
                ) {
                    NSurface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { editingExpense = item },
                        backgroundColor = NColors.White,
                        borderWidth = 2.dp,
                        shadowOffset = 3.dp,
                        cornerRadius = 4.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = dateFormat.format(Date(item.expense.timestamp)),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = NColors.Black,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = item.category.name,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = NColors.Black,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Rp ${item.expense.amount.toLong()}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = NColors.Black,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.End
                            )
                        }
                    }
                }
            }
        }
    }

    editingExpense?.let { ewc ->
        EditExpenseDialog(
            expenseWithCategory = ewc,
            categories = categories,
            onSave = { updated ->
                onEdit(updated)
                editingExpense = null
            },
            onDismiss = { editingExpense = null }
        )
    }
}
