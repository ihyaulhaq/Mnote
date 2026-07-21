package com.github.ihyaulhaq.mnote

import android.app.Application
import com.github.ihyaulhaq.mnote.data.ExpenseRepository
import com.github.ihyaulhaq.mnote.data.local.MnoteDatabase

class MnoteApp : Application() {
    val database by lazy {
        MnoteDatabase.getInstance(this)
    }
    val repository by lazy {
        ExpenseRepository(
            database.expenseDao(),
            database.categoryDao()
        )
    }
}
