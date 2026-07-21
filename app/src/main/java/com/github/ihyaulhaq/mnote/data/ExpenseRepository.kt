package com.github.ihyaulhaq.mnote.data

import com.github.ihyaulhaq.mnote.data.local.Category
import com.github.ihyaulhaq.mnote.data.local.CategoryDao
import com.github.ihyaulhaq.mnote.data.local.Expense
import com.github.ihyaulhaq.mnote.data.local.ExpenseDao
import com.github.ihyaulhaq.mnote.data.local.ExpenseWithCategory
import kotlinx.coroutines.flow.Flow

class ExpenseRepository(
    private val expenseDao: ExpenseDao,
    private val categoryDao: CategoryDao
) {
    val allExpenses: Flow<List<ExpenseWithCategory>> = expenseDao.observeAll()
    val allCategories: Flow<List<Category>> = categoryDao.observeAll()

    suspend fun addExpense(expense: Expense) = expenseDao.insert(expense)
    suspend fun deleteExpense(id: Long) = expenseDao.deleteById(id)
    suspend fun addCategory(category: Category) = categoryDao.insert(category)
    suspend fun deleteCategory(id: Long) = categoryDao.deleteById(id)

    suspend fun seedDefaultCategories() {
        val defaults = listOf("makan", "transport", "main").map { Category(name = it) }
        categoryDao.insertAll(defaults)
    }
}
