package com.example.mybookshelf.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.verticalScroll
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
import com.example.mybookshelf.data.getCoilUrl
import com.example.mybookshelf.model.Book

@Composable
fun BookDetailScreen(
    scrollPosition: ScrollState,
    onBackPressed: () -> Unit,
    book: Book,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier.fillMaxSize()
    ) {
        BackHandler(onBack = onBackPressed)
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
            AsyncImage(
                model = book.getCoilUrl(),
                contentDescription = stringResource(R.string.book_cover),
                placeholder = painterResource(R.drawable.loading_img),
                error = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth(.45f),
                alignment = Alignment.Center
            )
            Text(scrollPosition.value.toString())
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = book.bookDetail.description,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}
