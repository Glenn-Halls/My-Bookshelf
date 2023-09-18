package com.example.mybookshelf.model

import android.util.Log
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookshelfViewModel(
    private val bookRepository: BookRepository,
    private val bestsellerRepository: BestsellerRepository,
    private val nytListRepository: NytListRepository,
) : ViewModel() {

    // Create observable state holder
    private val _uiState = MutableStateFlow(BookshelfUiState())
    // Accessor to state values
    val uiState: StateFlow<BookshelfUiState> = _uiState

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
            val searchResult: BookSearchResult = try {
                bookRepository.getBooks()
            } catch (e: Exception) {
                Log.e("ViewModel", e.message.toString())
                BookSearchResult(0, emptyList())
            }
            _uiState.update {
                it.copy(
                    searchResult = searchResult
                )
            }
        }
    }

    fun getBestsellers() {
        viewModelScope.launch {
            val bestsellerSearch: BestsellerSearch = try {
                bestsellerRepository.getBestsellers()
            } catch (e: Exception) {
                Log.e("ViewModel", e.message.toString())
                BestsellerSearch(0, BestsellerResults(
                    "",
                    "",
                    "",
                    listOf(FakeBestseller)
                ))
            }
            _uiState.update {
                it.copy(
                    bestSellers = bestsellerSearch
                )
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
