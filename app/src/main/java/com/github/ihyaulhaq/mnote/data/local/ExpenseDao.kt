package com.github.ihyaulhaq.mnote.data.local

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

data class ExpenseWithCategory(
    @Embedded val expense: Expense,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: Category
)

data class CategoryWithCount(
    @Embedded val category: Category,
    @ColumnInfo(name = "expenseCount") val expenseCount: Int
)

@Dao
interface ExpenseDao {
    @Transaction
    @Query("SELECT * FROM expenses ORDER BY timestamp DESC")
    fun observeAll(): Flow<List<ExpenseWithCategory>>

    @Insert
    suspend fun insert(expense: Expense)

    @Query("DELETE FROM expenses WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Update
    suspend fun update(expense: Expense)

    @Query("SELECT * FROM expenses WHERE id = :id")
    suspend fun getById(id: Long): Expense?

    @Transaction
    @Query("SELECT * FROM expenses WHERE timestamp BETWEEN :start AND :end ORDER BY timestamp DESC")
    fun observeByDateRange(start: Long, end: Long): Flow<List<ExpenseWithCategory>>

    @Query("""
        SELECT c.*, (SELECT COUNT(*) FROM expenses e WHERE e.categoryId = c.id) AS expenseCount
        FROM categories c ORDER BY c.name ASC
    """)
    fun observeCategoriesWithCount(): Flow<List<CategoryWithCount>>
}
