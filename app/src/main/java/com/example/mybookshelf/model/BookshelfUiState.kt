package com.example.mybookshelf.model

import com.example.mybookshelf.ui.util.ScreenSelect

data class BookshelfUiState(
    val currentScreen: ScreenSelect? = null,
    val searchResult: BookSearchResult? = null,
    val bestseller: BestsellerSearch? = null,
    val nytLists: List<NytBestsellerList>? = null,
    val selectedBook: Book? = null,
    val selectedBestseller: Bestseller? = null,
    val nytListSelected: String? = null,
)
