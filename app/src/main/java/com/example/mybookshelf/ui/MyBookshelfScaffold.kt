package com.example.mybookshelf.ui


import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material3.Button
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.mybookshelf.R
import com.example.mybookshelf.data.convertToBestseller
import com.example.mybookshelf.data.sortBestsellers
import com.example.mybookshelf.data.sortMyBook
import com.example.mybookshelf.model.Bestseller
import com.example.mybookshelf.model.Book
import com.example.mybookshelf.model.BookshelfViewModel
import com.example.mybookshelf.model.NytUiState
import com.example.mybookshelf.model.SearchUiState
import com.example.mybookshelf.model.SortOrder
import com.example.mybookshelf.ui.util.BookshelfNavigationType
import com.example.mybookshelf.ui.util.MyBookScreen
import com.example.mybookshelf.ui.util.ScreenSelect
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MyBookshelfScreen(
    viewModel: BookshelfViewModel,
    windowSize: WindowSizeClass,
    modifier: Modifier = Modifier
) {
    // Context for use with app container
    val context = LocalContext.current
    // State Flow accessor to UI State
    val uiState by viewModel.uiState.collectAsState()
    // State Flow accessor to book database
    val bookshelfBooksDb by viewModel.myBookDb.collectAsState()
    // Sorted book list defined by user, defaulting to Last Updated
    val bookshelfBooks = bookshelfBooksDb.sortMyBook(
        uiState.myBookSortOrder ?: SortOrder.LAST_UPDATED
    )
    // State Flow accessor to bestseller database
    val myBestsellerDb by viewModel.myBestsellerDb.collectAsState()
    // Sorted bestseller list as defined by user, defaulting to Last Updated
    val myBestsellerBooks = myBestsellerDb.sortBestsellers(
        uiState.bestsellerSortOrder ?: SortOrder.LAST_UPDATED
    )
    // Define navigation type based on WindowSizeClass dimensions
    val navigationType = viewModel.getNavigationSetup(windowSize)
    // Get window height in order to NOT show top bar on compact screens
    val windowHeight = windowSize.heightSizeClass
    // Create a coroutine scope event listener
    val coroutineScope = rememberCoroutineScope()
    // Get screen content layout for Book search
    val bookScreenLayout = viewModel.getScreenLayout(windowSize, uiState.selectedBook)
    // Get screen content layout for MyBook
    val myBookScreenLayout = viewModel.getScreenLayout(windowSize, null, null, uiState.selectedMyBook)
    // Define & remember scroll state in order to use Launched Effect to update via coroutine
    val scrollPosition = rememberScrollState()
    // Separate from scroll position, remember list scroll state
    val listScrollPosition = rememberLazyGridState()
    // Separate from watchlist, shared position for MyBook and MyBook favourites
    val myBookListScrollPosition = rememberLazyGridState()
    // Get action button to display in title bar
    val actionButton = viewModel.getActionButton(myBookScreenLayout)
    // Get title for header based on screen selected
    val title = when (uiState.currentScreen) {
        ScreenSelect.NONE -> stringResource(R.string.app_name)
        ScreenSelect.BEST_SELLERS -> stringResource(R.string.best_sellers)
        ScreenSelect.WATCH_LIST -> stringResource(R.string.watch_list)
        ScreenSelect.BROWSE -> stringResource(R.string.browse_books)
        ScreenSelect.MY_BOOKS -> stringResource(R.string.my_books)
        ScreenSelect.FAVOURITES -> stringResource(R.string.my_favorites)
        null -> stringResource(R.string.app_name)
    }
    // Helper function adds / removes book to / from database within coroutine scope
    fun onBookmarkClick(book: Book) {
        coroutineScope.launch { viewModel.onBookmarkClick(book, bookshelfBooks) }
    }
    // Helper function toggles favourite tag and adds book to database in coroutine scope
    fun onFavouriteClick(book: Book) {
        coroutineScope.launch { viewModel.onFavouriteClick(book, bookshelfBooks) }
    }
    // Helper function adds custom review to MyBook
    fun onReviewCompletion() {
        coroutineScope.launch {
            viewModel.reviewMyBook()
            viewModel.toggleEditState()
        }
    }
    // Helper function removes current review state
    fun onReviewCancelled() {
        coroutineScope.launch {
            viewModel.userReviewUpdate(null, null, null)
            viewModel.toggleEditState()
        }
    }
    // Helper function when bestseller selected - updates search, reset selected book and scroll.
    fun onBestsellerClick(bestseller: Bestseller) {
        viewModel.updateSearchQuery("${bestseller.title} ${bestseller.author}")
        viewModel.updateSearch(context)
        viewModel.selectBook(null)
        coroutineScope.launch {
            listScrollPosition.scrollToItem(0,0)
        }
    }
    // Helper function on bestseller star click - adds bestseller to watch list
    fun onBestsellerStarClick(bestseller: Bestseller) {
        coroutineScope.launch {
            viewModel.toggleBestseller(bestseller)
        }
    }

    /*
     *  Scroll to the position defined in ViewModel on Re/Composition OR if selected book OR
     *  myBookSort Order changes OR when database is updated as reflected by
     *  bookshelfBooks. NB: viewModel.selectBook(book) will set scroll position to 0px.
     */
    LaunchedEffect(uiState.selectedBook, uiState.myBookSortOrder, uiState.currentScreen) {
        scrollPosition.scrollTo(uiState.scrollPosition)
        myBookListScrollPosition.scrollToItem(uiState.gridScrollPosition)
        Log.d("Launched Effect", "scroll to ${scrollPosition.value}")
        Log.d("Launched Effect", "grid to ${uiState.gridScrollPosition}")
    }
    // On Composable destruction, save scroll position to ViewModel.
    DisposableEffect(true) {
        onDispose {
            viewModel.setScrollPosition(scrollPosition.value)
            viewModel.setGridScrollPosition(myBookListScrollPosition.firstVisibleItemIndex)
            Log.d("Disposable Effect", "save scroll position ${scrollPosition.value}")
            Log.d("Disposable Effect", "save grid position ${myBookListScrollPosition.firstVisibleItemIndex}")
            Log.d("Disposable Effect", "saveD grid position ${uiState.gridScrollPosition}")
        }
    }
    // Enable back navigation via viewModel's up navigation logic
    BackHandler(true, viewModel::navigateBack)
    Scaffold(
        modifier = modifier,
        // Do not show top bar on compact screens
        topBar = {
            if (windowHeight != WindowHeightSizeClass.Compact) {
                MyBookshelfTopBar(
                    title = title,
                    onUpButtonClick = { viewModel.navigateBack() },
                    showActionButton = actionButton.showButton,
                    actionButtonVector = actionButton.icon,
                    isActionIconMirrored = actionButton.mirrorIcon ?: false,
                    onActionButtonClick = actionButton.action,
                    contentDescription = actionButton.contentDescription,
                )
            }
        },
        bottomBar = {
            if (navigationType == BookshelfNavigationType.BOTTOM_BAR) {
                BookshelfBottomNavBar(
                    currentScreen = uiState.currentScreen,
                    onTabPressed = { viewModel.navigateToScreen(it) }
                )
            }
        }
    ){ innerPadding ->
        Row(modifier = Modifier.padding(innerPadding)) {
            // Display navigation rail for non-compact-width screens
            if (navigationType == BookshelfNavigationType.NAVIGATION_RAIL) {
                BookshelfNavigationRail(
                    currentScreen = uiState.currentScreen,
                    onTabPressed = { viewModel.navigateToScreen(it) },
                    onNavigateBack = viewModel::navigateBack,
                    showBackButton = windowHeight == WindowHeightSizeClass.Compact
                )
            }
            // Determine screen to display based on current screen selection
            when (uiState.currentScreen) {
                ScreenSelect.NONE -> {
                    if (viewModel.searchUiState == SearchUiState.Loading) {
                        LoadingScreen()
                    } else {
                        Text("null")
                    }
                }
                ScreenSelect.BEST_SELLERS -> {
                    NytBestsellerScreen(
                        nytUiState = viewModel.nytUiState,
                        nytApiOnCooldown = uiState.nytApiOnCooldown,
                        nytApiCooldown = uiState.nytApiCooldown,
                        nytListList = uiState.nytLists ?: emptyList(),
                        myBestsellerList = myBestsellerBooks.sortBestsellers(SortOrder.LAST_ADDED),
                        onNytListClick = {
                            viewModel.selectNytList(it)
                            viewModel.updateBestsellerList(context)
                                         },
                        onCardClick = { onBestsellerClick(it) },
                        onStarClick = { onBestsellerStarClick(it) },
                        onTryAgain = { viewModel.getBestsellers(300) },
                        listSelected = uiState.selectedNytList,
                        hideTopBar = (windowHeight == WindowHeightSizeClass.Compact)
                    )
                }
                ScreenSelect.WATCH_LIST -> {
                    if (viewModel.nytUiState == NytUiState.Loading) {
                        LoadingScreen()
                    } else {
                        MyBestsellerGrid(
                            myBestsellers = myBestsellerBooks,
                            onCardClick = { onBestsellerClick(it.convertToBestseller()) },
                            onStarClick = { coroutineScope.launch {
                                viewModel.deleteMyBestseller(it)
                            } },
                        )
                    }
                }
                ScreenSelect.BROWSE -> {
                    when (viewModel.searchUiState) {
                        SearchUiState.Loading -> LoadingScreen()
                        SearchUiState.Error -> ErrorScreen({ viewModel.searchBooks(300) })
                        else -> {
                            if (uiState.searchResult == null) {
                                CustomSearchScreen(
                                    searchQuery = uiState.searchQuery ?: "",
                                    searchStringUpdate = { viewModel.setSearchString(it) },
                                    onSearchClicked = {
                                        viewModel.navigateBack()
                                        viewModel.updateSearch(context)
                                        coroutineScope.launch {
                                            listScrollPosition.scrollToItem(0, 0)
                                        }
                                    }
                                )
                            } else {
                                BookSearchScreen(
                                    searchStatus = viewModel.searchUiState,
                                    layout = bookScreenLayout,
                                    scrollPosition = scrollPosition,
                                    listScrollPosition = listScrollPosition,
                                    onCardClick = { viewModel.selectBook(it) },
                                    isMyBook = viewModel.isBookMyBook(),
                                    isFavourite = viewModel.isBookFavourite(),
                                    onFavouriteClick = { onFavouriteClick(it) },
                                    onBookmarkClick = { onBookmarkClick(it) },
                                    onTryAgain = { viewModel.searchBooks(300) },
                                    bookSelected = uiState.selectedBook,
                                )
                            }
                        }
                    }
                }
                ScreenSelect.MY_BOOKS -> {
                    val editInProgress = uiState.editInProgress
                    if (editInProgress) {
                        EditMyBookScreen(
                            myBook = uiState.selectedMyBook!!,
                            onFavouriteChange = { viewModel.userReviewUpdate(isFavourite = it) },
                            onRatingChange = { viewModel.userReviewUpdate(userRating = it.toFloat()) },
                            onEdit ={ viewModel.userReviewUpdate(userNotes = it) },
                            onCompletion = { onReviewCompletion() },
                            userReview = uiState.userReview,
                            onDelete = {coroutineScope.launch {
                                viewModel.deleteMyBook(it)
                            }},
                            onCancel = {
                                onReviewCancelled()
                                       },
                            onDismiss = { viewModel.toggleEditState() },
                        )
                    } else {
                        MyBookScreen(
                            layout = myBookScreenLayout,
                            myBooks = bookshelfBooks,
                            listScrollPosition = myBookListScrollPosition,
                            onCardClick = { viewModel.selectMyBook(it) },
                            scrollPosition = scrollPosition,
                            onEditClick = { viewModel.toggleEditState() },
                            onFavouriteClick = {
                                coroutineScope.launch{
                                    viewModel.toggleMyBookFavourite()
                                }
                            },
                            myBookSelected = uiState.selectedMyBook
                        )
                    }
                }
                ScreenSelect.FAVOURITES -> {
                    val editInProgress = uiState.editInProgress
                    if (editInProgress) {
                        EditMyBookScreen(
                            myBook = uiState.selectedMyBook!!,
                            onFavouriteChange = { viewModel.userReviewUpdate(isFavourite = it) },
                            onRatingChange = { viewModel.userReviewUpdate(userRating = it.toFloat()) },
                            onEdit ={ viewModel.userReviewUpdate(userNotes = it) },
                            onCompletion = { onReviewCompletion() },
                            userReview = uiState.userReview,
                            onDelete = { coroutineScope.launch {
                                viewModel.deleteMyBook(it)
                            } },
                            onCancel = {
                                onReviewCancelled()
                            },
                            onDismiss = { viewModel.toggleEditState() },
                        )
                    } else {
                        MyBookScreen(
                            layout = myBookScreenLayout,
                            myBooks = bookshelfBooks.filter { it.isFavourite },
                            listScrollPosition = myBookListScrollPosition,
                            onCardClick = { viewModel.selectMyBook(it) },
                            scrollPosition = scrollPosition,
                            onEditClick = { viewModel.toggleEditState() },
                            onFavouriteClick = {
                                coroutineScope.launch{
                                    viewModel.toggleMyBookFavourite()
                                }
                            },
                            myBookSelected = uiState.selectedMyBook
                        )
                    }
                }
                else -> if (
                    viewModel.searchUiState == SearchUiState.Loading ||
                    viewModel.nytUiState == NytUiState.Loading
                    ) {
                    LoadingScreen()
                } else {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Column(
                        ) {
                            Text(
                                text = uiState.nytApiOnCooldown.toString(),
                                style = MaterialTheme.typography.headlineLarge
                            )
                            Text(
                                text = uiState.nytApiCooldown.toString(),
                                style = MaterialTheme.typography.headlineLarge
                            )
                            Button(
                                onClick = { viewModel.testTimer() }
                            ) {
                                Text("TEST")
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBookshelfTopBar(
    title: String,
    onUpButtonClick: () -> Unit,
    showActionButton: Boolean,
    isActionIconMirrored: Boolean,
    actionButtonVector: ImageVector?,
    onActionButtonClick: () -> Unit,
    contentDescription: String?,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.displayLarge,
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
        },
        actions = {
                  if (showActionButton) {
                      IconButton(onClick = onActionButtonClick) {
                          if (isActionIconMirrored) {
                              Icon(
                                  imageVector = actionButtonVector ?: Icons.Default.BrokenImage,
                                  contentDescription = contentDescription,
                                  modifier = Modifier.scale(scaleX = -1f, scaleY = 1f)
                              )
                          } else {
                              Icon(
                                  imageVector = actionButtonVector ?: Icons.Default.BrokenImage,
                                  contentDescription = contentDescription
                              )
                          }
                      }
                  }
        },
    )
}
