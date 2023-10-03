package com.example.mybookshelf.model

import com.example.mybookshelf.ui.util.ScreenSelect

data class BookshelfUiState(
    val currentScreen: ScreenSelect? = null,
    val searchResult: BookSearchResult? = null,
    val bestseller: BestsellerSearch? = null,
    val nytLists: List<NytBestsellerList>? = null,
    val selectedBook: Book? = null,
    val selectedBestseller: Bestseller? = null,
    val selectedMyBook: MyBook? = null,
    val nytListSelected: String? = null,
    val searchQuery: String? = null,
    var userReview: UserReview? = null,
    val scrollPosition: Int = 0,
    val editInProgress: Boolean = false,
)
