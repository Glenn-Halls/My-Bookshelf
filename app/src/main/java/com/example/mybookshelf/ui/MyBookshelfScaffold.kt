package com.example.mybookshelf.ui


import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BrokenImage
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.mybookshelf.ProtoData.DarkMode
import com.example.mybookshelf.R
import com.example.mybookshelf.model.Bestseller
import com.example.mybookshelf.model.Book
import com.example.mybookshelf.model.BookshelfViewModel
import com.example.mybookshelf.model.NytBestsellerList
import com.example.mybookshelf.model.NytUiState
import com.example.mybookshelf.model.SearchUiState
import com.example.mybookshelf.model.SortOrder
import com.example.mybookshelf.model.extension.convertToBestseller
import com.example.mybookshelf.model.extension.sortBestsellers
import com.example.mybookshelf.model.extension.sortMyBook
import com.example.mybookshelf.ui.util.BookshelfBottomNavBar
import com.example.mybookshelf.ui.util.BookshelfNavigationRail
import com.example.mybookshelf.ui.util.BookshelfNavigationType
import com.example.mybookshelf.ui.util.ErrorScreen
import com.example.mybookshelf.ui.util.LoadingScreen
import com.example.mybookshelf.ui.util.MyBestsellerGrid
import com.example.mybookshelf.ui.util.NavigationTabs
import com.example.mybookshelf.ui.util.ScreenSelect
import com.example.mybookshelf.ui.util.SortOrderActionButtonList
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
    // State Flow accessor to myNytList database
    val myNytLists by viewModel.myNytListDb.collectAsState()
    // Get flow of dark mode status from view model
    val darkMode by viewModel.darkMode.collectAsState(null)
    // Get startup screen flow from view model
    val startupScreen by viewModel.startupScreen.collectAsState(null)
    // Get user preference sort order
    val userSortOrder by viewModel.protoSortOrder.collectAsState(null)
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
        ScreenSelect.SETTINGS -> stringResource(R.string.app_name)
        ScreenSelect.BEST_SELLERS -> stringResource(R.string.best_sellers)
        ScreenSelect.WATCH_LIST -> stringResource(R.string.watch_list)
        ScreenSelect.BROWSE -> stringResource(R.string.browse)
        ScreenSelect.MY_BOOKS -> stringResource(R.string.bookshelf)
        ScreenSelect.FAVOURITES -> stringResource(R.string.favourites)
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
//    fun onBestsellerClick(bestseller: Bestseller) {
//        viewModel.updateSearchQuery("${bestseller.title} ${bestseller.author}")
//        viewModel.updateSearch(context)
//        viewModel.selectBook(null)
//        coroutineScope.launch {
//            listScrollPosition.scrollToItem(0,0)
//        }
//    }
    fun onBestsellerClick(context: Context, bestseller: Bestseller) {
        coroutineScope.launch { viewModel.bestsellerBookSearch(context, bestseller) }
    }

    // Helper function on bestseller eye click - adds bestseller to watch list
    fun onBestsellerEyeClick(bestseller: Bestseller) {
        coroutineScope.launch {
            viewModel.toggleBestseller(bestseller)
        }
    }
    // Helper function on NYT bestseller star click = adds list to database
    fun onNytListStarClick(bestsellerList: NytBestsellerList) {
        coroutineScope.launch {
            viewModel.toggleMyNytList(bestsellerList)
        }
    }
    /*  Helper functions listed below are designed to reduce clutter composables where it is
        necessary to call view model functions within a coroutine scope.
     */
    fun setDarkMode(darkMode: DarkMode) {
        coroutineScope.launch {
            viewModel.setDarkMode(darkMode)
        }
    }
    fun setScreenSelect(screen: ScreenSelect?) {
        coroutineScope.launch {
            viewModel.setStartupScreen(screen)
        }
    }
    fun setSortOrder(sortOrder: SortOrder) {
        coroutineScope.launch {
            viewModel.updateProtoSortOrder(sortOrder)
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
                    onTitleClick = { viewModel.navigateHome() },
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
                ScreenSelect.BEST_SELLERS -> {
                    NytBestsellerScreen(
                        nytUiState = viewModel.nytUiState,
                        nytApiOnCooldown = uiState.nytApiOnCooldown,
                        nytApiCooldown = uiState.nytApiCooldown,
                        nytListList = uiState.nytLists,
                        myNytLists = myNytLists,
                        onNullNytListList = { viewModel.getNytLists(300) },
                        filterLists = uiState.favouriteFilter,
                        myBestsellerList = myBestsellerBooks.sortBestsellers(SortOrder.LAST_ADDED),
                        onNytListClick = {
                            viewModel.selectNytList(it)
                            viewModel.updateBestsellerList(context)
                                         },
                        onNytListStarClick = { onNytListStarClick(it) },
                        onCardClick = { onBestsellerClick(context, it) },
                        onStarClick = { onBestsellerEyeClick(it) },
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
                            onCardClick = { onBestsellerClick(context, it.convertToBestseller()) },
                            onStarClick = { coroutineScope.launch {
                                viewModel.deleteMyBestseller(it)
                            } },
                        )
                    }
                }
                ScreenSelect.BROWSE -> {
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
                    } else when (viewModel.searchUiState) {
                        SearchUiState.Loading -> LoadingScreen()
                        SearchUiState.Error -> ErrorScreen({ viewModel.searchBooks(300) })
                        else -> {
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
                ScreenSelect.SETTINGS -> {
                    if (viewModel.nytUiState == NytUiState.Loading) {
                        LoadingScreen()
                    } else {
                        SettingsScreen(
                            darkMode = darkMode,
                            onDarkModeClick = {
                                setDarkMode(it)
                                coroutineScope.launch{
                                    viewModel.updateMyBestsellerDatabase(context)
                                }
                                              },
                            startScreen = startupScreen,
                            navTabList = NavigationTabs,
                            onStartScreenClick = { setScreenSelect(it.screenSelect) },
                            sortOrderSetting = userSortOrder,
                            sortOrderOptions = SortOrderActionButtonList,
                            onSortOrderClick = { setSortOrder(it) },

                        )
                    }
                }
                null -> {
                    if (viewModel.nytUiState == NytUiState.Loading) {
                        LoadingScreen()
                    } else {
                        val activity = (LocalContext.current as? Activity)
                        HomeScreen(
                            navigationType = navigationType,
                            navigationElements = NavigationTabs,
                            onIconClick = { viewModel.navigateToScreen(it) },
                            showExitDialog = uiState.showExitDialog,
                            onExitCancel = { viewModel.hideExitDialog() },
                            onExitConfirm = { activity?.finishAndRemoveTask() }
                        )
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
    onTitleClick: () -> Unit,
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
                modifier = Modifier.clickable(true, onClick = onTitleClick)
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        navigationIcon = {
            IconButton(
                onClick = onUpButtonClick,
                modifier = Modifier.fillMaxHeight()
                ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back_button),
                    modifier = Modifier.fillMaxSize()
                )
            }
        },
        actions = {
                  if (showActionButton) {
                      IconButton(
                          onClick = onActionButtonClick,
                          modifier = Modifier.fillMaxHeight(),
                          ) {
                          if (isActionIconMirrored) {
                              Icon(
                                  imageVector = actionButtonVector ?: Icons.Default.BrokenImage,
                                  contentDescription = contentDescription,
                                  modifier = Modifier
                                      .fillMaxSize(.85f)
                                      .scale(scaleX = -1f, scaleY = 1f)
                              )
                          } else {
                              Icon(
                                  imageVector = actionButtonVector ?: Icons.Default.BrokenImage,
                                  contentDescription = contentDescription,
                                  modifier = Modifier.fillMaxSize(.85f)
                              )
                          }
                      }
                  }
        },
    )
}
