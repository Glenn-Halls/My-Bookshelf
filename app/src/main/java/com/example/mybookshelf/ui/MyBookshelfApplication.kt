package com.example.mybookshelf.ui

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import com.example.mybookshelf.ui.theme.MyBookshelfTheme

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun MyBookshelfApplication(windowSize: WindowSizeClass) {
    MyBookshelfTheme {
        MyBookshelfScreen(windowSize)
    }
}
