package com.example.mybookshelf.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.mybookshelf.model.Bestseller
import com.example.mybookshelf.model.NytUiState

@Composable
fun NytBestsellerScreen(
    nytUiState: NytUiState,
    onCardClick: (Bestseller) -> Unit,
    onTryAgain: () -> Unit,
    hideTopBar: Boolean,
    modifier: Modifier = Modifier,
    listSelected: String?,
) {
    val showHeader: Boolean = (listSelected != null && !hideTopBar)
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        when (nytUiState) {
            is NytUiState.Loading -> LoadingScreen()
            is NytUiState.Error -> ErrorScreen(onTryAgain)
            is NytUiState.Success ->
                Scaffold(
                    topBar = {
                        if (showHeader) {
                            BookshelfHeader(heading = listSelected!!)
                        }
                    }
                ) { innerPadding ->
                    BestsellerGrid(
                        bestsellers = nytUiState.bestsellerList,
                        onCardClick = onCardClick,
                        modifier = modifier.padding(innerPadding)

                    )
                }
        }
    }
}
