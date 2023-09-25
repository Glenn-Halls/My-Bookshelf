package com.example.mybookshelf.model

import android.util.Log
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
import com.example.mybookshelf.data.MyBookRepository
import com.example.mybookshelf.data.NytListRepository
import com.example.mybookshelf.ui.util.BookshelfContentLayout
import com.example.mybookshelf.ui.util.BookshelfNavigationType
import com.example.mybookshelf.ui.util.NavigationElement
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
}


class BookshelfViewModel(
    private val bookRepository: BookRepository,
    private val bestsellerRepository: BestsellerRepository,
    private val nytListRepository: NytListRepository,
    private val myBookRepository: MyBookRepository,
) : ViewModel() {

    // Create observable state holder
    private val _uiState = MutableStateFlow(BookshelfUiState())
    // Accessor to state values
    val uiState: StateFlow<BookshelfUiState> = _uiState

    // Create observable search UI state holder
    var searchUiState: SearchUiState by mutableStateOf(SearchUiState.Loading)
        private set
    // Create observable NYT search UI state holder
    var nytUiState: NytUiState by mutableStateOf(NytUiState.Loading)

    // Navigate to selected screen
    fun navigateToScreen(nav: NavigationElement) {
        _uiState.update {
            it.copy(
                currentScreen = nav.screenSelect
            )
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

    // Get Bestsellers with an optional delay to display search attempt to user
    fun getBestsellers(delay: Long? = null) {
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
                NytUiState.Success(bestsellerSearch.results.bestsellerList)
            } catch (e: IOException) {
                Log.d("ViewModel", "IO Exception")
                NytUiState.Error
            } catch (e: HttpException) {
                Log.d("ViewModel", "HTTP Exception")
                NytUiState.Error
            }
        }
    }

    private fun getNytLists() {
        viewModelScope.launch {
            val nytListSearch: NytListSearch = try {
                nytListRepository.getNytLists()
            } catch (e: Exception) {
                Log.e("ViewModel", e.message.toString())
                NytListSearch(
                    listOf(
                        NytBestsellerList(
                            listName = "No Lists Found...",
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

    fun selectBook(book: Book) {
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

    fun selectNytList(nytList: String) {
        _uiState.update {
            it.copy(
                nytListSelected = nytList
            )
        }
    }

    fun setSearchString(search: String) {
        _uiState.update {
            it.copy(
                searchString = search
            )
        }
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

    fun navigateBack() {
        _uiState.update {
            it.copy(
                selectedBook = null,
                selectedBestseller = null,
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

    // TEST method for database
    suspend fun testSaveItem() {
        myBookRepository.insertBook(
            MyBook(
                id = "id",
                link = "link",
                title = "title",
                author = "author",
                description = "description",
                thumbnail = "thumbnail",
                rating = 1,
                isFavourite = true,
                notes = "notes"
            )
        )
    }

    suspend fun testSaveBook(book: Book) {
        myBookRepository.insertBook(
            MyBook(
            id = book.id,
            link = book.link,
            title = book.bookDetail.title,
            author = book.bookDetail.title,
            description = book.bookDetail.description,
            thumbnail = book.bookDetail.bookCover.thumbnail
            )
        )
    }

    init {
        searchBooks()
        getBestsellers()
        getNytLists()
        selectNytList("testing, 1, 2, 3...")
        setSearchString("jazz history")
        viewModelScope.launch {
            testSaveItem()
        }
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
