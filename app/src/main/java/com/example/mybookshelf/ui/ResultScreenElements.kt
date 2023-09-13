package com.example.mybookshelf.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.mybookshelf.R
import com.example.mybookshelf.data.getShortDescription
import com.example.mybookshelf.model.Book

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookCard(
    book: Book,
    onCardClick: (Book) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        elevation = CardDefaults.cardElevation(),
        onClick = {onCardClick(book)},
        modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)),
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier,
        ) {
            Text(
                text = book.bookDetail?.title.toString(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displayMedium
            )
            Text(
                text = book.bookDetail?.date.toString(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = book.getShortDescription(15),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.labelMedium
            )
            Image(
                painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth(.45f),
                alignment = Alignment.Center
            )
        }
    }
}






@Composable
@Preview
fun BookCardPreview() {
    BookCard(book = Book("id", "link"), onCardClick = {})
}
