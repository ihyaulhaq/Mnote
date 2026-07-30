package com.github.ihyaulhaq.mnote.ui.stats

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.ihyaulhaq.mnote.ui.components.NSurface
import com.github.ihyaulhaq.mnote.ui.theme.NColors
import com.github.ihyaulhaq.mnote.viewmodel.CategoryViewModel/** Settings menu orchestrator with two-level navigation (menu list → sub-sections). */
@Composable
fun SettingsContent(
    categoryViewModel: CategoryViewModel
) {
    var activeSection by remember { mutableStateOf<String?>(null) }

    if (activeSection == null) {
        SettingsMenu(
            onSelectCategories = { activeSection = "categories" }
        )
    } else {
        CategoriesManagement(
            categoryViewModel = categoryViewModel,
            onBack = { activeSection = null }
        )
    }
}

@Composable
private fun SettingsMenu(
    onSelectCategories: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            NSurface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onSelectCategories),
                backgroundColor = NColors.White,
                borderWidth = 2.dp,
                shadowOffset = 3.dp,
                cornerRadius = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Manage Categories",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = NColors.Black
                    )
                    Text(
                        text = ">",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = NColors.Black.copy(alpha = 0.4f)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PrevSettingsMenu () {
    SettingsMenu (
        onSelectCategories = {}
    )
}
