package com.example.mybookshelf.ui.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.compose.material.icons.filled.Update
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.mybookshelf.model.SortOrder

data class ActionButton(
    val showButton: Boolean,
    val icon: ImageVector? = null,
    val action: () -> Unit = {},
    val mirrorIcon: Boolean? = false,
    val contentDescription: String? = null,
)

val SortOrderActionButtonList: List <Pair<SortOrder, ActionButton>> = listOf(
    SortOrder.ALPHABETICAL to ActionButton(
        showButton = true,
        icon = Icons.Default.SortByAlpha,
        action = {},
    ),
    SortOrder.ALPHABETICAL_REVERSE to ActionButton(
        showButton = true,
        icon = Icons.Default.SortByAlpha,
        action = {},
        mirrorIcon = true
    ),
    SortOrder.LAST_ADDED to ActionButton(
        showButton = true,
        icon = Icons.Default.Update,
        action = {},
    ),
    SortOrder.LAST_ADDED_REVERSE to ActionButton(
        showButton = true,
        icon = Icons.Default.Update,
        action = {},
        mirrorIcon = true
    ),
    SortOrder.LAST_UPDATED to ActionButton(
        showButton = true,
        icon = Icons.Default.Edit,
        action = {},
    ),
    SortOrder.LAST_UPDATED_REVERSE to ActionButton(
        showButton = true,
        icon = Icons.Default.Edit,
        action = {},
        mirrorIcon = true
    )
)
