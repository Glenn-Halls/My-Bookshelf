package com.example.mybookshelf.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mybookshelf.R
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
    isMyBook: Boolean,
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
                            isMyBook = isMyBook,
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
                                isMyBook = isMyBook,
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

@Composable
fun CustomSearchScreen(
    searchQuery: String,
    searchStringUpdate: (String) -> Unit,
    onSearchClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {
            Text(
                text = "Search Google's Library",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.fillMaxWidth(.5f)
            )

        }
        OutlinedTextField(
            label = {
                Text(
                    text = "Search Google's Library",
                    style = MaterialTheme.typography.labelMedium
                )
            },
            value = searchQuery,
            singleLine = true,
            onValueChange = searchStringUpdate,
            trailingIcon = {
                           Icon(
                               Icons.Default.Clear,
                               contentDescription = stringResource(R.string.clear_text),
                               modifier = modifier.clickable {
                                   searchStringUpdate("")
                               }
                           )
            },
            shape = RoundedCornerShape(50),
        )
        Spacer(modifier = modifier.size(16.dp))
        Button(onClick = onSearchClicked) {
            Text(
                text = "Search",
                style = MaterialTheme.typography.labelLarge
            )
        }
        Spacer(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        )
    }
}
