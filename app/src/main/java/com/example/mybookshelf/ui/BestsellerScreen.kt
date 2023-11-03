package com.example.mybookshelf.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.mybookshelf.model.Bestseller
import com.example.mybookshelf.model.MyBestseller
import com.example.mybookshelf.model.MyNytList
import com.example.mybookshelf.model.NytBestsellerList
import com.example.mybookshelf.model.NytUiState
import com.example.mybookshelf.ui.util.BestsellerGrid
import com.example.mybookshelf.ui.util.BookshelfHeader
import com.example.mybookshelf.ui.util.ErrorScreen
import com.example.mybookshelf.ui.util.LoadingScreen
import com.example.mybookshelf.ui.util.NytListList
import com.example.mybookshelf.ui.util.NytWaitScreen

@Composable
fun NytBestsellerScreen(
    nytUiState: NytUiState,
    nytApiOnCooldown: Boolean,
    nytApiCooldown: Int,
    nytListList: List<NytBestsellerList>?,
    myNytLists: List<MyNytList>,
    onNullNytListList: () -> Unit,
    filterLists: Boolean,
    onNytListClick: (NytBestsellerList) -> Unit,
    onNytListStarClick: (NytBestsellerList) -> Unit,
    myBestsellerList: List<MyBestseller>,
    onCardClick: (Bestseller) -> Unit,
    onStarClick: (Bestseller) -> Unit,
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
        } else if (nytListList == null) {
            if (nytUiState == NytUiState.Loading) {
                LoadingScreen()
            } else {
                ErrorScreen(onTryAgainButton = onNullNytListList)
            }
        } else if (listSelected == null) {
            NytListList(
                nytListList = nytListList,
                myNytLists = myNytLists,
                filterLists = filterLists,
                onListClick = onNytListClick,
                onStarClick = onNytListStarClick,
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
                        myBestsellerList = myBestsellerList,
                        onCardClick = onCardClick,
                        onStarClick = onStarClick,
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
