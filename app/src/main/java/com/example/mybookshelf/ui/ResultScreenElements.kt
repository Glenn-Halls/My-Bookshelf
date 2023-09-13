package com.example.mybookshelf.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.unit.dp
import com.example.mybookshelf.R
import com.example.mybookshelf.data.getShortDescription
import com.example.mybookshelf.model.Book

@Composable
fun BookGrid(
    books: List<Book>,
    onCardClick: (Book) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 300.dp),
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(dimensionResource(R.dimen.padding_medium)),
    ) {
        items(
            items = books,
            key = { book -> book.id }
        ) {
            book -> BookCard(book = book, onCardClick = onCardClick)
        }
    }
}

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
        modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)),
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .height(450.dp)
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = book.bookDetail.title,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displayMedium
            )
            Text(
                text = book.bookDetail.date,
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
