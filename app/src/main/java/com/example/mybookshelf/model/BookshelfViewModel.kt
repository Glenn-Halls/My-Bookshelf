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
import com.example.mybookshelf.data.NytListRepository
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

    fun searchBooks() {
        viewModelScope.launch {
            searchUiState = SearchUiState.Loading
            delay(300)
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

    fun getBestsellers() {
        viewModelScope.launch {
            nytUiState = NytUiState.Loading
            delay(300)
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
                selectedBook = book
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

    init {
        searchBooks()
        getBestsellers()
        getNytLists()
        selectNytList("testing, 1, 2, 3...")
        setSearchString("jazz history")
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MyBookshelfApplication)
                val bookRepository = application.container.bookRepository
                val bestsellerRepository = application.container.bestsellerRepository
                val nytListRepository = application.container.nytListRepository
                BookshelfViewModel(bookRepository, bestsellerRepository, nytListRepository)
            }
        }
    }
}
