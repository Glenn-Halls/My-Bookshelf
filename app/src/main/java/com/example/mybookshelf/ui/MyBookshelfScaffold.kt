package com.example.mybookshelf.ui


import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.mybookshelf.R
import com.example.mybookshelf.model.BookshelfViewModel
import com.example.mybookshelf.ui.util.BookshelfNavigationType
import com.example.mybookshelf.ui.util.ScreenSelect
import kotlinx.coroutines.launch

@Composable
fun MyBookshelfScreen(
    viewModel: BookshelfViewModel,
    windowSize: WindowSizeClass,
    modifier: Modifier = Modifier
) {
    // State Flow accessor to UI State
    val uiState by viewModel.uiState.collectAsState()
    // Define navigation type based on WindowSizeClass dimensions
    val navigationType = viewModel.getNavigationSetup(windowSize)
    // Get window height in order to NOT show top bar on compact screens
    val windowHeight = windowSize.heightSizeClass
    // Create a coroutine scope event listener
    val coroutineScope = rememberCoroutineScope()
    // Get screen content layout
    val bookScreenLayout = viewModel.getScreenLayout(windowSize, uiState.selectedBook)
    // Define & remember scroll state in order to use Launched Effect to update via coroutine
    val scrollPosition = rememberScrollState()
    // Scroll to the position defined in ViewModel on Re/Composition OR if selected book changes.
    // NB: viewModel.selectBook(book) will set scroll position to 0px.
    LaunchedEffect(uiState.selectedBook) {
        scrollPosition.scrollTo(uiState.scrollPosition)
        Log.d("Launched Effect", "scroll to ${scrollPosition.value}")
    }
    // On Composable destruction, save scroll position to ViewModel.
    DisposableEffect(true) {
        onDispose {
            viewModel.setScrollPosition(scrollPosition.value)
            Log.d("Disposable Effect", "save scroll position ${scrollPosition.value}")
        }
    }
    BackHandler(true, viewModel::navigateBack)
    Scaffold(
        // Do not show top bar on compact screens
        topBar = {
            if (windowHeight != WindowHeightSizeClass.Compact) {
                MyBookshelfTopBar(onUpButtonClick = viewModel::navigateBack)
            }
        },
        bottomBar = {
            if (navigationType == BookshelfNavigationType.BOTTOM_BAR) {
                BookshelfBottomNavBar(
                    onTabPressed = { viewModel.navigateToScreen(it) }
                )
            }
        }
    ){ innerPadding ->
        Row(modifier = Modifier.padding(innerPadding)) {
            // Display navigation rail for non-compact-width screens
            if (navigationType == BookshelfNavigationType.NAVIGATION_RAIL) {
                BookshelfNavigationRail(
                    onTabPressed = { viewModel.navigateToScreen(it) },
                    onNavigateBack = viewModel::navigateBack,
                    showBackButton = windowHeight == WindowHeightSizeClass.Compact
                )
            }
            // Determine screen to display based on current screen selection
            when (uiState.currentScreen) {
                ScreenSelect.NONE -> Text("hello")
                ScreenSelect.BEST_SELLERS -> NytBestsellerScreen(
                    nytUiState = viewModel.nytUiState,
                    onCardClick = { viewModel.selectBestseller(it) },
                    onTryAgain = { viewModel.getBestsellers(300) },
                    listSelected = uiState.nytListSelected,
                    hideTopBar = (windowHeight == WindowHeightSizeClass.Compact)
                )

                ScreenSelect.WATCH_LIST -> ErrorScreen(onTryAgainButton = {
                    coroutineScope.launch {
                        viewModel.testSaveItem()
                    }
                })
                ScreenSelect.BROWSE ->
                    BookSearchScreen(
                        searchStatus = viewModel.searchUiState,
                        layout = bookScreenLayout,
                        scrollPosition = scrollPosition,
                        onCardClick = { viewModel.selectBook(it) },
                        onTryAgain = { viewModel.searchBooks(300) },
                        bookSelected = uiState.selectedBook,
                    )
                ScreenSelect.MY_BOOKS -> Text(uiState.searchResult!!.items.toString())
                ScreenSelect.FAVOURITES -> Text("""
                    ${uiState.selectedBook}
                    ${uiState.selectedBestseller}
                    ${uiState.currentScreen}
                    ${uiState.nytLists}
                """.trimIndent())
                else -> Text("null")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBookshelfTopBar(onUpButtonClick: () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.displayLarge
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        navigationIcon = {
            IconButton(onClick = onUpButtonClick) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back_button),
                )
            }
        }
    )
}
