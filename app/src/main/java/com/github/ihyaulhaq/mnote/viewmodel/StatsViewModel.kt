package com.github.ihyaulhaq.mnote.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.ihyaulhaq.mnote.data.ExpenseRepository
import com.github.ihyaulhaq.mnote.data.local.ExpenseWithCategory
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

class StatsViewModel(private val repository: ExpenseRepository) : ViewModel() {

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val defaultContentRange: DateRangeState = run {
        val now = LocalDate.now()
        val start = now.withDayOfMonth(1)
            .atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val end = now.withDayOfMonth(now.lengthOfMonth())
            .atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        DateRangeState(start = start, end = end)
    }

    private val _dateRange = MutableStateFlow(DateRangeState())
    val dateRange = _dateRange.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val filteredExpenses: StateFlow<List<ExpenseWithCategory>> =
        _dateRange
            .flatMapLatest { range ->
                val start = range.start ?: defaultContentRange.start ?: 0L
                val end = range.end ?: defaultContentRange.end ?: System.currentTimeMillis()
                repository.getExpensesByDateRange(start, end)
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    fun setStartDate(start: Long) {
        _dateRange.value = _dateRange.value.copy(start = start)
    }

    fun setEndDate(end: Long) {
        _dateRange.value = _dateRange.value.copy(end = end)
    }

    fun clearDateRange() {
        _dateRange.value = DateRangeState()
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
