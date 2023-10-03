package com.example.mybookshelf.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.mybookshelf.R
import com.example.mybookshelf.data.convertToBook
import com.example.mybookshelf.data.getCoilUrl
import com.example.mybookshelf.model.MyBook
import com.example.mybookshelf.model.UserReview
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditMyBookScreen(
    myBook: MyBook,
    onEdit: (String) -> Unit,
    onRatingChange: (Int) -> Unit,
    onFavouriteChange: (Boolean) -> Unit,
    onCompletion: () -> Unit,
    onCancel: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    userReview: UserReview?,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.clickable {  }
            .fillMaxSize()
    ) {
        AsyncImage(
            model = myBook.convertToBook().getCoilUrl(),
            contentDescription = null,
            placeholder = painterResource(R.drawable.loading_img),
            error = null,
            contentScale = ContentScale.Fit,
            alignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        )
        EditMyBookPopup(
            myBook = myBook,
            onEdit = onEdit,
            onRatingChange = onRatingChange,
            onFavouriteChange = onFavouriteChange,
            onCompletion = onCompletion,
            onCancel = onCancel,
            onDismiss = onDismiss,
            userReview = userReview,
        )
    }
}


@Composable
fun EditMyBookPopup(
    myBook: MyBook,
    onEdit: (String) -> Unit,
    onRatingChange: (Int) -> Unit,
    onFavouriteChange: (Boolean) -> Unit,
    onCompletion: () -> Unit,
    onCancel: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    userReview: UserReview?,
) {
    val favouriteIconSize = 40.dp
    val userNotes = userReview?.userNotes ?: myBook.notes ?: ""
    val userFavourite = userReview?.isFavourite ?: myBook.isFavourite
    val scrollState = rememberScrollState()
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnClickOutside = true)
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = myBook.title,
                    style = MaterialTheme.typography.labelLarge,
                    )
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = { onFavouriteChange(!userFavourite) }) {
                        Icon(
                            Icons.Filled.DeleteForever,
                            contentDescription = stringResource(R.string.delete_from_library),
                            modifier = Modifier.size(favouriteIconSize)
                        )
                    }
                    IconButton(onClick = { onFavouriteChange(!userFavourite) }) {
                        if (userFavourite) {
                            Icon(
                                Icons.Filled.Favorite,
                                contentDescription = stringResource(R.string.favourite),
                                modifier = Modifier.size(favouriteIconSize),
                            )
                        } else {
                            Icon(
                                Icons.Filled.FavoriteBorder,
                                contentDescription = stringResource(R.string.not_favourite),
                                modifier = Modifier.size(favouriteIconSize),
                            )
                        }
                    }
                }
                RatingBar(
                    value = userReview?.userRating ?: myBook.rating?.toFloat() ?: 0f,
                    style = RatingBarStyle.Default,
                    onRatingChanged = {onRatingChange(it.toInt())},
                    onValueChange = {onRatingChange(it.toInt())},
                )
                OutlinedTextField(
                    label = {
                        Text(
                            text = "My Reading Notes",
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    value = userNotes,
                    onValueChange = onEdit,
                    modifier = Modifier
                        .fillMaxWidth(.9f)
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onCancel) { Text("dismiss") }
                    TextButton(onClick = onCompletion) { Text("complete") }
                }
            }
        }
    }
}

