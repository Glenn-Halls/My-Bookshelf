package com.example.mybookshelf.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
    onCardClick: (Book) -> Unit,
    onBackClick: () -> Unit,
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
                            onCardClick = onCardClick
                        )
                    }
                    BookshelfContentLayout.DETAILS_ONLY -> {
                        BookDetailScreen(
                            onBackPressed = onBackClick,
                            book = bookSelected!!,
                            scrollPosition = scrollPosition)
                    }
                    BookshelfContentLayout.LIST_AND_DETAILS -> {
                        Row(modifier.fillMaxSize()) {
                            BookGrid(
                                books = searchStatus.bookList,
                                onCardClick = onCardClick,
                                modifier = modifier.fillMaxWidth(.5f)
                            )
                            BookDetailScreen(
                                onBackPressed = onBackClick,
                                book = bookSelected!!,
                                scrollPosition = scrollPosition,
                                )
                        }
                    }
                }
            }
        }
    }
}
