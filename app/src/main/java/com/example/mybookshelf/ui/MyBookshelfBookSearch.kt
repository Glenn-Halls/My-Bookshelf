package com.example.mybookshelf.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.mybookshelf.model.Book
import com.example.mybookshelf.model.SearchUiState
import com.example.mybookshelf.ui.util.BookshelfContentLayout

@Composable
fun BookSearchScreen(
    searchStatus: SearchUiState,
    layout: BookshelfContentLayout,
    scrollPosition: ScrollState,
    listScrollPosition: LazyGridState,
    onCardClick: (Book) -> Unit,
    isFavourite: Boolean,
    onFavouriteClick: (Book) -> Unit,
    isBookmarked: Boolean,
    onBookmarkClick: (Book) -> Unit,
    onTryAgain: () -> Unit,
    modifier: Modifier = Modifier,
    bookSelected: Book?,
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        when (searchStatus) {
            is SearchUiState.Loading -> { LoadingScreen() }
            is SearchUiState.Error -> { ErrorScreen(onTryAgain) }
            is SearchUiState.Success -> {
                when (layout) {
                    BookshelfContentLayout.LIST_ONLY -> {
                        BookGrid(
                            books = searchStatus.bookList,
                            onCardClick = onCardClick,
                            listScrollPosition,
                        )
                    }
                    BookshelfContentLayout.DETAILS_ONLY -> {
                        BookDetailScreen(
                            book = bookSelected!!,
                            isFavourite = isFavourite,
                            onFavouriteClick = onFavouriteClick,
                            onBookmarkClick = onBookmarkClick,
                            scrollPosition = scrollPosition)
                    }
                    BookshelfContentLayout.LIST_AND_DETAILS -> {
                        Row(modifier.fillMaxSize()) {
                            BookGrid(
                                books = searchStatus.bookList,
                                onCardClick = onCardClick,
                                listScrollPosition,
                                modifier = modifier.fillMaxWidth(.5f)
                            )
                            BookDetailScreen(
                                book = bookSelected!!,
                                isFavourite = isFavourite,
                                onFavouriteClick = onFavouriteClick,
                                onBookmarkClick = onBookmarkClick,
                                scrollPosition = scrollPosition,
                                )
                        }
                    }
                }
            }
        }
    }
}
