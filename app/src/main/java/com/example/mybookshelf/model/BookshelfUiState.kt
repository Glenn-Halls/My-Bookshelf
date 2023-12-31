package com.example.mybookshelf.model

import com.example.mybookshelf.ui.util.ScreenSelect

data class BookshelfUiState(
    val currentScreen: ScreenSelect? = null,
    val myBookSortOrder: SortOrder? = null,
    val bestsellerSortOrder: SortOrder? = null,
    val searchResult: BookSearchResult? = null,
    val bestseller: BestsellerSearch? = null,
    val nytLists: List<NytBestsellerList>? = null,
    val selectedBook: Book? = null,
    val selectedBestseller: Bestseller? = null,
    val selectedMyBook: MyBook? = null,
    val selectedNytList: NytBestsellerList? = null,
    val favouriteFilter: Boolean = false,
    val searchQuery: String? = null,
    var userReview: UserReview? = null,
    val scrollPosition: Int = 0,
    val gridScrollPosition: Int = 0,
    val editInProgress: Boolean = false,
    val nytApiOnCooldown: Boolean = false,
    val nytApiCooldown: Int = 0,
    val showExitDialog: Boolean = false
)
