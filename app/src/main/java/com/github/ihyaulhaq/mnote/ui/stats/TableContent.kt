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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxDefaults
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.github.ihyaulhaq.mnote.data.local.Category
import com.github.ihyaulhaq.mnote.data.local.Expense
import com.github.ihyaulhaq.mnote.data.local.ExpenseWithCategory
import com.github.ihyaulhaq.mnote.ui.components.NButton
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
    var pendingDeleteId by remember { mutableStateOf<Long?>(null) }

    val dateFormat = remember { SimpleDateFormat("dd/MM/yy", Locale.getDefault()) }

    // Column: header + scrollable expense list
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Table header row
        NSurface(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = NColors.Blue,
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
                    text = "Date",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = NColors.White,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Category",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = NColors.White,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Amount",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = NColors.White,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
            }
        }

        // Scrollable list of expense rows
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(expenses, key = { it.expense.id }) { item ->
                // Swipe-to-dismiss
                // show a confirmation dialog instead of deleting immediately.
                val dismissState = rememberSwipeToDismissBoxState(
                    positionalThreshold = SwipeToDismissBoxDefaults.positionalThreshold
                )
                LaunchedEffect(pendingDeleteId) {
                    if (pendingDeleteId != item.expense.id &&
                        dismissState.currentValue != SwipeToDismissBoxValue.Settled
                    ) {
                        dismissState.reset()
                    }
                }

                // Reset the swipe if the dialog was dismissed (Cancel) or another row triggered it
                SwipeToDismissBox(
                    state = dismissState,
                    enableDismissFromStartToEnd = false,
                    onDismiss = { direction ->
                        if (direction == SwipeToDismissBoxValue.EndToStart) {
                            pendingDeleteId = item.expense.id
                        }
                    },
                    backgroundContent = {
                        val color by animateColorAsState(
                            NColors.Red,
                            label = "swipe_bg"
                        )
                        // Red background with rounded corners, matching the row shape
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(9.dp))
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
                ) {
                    // Expense row content
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

    // Edit expense dialog
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

    // Delete confirmation dialog
    pendingDeleteId?.let { id ->
        Dialog(
            onDismissRequest = { pendingDeleteId = null },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            NSurface(
                modifier = Modifier.padding(horizontal = 32.dp),
                backgroundColor = NColors.Background,
                borderWidth = 3.dp,
                shadowOffset = 6.dp,
                cornerRadius = 6.dp
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Delete Expense?",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = NColors.Black
                    )
                    Text(
                        text = "This action cannot be undone.",
                        fontSize = 14.sp,
                        color = NColors.Black.copy(alpha = 0.6f)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        NButton(
                            modifier = Modifier.weight(1f),
                            contentModifier = Modifier.fillMaxWidth(),
                            backgroundColor = NColors.White,
                            onClick = { pendingDeleteId = null }
                        ) {
                            Text(
                                text = "Cancel",
                                fontWeight = FontWeight.Bold,
                                color = NColors.Black
                            )
                        }
                        NButton(
                            modifier = Modifier.weight(1f),
                            contentModifier = Modifier.fillMaxWidth(),
                            backgroundColor = NColors.Red,
                            onClick = {
                                onDelete(id)
                                pendingDeleteId = null
                            }
                        ) {
                            Text(
                                text = "Delete",
                                fontWeight = FontWeight.Bold,
                                color = NColors.White
                            )
                        }
                    }
                }
            }
        }
    }
}
