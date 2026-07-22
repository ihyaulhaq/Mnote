package com.github.ihyaulhaq.mnote.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        Expense::class,
        Category::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MnoteDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        @Volatile
        private var INSTANCE: MnoteDatabase? = null

        fun getInstance(context: Context): MnoteDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    MnoteDatabase::class.java,
                    "mnote.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
