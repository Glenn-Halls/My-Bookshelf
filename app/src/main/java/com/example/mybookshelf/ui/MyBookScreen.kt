package com.example.mybookshelf.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.mybookshelf.model.MyBook
import com.example.mybookshelf.ui.util.BookshelfContentLayout
import com.example.mybookshelf.ui.util.MyBookGrid

@Composable
fun MyBookScreen(
    layout: BookshelfContentLayout,
    myBooks: List<MyBook>,
    listScrollPosition: LazyGridState,
    onCardClick: (MyBook) -> Unit,
    scrollPosition: ScrollState,
    onEditClick: () -> Unit,
    onFavouriteClick: (MyBook) -> Unit,
    modifier: Modifier = Modifier,
    myBookSelected: MyBook?,
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        when (layout) {
            BookshelfContentLayout.LIST_ONLY -> {
                MyBookGrid(
                    myBooks = myBooks,
                    gridScrollPosition = listScrollPosition,
                    onCardClick = onCardClick
                )
            }
            BookshelfContentLayout.DETAILS_ONLY -> {
                MyBookDetailScreen(
                    scrollPosition = scrollPosition,
                    myBook = myBookSelected!!,
                    onEditClick = onEditClick,
                    isFavourite = myBookSelected.isFavourite,
                    onFavouriteClick = onFavouriteClick
                )
            }
            BookshelfContentLayout.LIST_AND_DETAILS -> {
                Row(modifier.fillMaxSize()) {
                    MyBookGrid(
                        myBooks = myBooks,
                        gridScrollPosition = listScrollPosition,
                        onCardClick = onCardClick,
                        modifier = modifier.fillMaxWidth(.5f)
                    )
                    MyBookDetailScreen(
                        scrollPosition = scrollPosition,
                        myBook = myBookSelected!!,
                        onEditClick = onEditClick,
                        isFavourite = myBookSelected.isFavourite,
                        onFavouriteClick = onFavouriteClick
                    )
                }
            }
        }
    }
}
