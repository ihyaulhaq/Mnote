package com.github.ihyaulhaq.mnote.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.ihyaulhaq.mnote.MnoteApp

@Composable
fun sharedCategoryViewModel(): CategoryViewModel {
    val context = LocalContext.current
    return viewModel(
        viewModelStoreOwner = context as ViewModelStoreOwner,
        factory = (context.applicationContext as MnoteApp).appFactory
    )
}
