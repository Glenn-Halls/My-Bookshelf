package com.example.mybookshelf.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.mybookshelf.model.Book

@Composable
fun BookSearchScreen(
    books: List<Book>,
    onCardClick: (Book) -> Unit,
    modifier: Modifier = Modifier
) {
    BookGrid(books = books, onCardClick = onCardClick)
}
