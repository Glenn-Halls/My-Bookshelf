package com.example.mybookshelf.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mybookshelf.R
import com.example.mybookshelf.ui.util.BookshelfNavigationType
import com.example.mybookshelf.ui.util.NavigationElement
import com.example.mybookshelf.ui.util.NavigationTabs

@Composable
fun NavigationElement.getNameString() = stringResource(this.name)
@Composable
fun NavigationElement.getDescriptionString() = stringResource(this.description ?: R.string.blank)

@Composable
fun HomeScreen(
    navigationType: BookshelfNavigationType,
    navigationElements: List<NavigationElement>,
    onIconClick: (NavigationElement) -> Unit,
    showExitDialog: Boolean,
    onExitConfirm: () -> Unit,
    onExitCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tabLocation: String = when (navigationType) {
        BookshelfNavigationType.BOTTOM_BAR -> "below"
        BookshelfNavigationType.NAVIGATION_RAIL,
        BookshelfNavigationType.NAVIGATION_DRAWER
        -> "to the left"
    }
    val iconColor = MaterialTheme.colorScheme.secondary
    val iconSize = 70.dp
    val scrollState = rememberScrollState()
    val stdStyle = MaterialTheme.typography.labelMedium
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "Welcome to your bookshelf",
            style = MaterialTheme.typography.displayMedium,
            fontSize = 40.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_large))
        )
        Text(
            text = stringResource(R.string.bookshelf_introduction),
            style = stdStyle,
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_large))
        )
        navigationElements.forEach {
            Spacer(modifier = Modifier.size(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(iconSize)
                        .padding(dimensionResource(R.dimen.padding_medium))
                ) {
                    IconButton(
                        onClick = { onIconClick(it) },
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Image(
                            painter = painterResource(it.icon),
                            contentDescription = stringResource(it.name),
                            colorFilter = ColorFilter.tint(
                                iconColor,
                                blendMode = BlendMode.SrcAtop
                            ),
                            modifier = Modifier.size(iconSize.minus(30.dp))
                        )
                    }
                }
                Text(
                    text = "Click here or on the tab $tabLocation to navigate to the " +
                            "${it.getNameString()} screen. ${it.getDescriptionString()}",
                    style = stdStyle,
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
                )
            }
        }
        Spacer(modifier = Modifier.size(dimensionResource(R.dimen.padding_large)))
        if (showExitDialog) {
            AlertDialog(
                onDismissRequest = onExitCancel,
                title = { Text("Are you sure you want to exit?") },
                confirmButton = {
                    TextButton(onClick = onExitConfirm) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    TextButton(onClick = onExitCancel) {
                        Text("Cancel")
                    }
                },
            )
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun HomeScreenPreview() {
    HomeScreen(
        navigationType = BookshelfNavigationType.BOTTOM_BAR,
        navigationElements = NavigationTabs,
        {},
        false,
        {},
        {}
    )
}
