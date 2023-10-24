package com.example.mybookshelf.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mybookshelf.model.BookshelfViewModel
import com.example.mybookshelf.ui.theme.MyBookshelfTheme

@Composable
fun MyBookshelfApp(windowSize: WindowSizeClass) {
    val viewModel: BookshelfViewModel = viewModel(
        factory = BookshelfViewModel.Factory
    )
    val darkMode = viewModel.darkMode.collectAsState(null)
    MyBookshelfTheme(darkMode.value ?: isSystemInDarkTheme()) {
        MyBookshelfScreen(
            viewModel,
            windowSize,
        )
    }
}
