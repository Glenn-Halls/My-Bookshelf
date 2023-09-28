package com.example.mybookshelf.ui.util

import androidx.compose.ui.graphics.vector.ImageVector

data class ActionButton(
    val showButton: Boolean,
    val icon: ImageVector? = null,
    val action: () -> Unit = {},
    val contentDescription: String? = null,
)
