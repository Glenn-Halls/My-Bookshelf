package com.example.mybookshelf.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.mybookshelf.model.Book
import com.example.mybookshelf.model.SearchUiState

@Composable
fun BookSearchScreen(
    searchStatus: SearchUiState,
    onCardClick: (Book) -> Unit,
    onTryAgain: () -> Unit,
    modifier: Modifier = Modifier,
    bookSelected: Book?,
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        when (searchStatus) {
            is SearchUiState.Loading -> LoadingScreen()
            is SearchUiState.Error -> ErrorScreen(onTryAgain)
            is SearchUiState.Success ->
                if (bookSelected == null) {
                    BookGrid(
                        books = searchStatus.bookList,
                        onCardClick = onCardClick
                    )
                } else {
                    BookDetailScreen(onBackPressed = { /*TODO*/ }, book = bookSelected)
                }
        }
    }
}
