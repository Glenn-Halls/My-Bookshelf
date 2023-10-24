package com.example.mybookshelf.ui

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mybookshelf.model.BookshelfViewModel
import com.example.mybookshelf.ui.theme.MyBookshelfTheme

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun MyBookshelfApp(windowSize: WindowSizeClass) {
    val viewModel: BookshelfViewModel = viewModel(
        factory = BookshelfViewModel.Factory
    )
    val darkTheme = false
    MyBookshelfTheme(darkTheme) {
        MyBookshelfScreen(
            viewModel,
            windowSize,
        )
    }
}
