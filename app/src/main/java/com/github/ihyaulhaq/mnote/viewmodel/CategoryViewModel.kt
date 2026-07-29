package com.github.ihyaulhaq.mnote.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.ihyaulhaq.mnote.data.ExpenseRepository
import com.github.ihyaulhaq.mnote.data.local.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CategoryViewModel(private val repository: ExpenseRepository) : ViewModel() {

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    val categories: StateFlow<List<Category>> = repository.allCategories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addCategory(name: String) =
        launchWithError("Failed to add category") {
            repository.addCategory(Category(name = name))
        }

    fun deleteCategory(id: Long) =
        launchWithError("Failed to delete category") {
            repository.deleteCategory(id)
        }

    fun clearError() {
        _error.value = null
    }

    private fun launchWithError(
        message: String,
        block: suspend () -> Unit
    ) {
        viewModelScope.launch {
            try {
                block()
            } catch (e: Exception) {
                _error.value = "$message: ${e.message}"
            }
        }
    }
}
