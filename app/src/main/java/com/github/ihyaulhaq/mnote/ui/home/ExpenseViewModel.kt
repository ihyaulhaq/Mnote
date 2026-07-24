package com.github.ihyaulhaq.mnote.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.ihyaulhaq.mnote.data.ExpenseRepository
import com.github.ihyaulhaq.mnote.data.local.Category
import com.github.ihyaulhaq.mnote.data.local.Expense
import com.github.ihyaulhaq.mnote.data.local.ExpenseWithCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId

data class DateRangeState(
    val start: Long? = null,
    val end: Long? = null
)

class ExpenseViewModel(private val repository: ExpenseRepository) : ViewModel() {

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    val categories: StateFlow<List<Category>> = repository.allCategories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val defaultRange: DateRangeState = run {
        val now = LocalDate.now()
        val start = now.minusMonths(1).withDayOfMonth(1)
            .atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val end = now.withDayOfMonth(now.lengthOfMonth())
            .atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        DateRangeState(start = start, end = end)
    }

    private val _dateRange = MutableStateFlow(defaultRange)
    val dateRange = _dateRange.asStateFlow()

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val filteredExpenses: StateFlow<List<ExpenseWithCategory>> =
        _dateRange
            .flatMapLatest { range ->
                if (range.start != null && range.end != null) {
                    repository.getExpensesByDateRange(
                        range.start,
                        range.end
                    )
                } else {
                    repository.allExpenses
                }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    init {
        viewModelScope.launch {
            try {
                repository.seedDefaultCategories()
            } catch (e: Exception) {
                _error.value = "Failed to seed categories: ${e.message}"
            }
        }
    }

    fun addExpense(amount: Double, categoryId: Long, desc: String = "") {
        viewModelScope.launch {
            try {
                repository.addExpense(
                    Expense(amount = amount, categoryId = categoryId, desc = desc)
                )
            } catch (e: Exception) {
                _error.value = "Failed to add expense: ${e.message}"
            }
        }
    }

    fun deleteExpense(id: Long) {
        viewModelScope.launch {
            try {
                repository.deleteExpense(id)
            } catch (e: Exception) {
                _error.value = "Failed to delete expense: ${e.message}"
            }
        }
    }

    fun updateExpense(expense: Expense) {
        viewModelScope.launch {
            try {
                repository.updateExpense(expense)
            } catch (e: Exception) {
                _error.value = "Failed to update expense: ${e.message}"
            }
        }
    }

    suspend fun getExpenseById(id: Long): Expense? = repository.getExpenseById(id)
    fun setStartDate(start: Long) {
        _dateRange.value = _dateRange.value.copy(start = start)
    }

    fun setEndDate(end: Long) {
        _dateRange.value = _dateRange.value.copy(end = end)
    }

    fun clearDateRange() {
        _dateRange.value = defaultRange
    }

    fun clearError() {
        _error.value = null
    }
}
