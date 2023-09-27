package com.example.mybookshelf.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.mybookshelf.R
import com.example.mybookshelf.data.getCoilUrl
import com.example.mybookshelf.model.Book

@Composable
fun BookDetailScreen(
    scrollPosition: ScrollState,
    book: Book,
    isMyBook: Boolean,
    isFavourite: Boolean,
    onFavouriteClick: (Book) -> Unit,
    onBookmarkClick: (Book) -> Unit,
    modifier: Modifier = Modifier
) {
    val iconSize = 40.dp
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(scrollPosition)
        ) {
            Text(
                text = book.bookDetail.title,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.size(16.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                IconButton(onClick = { onBookmarkClick(book) }) {
                    if (isMyBook) {
                        Icon(
                            Icons.Filled.Bookmark,
                            contentDescription = stringResource(R.string.on_bookshelf),
                            tint = Color.Black,
                            modifier = Modifier.size(iconSize),
                        )
                    } else {
                        Icon(
                            Icons.Filled.BookmarkBorder,
                            contentDescription = stringResource(R.string.not_on_bookshelf),
                            tint = Color.Black,
                            modifier = Modifier.size(iconSize),
                        )
                    }
                }
                AsyncImage(
                    model = book.getCoilUrl(),
                    contentDescription = stringResource(R.string.book_cover),
                    placeholder = painterResource(R.drawable.loading_img),
                    error = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxWidth(.45f),
                    alignment = Alignment.Center
                )
                IconButton(onClick = { onFavouriteClick(book) }) {
                    if (isFavourite) {
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = stringResource(R.string.favourite),
                            modifier = Modifier.size(iconSize),
                        )
                    } else {
                        Icon(
                            Icons.Filled.FavoriteBorder,
                            contentDescription = stringResource(R.string.not_favourite),
                            modifier = Modifier.size(iconSize),
                        )
                    }
                }
            }
            Text(scrollPosition.value.toString())
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = book.bookDetail.description,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}
