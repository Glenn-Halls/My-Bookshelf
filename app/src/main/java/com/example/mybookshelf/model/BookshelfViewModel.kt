package com.example.mybookshelf.model

import android.content.Context
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mybookshelf.MyBookshelfApplication
import com.example.mybookshelf.data.BestsellerRepository
import com.example.mybookshelf.data.BookRepository
import com.example.mybookshelf.data.DefaultAppContainer
import com.example.mybookshelf.data.MyBookRepository
import com.example.mybookshelf.data.NetworkBestsellerRepository
import com.example.mybookshelf.data.NetworkBookRepository
import com.example.mybookshelf.data.NytListRepository
import com.example.mybookshelf.ui.util.ActionButton
import com.example.mybookshelf.ui.util.BookshelfContentLayout
import com.example.mybookshelf.ui.util.BookshelfNavigationType
import com.example.mybookshelf.ui.util.NavigationElement
import com.example.mybookshelf.ui.util.ScreenSelect
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface SearchUiState {
    data class Success(val bookList: List<Book>) : SearchUiState
    object Error: SearchUiState
    object Loading: SearchUiState
}

sealed interface NytUiState {
    data class Success(val bestsellerList: List<Bestseller>) : NytUiState
    object Error: NytUiState
    object Loading: NytUiState
    object Ready: NytUiState
}


@OptIn(ExperimentalCoroutinesApi::class)
class BookshelfViewModel(
    private var bookRepository: BookRepository,
    private var bestsellerRepository: BestsellerRepository,
    private val nytListRepository: NytListRepository,
    private val myBookRepository: MyBookRepository,
) : ViewModel() {

    // Create observable state holder
    private val _uiState = MutableStateFlow(BookshelfUiState())

    // Accessor to state values
    val uiState: StateFlow<BookshelfUiState> = _uiState

    // Accessor to my book database
    val myBookDb: StateFlow<List<MyBook>> = myBookRepository
        .getAllBooksStream()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    // Create observable search UI state holder
    var searchUiState: SearchUiState by mutableStateOf(SearchUiState.Loading)
        private set

    // Create observable NYT search UI state holder
    var nytUiState: NytUiState by mutableStateOf(NytUiState.Loading)
        private set

    private val _nytQueryState = MutableStateFlow(NytQueryStatus.READY)

    //Temporary function to test cooldown timer flow
    fun testTimer() {
        startNytCountdownTimer()
        _uiState.update {
            it.copy(
                myBookSortOrder = QuerySortOrder.LAST_UPDATED
            )
        }
    }

    private fun getNytCountdownFlow(isRunning: Boolean): Flow<Int> {
        return flow {
            val startValue = 12
            var currentValue = startValue
            while (isRunning) {
                emit(currentValue)
                while (currentValue >= 0) {
                    delay(1000L)
                    currentValue--
                    emit(currentValue)
                }
            }
        }
    }

    private fun startNytCountdownTimer() {
        _nytQueryState.update { NytQueryStatus.RESET }
        _nytQueryState.update { NytQueryStatus.WAITING }
        _uiState.update { it.copy(nytApiOnCooldown = true) }
    }

    // Navigate to selected screen
    fun navigateToScreen(nav: NavigationElement) {
        _uiState.update {
            it.copy(
                currentScreen = nav.screenSelect
            )
        }
    }

    fun sortMyBooks(
        myBookList: List<MyBook>,
        sortOrder: QuerySortOrder?
    ): List<MyBook> {
        return when (sortOrder) {
            QuerySortOrder.ALPHABETICAL -> myBookList.sortedBy{it.title}
            QuerySortOrder.ALPHABETICAL_REVERSE -> myBookList.sortedBy{it.title}
            QuerySortOrder.LAST_UPDATED -> myBookList.sortedBy{it.lastUpdated}.reversed()
            QuerySortOrder.LAST_UPDATED_REVERSE -> myBookList.sortedBy { it.lastUpdated }.reversed()
            QuerySortOrder.LAST_ADDED -> myBookList.reversed()
            QuerySortOrder.LAST_ADDED_REVERSE -> myBookList
            null -> myBookList
        }
    }

    // Get screen layout based on window size class and context
    fun getScreenLayout(
        windowSize: WindowSizeClass,
        book: Book? = null,
        bestseller: Bestseller? = null,
        myBook: MyBook? = null,
    ): BookshelfContentLayout {
        return if (
            book != null || bestseller != null || myBook != null
        ) {
            if (windowSize.widthSizeClass == WindowWidthSizeClass.Compact) {
                BookshelfContentLayout.DETAILS_ONLY
            } else {
                BookshelfContentLayout.LIST_AND_DETAILS
            }
        } else BookshelfContentLayout.LIST_ONLY
    }

    // Search for books with an optional delay to display search attempt to user
    fun searchBooks(delay: Long? = null) {
        viewModelScope.launch {
            searchUiState = SearchUiState.Loading
            if (delay != null) {
                delay(delay)
            }
            searchUiState = try {
                val searchResult = bookRepository.getBooks()
                val myBooks = myBookDb.value
                val myFavourites = myBooks.filter { it.isFavourite }
                searchResult.myBooksInSearch = myBooks.map { it.id }
                searchResult.favouritesInSearch = myFavourites.map { it.id }
                _uiState.update {
                    it.copy(
                        searchResult = searchResult
                    )
                }
                SearchUiState.Success(searchResult.items)
            } catch (e: IOException) {
                Log.d("ViewModel", "IO Exception")
                SearchUiState.Error
            } catch (e: HttpException) {
                Log.d("ViewModel", "HTTP Exception")
                SearchUiState.Error
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update {
            it.copy(searchQuery = query)
        }
    }

    private fun resetSearchResults() {
        _uiState.update {
            it.copy(searchResult = null)
        }
    }

    fun updateSearch(context: Context) {
        bookRepository = NetworkBookRepository(
            bookshelfApiService = DefaultAppContainer(context).bookRetrofitService,
            searchString = uiState.value.searchQuery ?: ""
        )
        searchBooks()
        _uiState.update {
            it.copy(
                currentScreen = ScreenSelect.BROWSE
            )
        }
    }

    fun updateBestsellerList(context: Context) {
        bestsellerRepository = NetworkBestsellerRepository(
            bestsellerApiService = DefaultAppContainer(context = context).bestsellerRetrofitService,
            nytList = uiState.value.selectedNytList?.listLocation ?: ""
        )
        getBestsellers(100)
        _uiState.update {
            it.copy(
                currentScreen = ScreenSelect.BEST_SELLERS
            )
        }
    }

    // Get Bestsellers with an optional delay to display network search attempt to user
    fun getBestsellers(delay: Long? = 100) {
        viewModelScope.launch {
            nytUiState = NytUiState.Loading
            if (delay != null) {
                delay(delay)
            }
            nytUiState = try {
                val bestsellerSearch = bestsellerRepository.getBestsellers()
                _uiState.update {
                    it.copy(
                        bestseller = bestsellerSearch
                    )
                }
                NytUiState.Success(
                    bestsellerSearch.results.bestsellerList
                )
            } catch (e: IOException) {
                Log.d("ViewModel", "IO Exception")
                NytUiState.Error
            } catch (e: HttpException) {
                Log.d("ViewModel", "HTTP Exception")
                Log.d("message", e.message())
                if (e.message().equals("Too Many Requests")) {
                    startNytCountdownTimer()
                }
                NytUiState.Error
            }
        }
    }

    private fun getNytLists(delay: Long? = null) {
        viewModelScope.launch {
            nytUiState = NytUiState.Loading
            if (delay != null) {
                delay(delay)
            }
            nytUiState = try {
                val nytSearchResult = nytListRepository.getNytLists()
                val nytListList = nytSearchResult.results
                _uiState.update {
                    it.copy(
                        nytLists = nytListList
                    )
                }
                NytUiState.Success(emptyList())
            } catch (e: IOException) {
                Log.d("ViewModel NYT Network State", "IO Exception")
                NytUiState.Error
            } catch (e: HttpException) {
                Log.d("ViewModel NYT Network State", "HTTP Exception")
                NytUiState.Error
            }


            val nytListSearch: NytListSearch = try {
                nytListRepository.getNytLists()
            } catch (e: Exception) {
                Log.e("ViewModel", e.message.toString())
                NytListSearch(
                    listOf(
                        NytBestsellerList(
                            listName = "No Lists Found...",
                            listLocation = "",
                            publishedDate = "",
                            firstPublished = "",
                            frequency = "",
                        )
                    )
                )
            }
            _uiState.update {
                it.copy(
                    nytLists = nytListSearch.results
                )
            }
        }
    }

    fun selectBook(book: Book?) {
        _uiState.update {
            it.copy(
                selectedBook = book,
                scrollPosition = 0,
            )
        }
    }

    fun selectBestseller(bestseller: Bestseller) {
        _uiState.update {
            it.copy(
                selectedBestseller = bestseller
            )
        }
    }

    fun selectMyBook(myBook: MyBook?) {
        _uiState.update {
            it.copy(
                selectedMyBook = myBook
            )
        }
    }

    fun selectNytList(nytList: NytBestsellerList?) {
        _uiState.update {
            it.copy(
                selectedNytList = nytList
            )
        }
    }

    fun setSearchString(search: String) {
        _uiState.update {
            it.copy(
                searchQuery = search
            )
        }
    }

    fun toggleEditState() {
        val currentState = uiState.value.editInProgress
        _uiState.update {
            it.copy(
                editInProgress = !currentState
            )
        }
    }

    // Update UI state user review to survive recomposition
    fun userReviewUpdate(
        isFavourite: Boolean? = uiState.value.userReview?.isFavourite,
        userRating: Float? = uiState.value.userReview?.userRating,
        userNotes: String? = uiState.value.userReview?.userNotes,
    ) {
        _uiState.update {
            it.copy(
                userReview = UserReview(
                    userNotes = userNotes,
                    userRating = userRating,
                    isFavourite = isFavourite,
                )
            )
        }
    }


    // When book selected from book search, verify if selected book is favourite in database
    fun isBookFavourite(): Boolean {
        val bookId = uiState.value.selectedBook?.id ?: "no book id"
        return uiState.value.searchResult?.favouritesInSearch?.contains(bookId) ?: false
    }

    // When book has been selected from book search, verify if book exists in database
    fun isBookMyBook(): Boolean {
        val bookId = uiState.value.selectedBook?.id ?: "no book id"
        return uiState.value.searchResult?.myBooksInSearch?.contains(bookId) ?: false
    }

    // Get navigation setup based on window size
    fun getNavigationSetup(windowSize: WindowSizeClass): BookshelfNavigationType {
        return when (windowSize.widthSizeClass) {
            WindowWidthSizeClass.Compact -> BookshelfNavigationType.BOTTOM_BAR
            WindowWidthSizeClass.Medium -> BookshelfNavigationType.NAVIGATION_RAIL
            WindowWidthSizeClass.Expanded -> BookshelfNavigationType.NAVIGATION_DRAWER
            else -> BookshelfNavigationType.BOTTOM_BAR
        }
    }

    // Temporary navigation handler
    fun navigateBack() {
        _uiState.update {
            it.copy(
                selectedBook = null,
                selectedBestseller = null,
                selectedMyBook = null,
            )
        }
    }

    fun setScrollPosition(position: Int) {
        _uiState.update {
            it.copy(
                scrollPosition = position
            )
        }
    }

    /*
     *   When user clicks on favourite tab:
     *      - If book is already in database:
     *          - If book is favourite, make favourite = false and remove from favouritesInSearch
     *          - If book is not favourite, make favourite = true and add to favouritesInSearch
     *      - If book is not in database, add to database with favourite = true
     */
    suspend fun onFavouriteClick(book: Book, bookShelfList: List<MyBook>) {
        val bookIsMyBook: Boolean = book.id in bookShelfList.map {it.id}
        if (bookIsMyBook) {
            val myBook = (bookShelfList.filter { it.id == book.id })[0]
            val searchResult = _uiState.value.searchResult
            val oldFavouriteList = searchResult!!.favouritesInSearch
            val bookIsFavourite: Boolean = book.id in oldFavouriteList
            toggleFavourite(myBook)
            val newFavouriteList = if (bookIsFavourite) {
                oldFavouriteList.filter { it != book.id }
            } else {
                oldFavouriteList + book.id
            }
            searchResult.favouritesInSearch = newFavouriteList
            _uiState.update {
                it.copy(
                    searchResult = searchResult
                )
            }
        } else {
            saveBook(book, true)
        }
    }

    // Toggle favourite: Boolean value of book within database
    suspend fun toggleFavourite(myBook: MyBook) {
        val newBook = myBook.copy(
            isFavourite = !myBook.isFavourite,
            lastUpdated = System.currentTimeMillis()
        )
        myBookRepository.updateBook(newBook)
    }

    // If book is in database, remove, if not, add to database
    suspend fun onBookmarkClick(book: Book, bookshelfList: List<MyBook>) {
        val bookIsMyBook: Boolean = book.id in bookshelfList.map {it.id}
        if (bookIsMyBook) {
            val myBook = bookshelfList.find { it.id == book.id }
            deleteBook(myBook!!)
        } else {
            saveBook(book)
        }
    }

    suspend fun toggleMyBookFavourite() {
        reviewMyBook(
            UserReview(isFavourite = !uiState.value.selectedMyBook!!.isFavourite)
        )
    }

    suspend fun reviewMyBook(review: UserReview? = null) {
        val userReview = review ?: uiState.value.userReview
        val myBook = uiState.value.selectedMyBook!!
        val newBook = myBook.copy(
            isFavourite = userReview?.isFavourite ?: myBook.isFavourite,
            rating = userReview?.userRating?.toInt() ?: myBook.rating,
            notes = userReview?.userNotes ?: myBook.notes,
            lastUpdated = System.currentTimeMillis()
        )
        myBookRepository.updateBook(newBook)
        _uiState.update {
            it.copy(
                userReview = null,
                selectedMyBook = newBook
            )
        }
    }

    suspend fun deleteMyBook(myBook: MyBook) {
        _uiState.update {
            it.copy(
                selectedMyBook = null,
                editInProgress = false,
            )
        }
        myBookRepository.deleteBook(myBook)
    }

    fun getActionButton(): ActionButton {
        return when (uiState.value.currentScreen) {
            ScreenSelect.NONE -> ActionButton(false,)
            ScreenSelect.BEST_SELLERS -> ActionButton(
                true,
                Icons.Filled.Whatshot,
                { selectNytList(null) },
                "select bestseller list",
            )

            ScreenSelect.WATCH_LIST -> ActionButton(
                true,
                Icons.Filled.Remove,
                { selectNytList(null) },
                contentDescription = "sort by alphabetical order"
            )
            ScreenSelect.BROWSE -> ActionButton(
                showButton = true,
                icon = Icons.Filled.Search,
                action = {
                    selectBook(null)
                    resetSearchResults()
                         },
                contentDescription = "search",
            )

            ScreenSelect.MY_BOOKS -> ActionButton(false)
            ScreenSelect.FAVOURITES -> ActionButton(false)
            null -> ActionButton(false)
        }
    }

    // Add book to MyBook database with isFavourite flag by converting book -> MyBook.
    // Update _uiState values for myBooksInSearch and favouritesInSearch, accordingly
    private suspend fun saveBook(
        book: Book,
        isFavourite: Boolean = false,
        ) {
        myBookRepository.insertBook(
            MyBook(
            id = book.id,
            link = book.link,
            title = book.bookDetail.title,
            author = book.bookDetail.authors[0],
            date = book.bookDetail.date,
            description = book.bookDetail.description,
            thumbnail = book.bookDetail.bookCover.thumbnail,
            isFavourite = isFavourite,
            lastUpdated = System.currentTimeMillis(),
            )
        )
        if (searchUiState is SearchUiState.Success) {
            val searchResult = _uiState.value.searchResult
            val newMyBookList = searchResult!!.myBooksInSearch + book.id
            searchResult.myBooksInSearch = newMyBookList
            if (isFavourite) {
                val newFavouriteList = searchResult.favouritesInSearch + book.id
                searchResult.favouritesInSearch = newFavouriteList
            }
            _uiState.update {
                it.copy(
                    searchResult = searchResult
                )
            }
        }
    }

    // Delete MyBook from database.
    // Update _uiState values for myBooksInSearch and favouritesInSearch accordingly.
    private suspend fun deleteBook(mybook: MyBook) {
        val searchResult = _uiState.value.searchResult
        val newMyBookList = searchResult!!.myBooksInSearch.filter { it != mybook.id }
        val newFavouriteList = searchResult.favouritesInSearch.filter { it != mybook.id }
        searchResult.myBooksInSearch = newMyBookList
        searchResult.favouritesInSearch = newFavouriteList
        myBookRepository.deleteBook(mybook)
        _uiState.update {
            it.copy(
                searchResult = searchResult
            )
        }
    }

    // On viewModel initiation, get search results to populate data and set timer function
    init {
        // When triggered, start countdown flow and once time = 0, set status to ready
        _nytQueryState
            .flatMapLatest {
                getNytCountdownFlow(
                    isRunning = it == NytQueryStatus.WAITING
                )
            }
            .onEach { timerUpdate ->
                _uiState.update { it.copy(nytApiCooldown = timerUpdate) }
                if (timerUpdate == 0) {
                    _uiState.update {
                        it.copy(nytApiOnCooldown = false)
                    }
                    _nytQueryState.update { NytQueryStatus.READY }
                    nytUiState = NytUiState.Ready
                }
            }
            .launchIn(viewModelScope)
        searchBooks()
        getBestsellers()
        getNytLists()
        setSearchString("jazz history")
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MyBookshelfApplication)
                val bookRepository = application.container.bookRepository
                val bestsellerRepository = application.container.bestsellerRepository
                val nytListRepository = application.container.nytListRepository
                val myBookRepository = application.container.myBookRepository
                BookshelfViewModel(
                    bookRepository,
                    bestsellerRepository,
                    nytListRepository,
                    myBookRepository
                    )
            }
        }
    }
}
