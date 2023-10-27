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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.mybookshelf.R
import com.example.mybookshelf.model.MyBook
import com.example.mybookshelf.model.extension.convertToBook
import com.example.mybookshelf.model.extension.getCoilUrl
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle

@Composable
fun MyBookDetailScreen(
    scrollPosition: ScrollState,
    myBook: MyBook,
    onEditClick: () -> Unit,
    isFavourite: Boolean,
    onFavouriteClick: (MyBook) -> Unit,
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
                text = myBook.title,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.size(16.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                IconButton(onClick = onEditClick) {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = stringResource(R.string.favourite),
                        modifier = Modifier.size(iconSize),
                    )
                }
                AsyncImage(
                    model = myBook.convertToBook().getCoilUrl(),
                    contentDescription = stringResource(R.string.book_cover),
                    placeholder = painterResource(R.drawable.loading_img),
                    error = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxWidth(.45f),
                    alignment = Alignment.Center,
                )
                IconButton(onClick = { onFavouriteClick(myBook) }) {
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
            Spacer(modifier = Modifier.size(16.dp))
            RatingBar(
                value = myBook.rating?.toFloat() ?: 0f,
                style = RatingBarStyle.Fill(
                    activeColor = MaterialTheme.colorScheme.secondary,
                    inActiveColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                onValueChange = {},
                onRatingChanged = {},
            )
            Spacer(modifier = Modifier.size(16.dp))
            Text(text = myBook.notes ?: "")
        }
    }
}
