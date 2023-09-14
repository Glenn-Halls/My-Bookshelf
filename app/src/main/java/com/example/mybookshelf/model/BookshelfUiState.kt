package com.example.mybookshelf.model

import com.example.mybookshelf.ui.util.ScreenSelect

data class BookshelfUiState(
    val currentScreen: ScreenSelect? = null,
    val searchResult: SearchResult? = null,
    val bestSellers: BestsellerResult? = null,
)
