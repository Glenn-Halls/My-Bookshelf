package com.example.mybookshelf.ui

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.mybookshelf.R
import com.example.mybookshelf.data.convertToBook
import com.example.mybookshelf.data.getCoilUrl
import com.example.mybookshelf.data.getShortDescription
import com.example.mybookshelf.model.Bestseller
import com.example.mybookshelf.model.Book
import com.example.mybookshelf.model.FakeBestseller
import com.example.mybookshelf.model.MyBook
import com.example.mybookshelf.model.NytBestsellerList
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle
import kotlinx.coroutines.delay

@Composable
fun BookGrid(
    books: List<Book>,
    onCardClick: (Book) -> Unit,
    listScrollPosition: LazyGridState,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 300.dp),
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(dimensionResource(R.dimen.padding_medium)),
        state = listScrollPosition,
    ) {
        items(
            items = books,
            key = { book -> book.id }
        ) {
            book -> BookCard(book = book, onCardClick = onCardClick)
        }
    }
}

@Composable
fun BestsellerGrid(
    bestsellers: List<Bestseller>,
    onCardClick: (Bestseller) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 300.dp),
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(dimensionResource(R.dimen.padding_medium))
        ) {
        items(
            items = bestsellers,
            key = { bestseller -> bestseller.isbn13 }
        ) {
            bestseller -> BestsellerCard(bestseller = bestseller, onCardClick = onCardClick)
        }
    }
}

@Composable
fun MyBookGrid(
    myBooks: List<MyBook>,
    onCardClick: (MyBook) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 300.dp),
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(dimensionResource(R.dimen.padding_medium)),
    ) {
        items(
            items = myBooks,
            key = { myBook -> myBook.id }
        ) {
            myBook -> MyBookCard(myBook = myBook, onCardClick = onCardClick)
        }
    }
}

@Composable
fun NytListList(
    nytListList: List<NytBestsellerList>,
    onListClick: (NytBestsellerList) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        contentPadding = PaddingValues(0.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp),
        horizontalArrangement = Arrangement.spacedBy(0.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        items(
            items = nytListList,
            key = { nytList -> nytList.toString() }
        ) {
            nytList -> BestSellerListItem(
            nytList = nytList,
            onListClick = onListClick,
            modifier = Modifier
                .padding(0.dp)
                .border(Dp.Hairline, Color.Black)
            )
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
        modifier = modifier.padding(dimensionResource(R.dimen.padding_medium)),
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .height(470.dp)
                .padding(dimensionResource(R.dimen.padding_small))
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = book.getCoilUrl(),
                contentDescription = stringResource(R.string.book_cover),
                placeholder = painterResource(R.drawable.loading_img),
                error = painterResource(R.drawable.ic_broken_image),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth(.45f),
                alignment = Alignment.Center
            )
            Text(
                text = book.bookDetail.title,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displayMedium
            )
            Text(
                text = book.bookDetail.authors[0],
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = book.bookDetail.date,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = book.getShortDescription(),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BestsellerCard(
    bestseller: Bestseller,
    onCardClick: (Bestseller) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        elevation = CardDefaults.cardElevation(),
        onClick = {onCardClick(bestseller)},
        modifier = modifier.padding(dimensionResource(R.dimen.padding_medium)),
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .height(420.dp)
                .padding(dimensionResource(R.dimen.padding_medium))
                .fillMaxWidth()
        ) {
            Text(
                text = bestseller.title,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displayMedium
            )
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.fillMaxWidth()
            ){
                AsyncImage(
                    model = bestseller.coverImage,
                    contentDescription = stringResource(R.string.book_cover),
                    placeholder = painterResource(R.drawable.loading_img),
                    error = painterResource(R.drawable.ic_broken_image),
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxWidth(.45f),
                    alignment = Alignment.Center
                )
                Box(
                    modifier = modifier
                        .fillMaxWidth(.7f)
                        .aspectRatio(.7f)
                ) {
                    Column(
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "Rank: ${bestseller.rank}",
                            style = MaterialTheme.typography.labelLarge,
                            )
                        Text(
                            text = "Previous: ${bestseller.previousRank}",
                            style = MaterialTheme.typography.labelMedium,
                            )
                        Text(
                            text = "Weeks: ${bestseller.weeksOnList}",
                            style = MaterialTheme.typography.labelMedium,
                            )
                        Text(
                            text = "Author:\n${bestseller.author}",
                            style = MaterialTheme.typography.labelMedium,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
            Text(
                text = bestseller.description,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBookCard(
    myBook: MyBook,
    onCardClick: (MyBook) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isFavourite = myBook.isFavourite
    val rating = myBook.rating
    val notes = myBook.notes
    val book = myBook.convertToBook()
    Card(
        elevation = CardDefaults.cardElevation(),
        onClick = {onCardClick(myBook)},
        modifier = modifier.padding(dimensionResource(R.dimen.padding_medium)),
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .height(470.dp)
                .padding(dimensionResource(R.dimen.padding_medium))
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = book.getCoilUrl(),
                contentDescription = stringResource(R.string.book_cover),
                placeholder = painterResource(R.drawable.loading_img),
                error = painterResource(R.drawable.ic_broken_image),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth(.45f),
                alignment = Alignment.Center
            )
            RatingBar(
                value = rating?.toFloat() ?: 0f,
                style = RatingBarStyle.Fill(),
                onValueChange = { /* TODO */ },
                onRatingChanged = { Log.d("Rating", "changed to $rating") }
            )
            Text(text = notes ?: "")
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
                text = book.getShortDescription(),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BestSellerListItem(
    nytList: NytBestsellerList,
    onListClick: (NytBestsellerList) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = {onListClick(nytList)},
        shape = RoundedCornerShape(0),
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .padding(dimensionResource(R.dimen.padding_medium))
        ) {
            Text(
                text = nytList.listName,
                style = MaterialTheme.typography.labelMedium
            )
            Text("STAR")
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(contentAlignment = Alignment.Center, modifier = modifier.fillMaxSize()) {
        LoadingAnimation3()
    }
}

@Composable
fun NytWaitScreen(
    tryAgain: Boolean,
    tryAgainAction: () -> Unit,
    timeToWait: Int,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.WifiOff,
                tint = MaterialTheme.colorScheme.secondary,
                contentDescription = null,
                modifier = Modifier.size(90.dp)
            )
            Text(
                text = "Too Many Requests",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "The NYT server limits repeated requests."
            )
            Text(
                text = "Please wait a moment and try again."
            )
            Spacer(modifier = Modifier.size(16.dp))
            Button(onClick = tryAgainAction) {
                Text(
                    text = if (tryAgain) "Try Again" else "Wait $timeToWait seconds",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Composable
fun LoadingAnimation3(
    circleColor: Color = MaterialTheme.colorScheme.primary,
    circleSize: Dp = 36.dp,
    animationDelay: Int = 400,
    initialAlpha: Float = 0.3f
) {
    // 3 circles
    val circles = listOf(
        remember {
            Animatable(initialValue = initialAlpha)
        },
        remember {
            Animatable(initialValue = initialAlpha)
        },
        remember {
            Animatable(initialValue = initialAlpha)
        }
    )

    circles.forEachIndexed { index, animatable ->

        LaunchedEffect(Unit) {

            // Use coroutine delay to sync animations
            delay(timeMillis = (animationDelay / circles.size).toLong() * index)

            animatable.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = animationDelay
                    ),
                    repeatMode = RepeatMode.Reverse
                )
            )
        }
    }
    // container for circles
    Row(
        modifier = Modifier
        //.border(width = 2.dp, color = Material Theme Primary)
    ) {

        // adding each circle
        circles.forEachIndexed { index, animatable ->

            // gap between the circles
            if (index != 0) {
                Spacer(modifier = Modifier.width(width = 6.dp))
            }

            Box(
                modifier = Modifier
                    .size(size = circleSize)
                    .clip(shape = CircleShape)
                    .background(
                        color = circleColor
                            .copy(alpha = animatable.value)
                    )
            ) {
            }
        }
    }
}

@Composable
fun ErrorScreen(
    onTryAgainButton: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.WifiOff,
                tint = MaterialTheme.colorScheme.secondary,
                contentDescription = null,
                modifier = Modifier.size(90.dp)
            )
            Text(
                text = "ERROR:\nSomething went wrong...",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.size(16.dp))
            Button(onClick = onTryAgainButton) {
                Text(
                    text = "Try Again",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookshelfHeader(
    heading: String,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = heading,
                style = MaterialTheme.typography.labelLarge
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = Modifier.height(30.dp)
    )
}






@Composable
@Preview
fun BookCardPreview() {
    BookCard(book = Book("id", "link"), onCardClick = {})
}

@Composable
@Preview
fun BestsellerCardPreview() {
    BestsellerCard(bestseller = FakeBestseller, onCardClick = {})
}


