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
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ExpenseViewModel(private val repository: ExpenseRepository) : ViewModel() {

    val expenses: StateFlow<List<ExpenseWithCategory>> = repository.allExpenses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categories: StateFlow<List<Category>> = repository.allCategories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _dateRange = MutableStateFlow<Pair<Long, Long>?>(null)

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val filteredExpenses: StateFlow<List<ExpenseWithCategory>> = _dateRange
        .flatMapLatest { range ->
            if (range != null) {
                repository.getExpensesByDateRange(range.first, range.second)
            } else {
                repository.allExpenses
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch { repository.seedDefaultCategories() }
    }

    fun addExpense(amount: Double, categoryId: Long, desc: String = "") {
        viewModelScope.launch {
            repository.addExpense(
                Expense(amount = amount, categoryId = categoryId, desc = desc)
            )
        }
    }

    fun deleteExpense(id: Long) {
        viewModelScope.launch { repository.deleteExpense(id) }
    }

    fun updateExpense(expense: Expense) {
        viewModelScope.launch { repository.updateExpense(expense) }
    }

    suspend fun getExpenseById(id: Long): Expense? = repository.getExpenseById(id)

    fun setDateRange(start: Long, end: Long) {
        _dateRange.value = Pair(start, end)
    }

    fun clearDateRange() {
        _dateRange.value = null
    }
}
