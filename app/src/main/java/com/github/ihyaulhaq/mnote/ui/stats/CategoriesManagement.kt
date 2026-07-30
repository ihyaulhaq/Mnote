package com.github.ihyaulhaq.mnote.ui.stats

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.github.ihyaulhaq.mnote.data.local.CategoryWithCount
import com.github.ihyaulhaq.mnote.ui.components.NButton
import com.github.ihyaulhaq.mnote.ui.components.NSurface
import com.github.ihyaulhaq.mnote.ui.components.NTextField
import com.github.ihyaulhaq.mnote.ui.theme.NColors
import com.github.ihyaulhaq.mnote.viewmodel.CategoryViewModel

/** Dialog for editing a category name. */
@Composable
private fun EditCategoryDialog(
    category: CategoryWithCount?,
    onDismiss: () -> Unit,
    onSave: (Long, String) -> Unit
) {
    val cwc = category ?: return
    var name by remember(cwc.category.id) { mutableStateOf(cwc.category.name) }
    Dialog(
        onDismissRequest = onDismiss,
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
                    text = "Edit Category",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = NColors.Black
                )
                NTextField(
                    value = name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    onValueChange = { name = it },
                    placeholder = "Category name"
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    NButton(
                        modifier = Modifier.weight(1f),
                        contentModifier = Modifier.fillMaxWidth(),
                        backgroundColor = NColors.White,
                        onClick = onDismiss
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
                        backgroundColor = NColors.Green,
                        onClick = {
                            if (name.isNotBlank()) {
                                onSave(cwc.category.id, name)
                            }
                        }
                    ) {
                        Text(
                            text = "Save",
                            fontWeight = FontWeight.Bold,
                            color = NColors.White
                        )
                    }
                }
            }
        }
    }
}

/** Dialog for confirming category deletion. */
@Composable
private fun DeleteCategoryDialog(
    category: CategoryWithCount?,
    onDismiss: () -> Unit,
    onConfirm: (Long) -> Unit
) {
    val cwc = category ?: return
    Dialog(
        onDismissRequest = onDismiss,
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
                    text = "Delete Category?",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = NColors.Black
                )
                Text(
                    text = "Delete \"${cwc.category.name}\"? This cannot be undone.",
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
                        onClick = onDismiss
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
                        onClick = { onConfirm(cwc.category.id) }
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

/** Dialog for adding a new category. */
@Composable
private fun AddCategoryDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onAdd: (String) -> Unit
) {
    if (!show) return
    var name by remember { mutableStateOf("") }
    Dialog(
        onDismissRequest = {
            name = ""
            onDismiss()
        },
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
                    text = "Add Category",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = NColors.Black
                )
                NTextField(
                    value = name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    onValueChange = { name = it },
                    placeholder = "Category name"
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    NButton(
                        modifier = Modifier.weight(1f),
                        contentModifier = Modifier.fillMaxWidth(),
                        backgroundColor = NColors.White,
                        onClick = {
                            name = ""
                            onDismiss()
                        }
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
                        backgroundColor = NColors.Green,
                        onClick = {
                            if (name.isNotBlank()) {
                                onAdd(name.trim())
                                name = ""
                            }
                        }
                    ) {
                        Text(
                            text = "Add",
                            fontWeight = FontWeight.Bold,
                            color = NColors.White
                        )
                    }
                }
            }
        }
    }
}

/** Category list with add/edit/delete dialogs and expense count per category. */
@Composable
fun CategoriesManagement(
    categoryViewModel: CategoryViewModel,
    onBack: () -> Unit
) {
    val categoriesWithCount by categoryViewModel.categoriesWithCount.collectAsState()
    var editingCategory by remember { mutableStateOf<CategoryWithCount?>(null) }
    var deletingCategory by remember { mutableStateOf<CategoryWithCount?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            NButton(
                backgroundColor = NColors.White,
                contentPadding = PaddingValues(8.dp),
                contentSize = 32.dp,
                onClick = onBack
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = NColors.Black,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Manage Categories",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = NColors.Black
            )
        }

        NSurface(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = NColors.Yellow,
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
                    text = "Category",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = NColors.White,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Expenses",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = NColors.White,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.width(80.dp))
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(categoriesWithCount, key = { it.category.id }) { item ->
                NSurface(
                    modifier = Modifier.fillMaxWidth(),
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
                            text = item.category.name,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = NColors.Black,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "${item.expenseCount}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = NColors.Black,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            NButton(
                                backgroundColor = NColors.Blue,
                                contentPadding = PaddingValues(6.dp),
                                contentSize = 28.dp,
                                onClick = { editingCategory = item }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit",
                                    tint = NColors.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            NButton(
                                backgroundColor = NColors.Red,
                                contentPadding = PaddingValues(6.dp),
                                contentSize = 28.dp,
                                onClick = { deletingCategory = item }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = NColors.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
            item {
                NButton(
                    modifier = Modifier.fillMaxSize(),
                    contentModifier = Modifier.fillMaxSize(),
                    backgroundColor = NColors.Green,
                    onClick = { showAddDialog = true }
                ) {
                    Text(
                        text = "Add Category",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = NColors.White,
                    )
                }
            }
        }
    }

    EditCategoryDialog(
        category = editingCategory,
        onDismiss = { editingCategory = null },
        onSave = { id, name ->
            categoryViewModel.updateCategory(id, name)
            editingCategory = null
        }
    )
    DeleteCategoryDialog(
        category = deletingCategory,
        onDismiss = { deletingCategory = null },
        onConfirm = { id ->
            categoryViewModel.deleteCategory(id)
            deletingCategory = null
        }
    )
    AddCategoryDialog(
        show = showAddDialog,
        onDismiss = { showAddDialog = false },
        onAdd = { name ->
            categoryViewModel.addCategory(name)
            showAddDialog = false
        }
    )
}
