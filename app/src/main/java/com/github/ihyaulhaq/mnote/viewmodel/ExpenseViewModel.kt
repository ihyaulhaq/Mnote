package com.github.ihyaulhaq.mnote.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.ihyaulhaq.mnote.data.ExpenseRepository
import com.github.ihyaulhaq.mnote.data.local.Expense
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExpenseViewModel(private val repository: ExpenseRepository) : ViewModel() {

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun addExpense(amount: Double, categoryId: Long, desc: String = "") =
        launchWithError("Failed to add expense") {
            repository.addExpense(
                Expense(amount = amount, categoryId = categoryId, desc = desc)
            )
        }

    fun deleteExpense(id: Long) =
        launchWithError("Failed to delete expense") {
            repository.deleteExpense(id)
        }

    fun updateExpense(expense: Expense) =
        launchWithError("Failed to update expense") {
            repository.updateExpense(expense)
        }

    suspend fun getExpenseById(id: Long): Expense? = repository.getExpenseById(id)

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
