package com.example.mybookshelf.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.mybookshelf.model.Bestseller
import com.example.mybookshelf.model.NytBestsellerList
import com.example.mybookshelf.model.NytUiState

@Composable
fun NytBestsellerScreen(
    nytUiState: NytUiState,
    nytApiOnCooldown: Boolean,
    nytApiCooldown: Int,
    nytListList: List<NytBestsellerList>,
    onNytListClick: (NytBestsellerList) -> Unit,
    onCardClick: (Bestseller) -> Unit,
    onTryAgain: () -> Unit,
    hideTopBar: Boolean,
    modifier: Modifier = Modifier,
    listSelected: NytBestsellerList?,
) {
    val showHeader: Boolean = (listSelected != null && !hideTopBar)
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        if (nytApiOnCooldown) {
            NytWaitScreen(
                tryAgain = false,
                tryAgainAction = {},
                timeToWait = nytApiCooldown)
        }
        if (listSelected == null) {
            NytListList(
                nytListList = nytListList,
                onListClick = onNytListClick,
            )
        } else when (nytUiState) {
            is NytUiState.Loading -> LoadingScreen()
            is NytUiState.Error -> ErrorScreen(onTryAgain)
            is NytUiState.Success ->
                Scaffold(
                    topBar = {
                        if (showHeader) {
                            BookshelfHeader(heading = listSelected.listName)
                        }
                    }
                ) { innerPadding ->
                    BestsellerGrid(
                        bestsellers = nytUiState.bestsellerList,
                        onCardClick = onCardClick,
                        modifier = modifier.padding(innerPadding)

                    )
                }
            is NytUiState.Ready -> NytWaitScreen(
                tryAgain = true,
                tryAgainAction = onTryAgain,
                timeToWait = 0
            )
        }
    }
}
