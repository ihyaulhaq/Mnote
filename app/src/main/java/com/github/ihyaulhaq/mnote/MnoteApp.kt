package com.github.ihyaulhaq.mnote

import android.app.Application
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.github.ihyaulhaq.mnote.data.ExpenseRepository
import com.github.ihyaulhaq.mnote.data.local.MnoteDatabase
import com.github.ihyaulhaq.mnote.viewmodel.CategoryViewModel
import com.github.ihyaulhaq.mnote.viewmodel.ExpenseViewModel
import com.github.ihyaulhaq.mnote.viewmodel.StatsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

    val appFactory = viewModelFactory {
        initializer { ExpenseViewModel(repository) }
        initializer { StatsViewModel(repository) }
        initializer { CategoryViewModel(repository) }
    }

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.IO).launch {
            repository.seedDefaultCategories()
        }
    }
}
